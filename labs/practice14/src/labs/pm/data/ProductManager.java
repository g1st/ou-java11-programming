/*
 * Copyright (c) 2021. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package labs.pm.data;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.FormatStyle;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ProductManager {
    private Map<Product, List<Review>> products = new HashMap<>();
    //    private ResourceFormatter formatter;
    private final ResourceBundle config = ResourceBundle.getBundle("labs.pm.data.config");
    private final MessageFormat productFormat = new MessageFormat(config.getString("product.data.format"));
    private final MessageFormat reviewFormat = new MessageFormat(config.getString("review.data.format"));
    private final Path reportsFolder = Path.of(config.getString("reports.folder"));
    private final Path dataFolder = Path.of(config.getString("data.folder"));
    private final Path tempFolder = Path.of(config.getString("temp.folder"));
    private static Map<String, ResourceFormatter> formatters =
            Map.of("en-GB", new ResourceFormatter(Locale.UK),
                    "en-US", new ResourceFormatter(Locale.US),
                    "fr-FR", new ResourceFormatter(Locale.FRANCE),
                    "ru-RU", new ResourceFormatter(new Locale("ru", "RU")),
                    "zh-CN", new ResourceFormatter(Locale.CHINA));
    private static final Logger logger = Logger.getLogger(ProductManager.class.getName());
    private static final ProductManager pm = new ProductManager();

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock writeLock = lock.writeLock();
    private final Lock readLock = lock.readLock();


    public static ProductManager getInstance() {
        return pm;
    }

//    public ProductManager(Locale locale) {
//        this(locale.toLanguageTag());
//    }

    private ProductManager() {
//        changeLocale(languageTag);
        loadAllData();
    }

//    public void changeLocale(String languageTag) {
//        formatter = formatters.getOrDefault(languageTag, formatters.get("en-GB"));
//    }

    public static Set<String> getSupportedLocales() {
        return formatters.keySet();
    }

    // this is factory method
    public Product createProduct(int id, String name, BigDecimal price, Rating rating, LocalDate bestBefore) {
        Product product = null;
        try {
            writeLock.lock();
            product = new Food(id, name, price, rating, bestBefore);
            products.putIfAbsent(product, new ArrayList<>());

        } catch (Exception ex) {
            logger.log(Level.INFO, "Error adding product " + ex.getMessage());
            return null;
        } finally {
            writeLock.unlock();
        }
        return product;
    }

    // this is factory method too
    public Product createProduct(int id, String name, BigDecimal price, Rating rating) {
        Product product = null;
        try {
            writeLock.lock();
            product = new Drink(id, name, price, rating);
            products.putIfAbsent(product, new ArrayList<>());
        } catch (Exception ex) {
            logger.log(Level.INFO, "Error adding product " + ex.getMessage());
            return null;
        } finally {
            writeLock.unlock();
        }
        return product;
    }

    private Product reviewProduct(Product product, Rating rating, String comments) {
        List<Review> reviews = products.get(product);

        reviews.add(new Review(rating, comments));

        products.remove(product);

        Rating avgRating = getAverageRating(reviews);
        Product newProduct = product.applyRating(avgRating);
        products.putIfAbsent(newProduct, reviews);
        return newProduct;
    }

    public Product reviewProduct(int id, Rating rating, String comments) {
        try {
            writeLock.lock();
            return reviewProduct(findProduct(id), rating, comments);
        } catch (ProductManagerException e) {
            logger.log(Level.INFO, e.getMessage());
        } finally {
            writeLock.unlock();
        }
        return null;
    }

    public void printProductReport(int id, String languageTag, String client) {
        try {
            readLock.lock();
            printProductReport(findProduct(id), languageTag, client);
        } catch (ProductManagerException e) {
            logger.log(Level.INFO, e.getMessage());
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error printing product report " + e.getMessage(), e);
        } finally {
            readLock.unlock();
        }
    }

    public void printProductReport(Product product, String languageTag, String client) throws IOException {
        List<Review> reviews = products.get(product);
        Collections.sort(reviews);
        ResourceFormatter formatter = formatters.getOrDefault(languageTag, formatters.get("en-GB"));

        // Create folder
        Files.createDirectories(reportsFolder);

        Path productFile = reportsFolder.resolve(
                MessageFormat.format(config.getString("report.file"), product.getId(), client));
        try (PrintWriter out = new PrintWriter(new OutputStreamWriter(
                Files.newOutputStream(productFile, StandardOpenOption.CREATE),
                "UTF-8"))
        ) {
            out.append(formatter.formatProduct(product));
            out.append(System.lineSeparator());

            if (reviews.isEmpty()) {
                out.append(formatter.getText("no.reviews"));
                out.append(System.lineSeparator());
            } else {
                String reviewsText = reviews
                        .stream()
                        .map(review -> formatter.formatReview(review) + System.lineSeparator())
                        .collect(Collectors.joining());
                out.append(reviewsText);
            }

        }

    }

    public void printProducts(Predicate<Product> filter, Comparator<Product> sorter, String languageTag) {
        try {
            readLock.lock();

            StringBuilder txt = new StringBuilder();
            ResourceFormatter formatter = formatters.getOrDefault(languageTag, formatters.get("en-GB"));

            products.keySet()
                    .stream()
                    .sorted(sorter)
                    .filter(filter)
                    .forEach(product ->
                            txt.append(formatter.formatProduct(product) + '\n'));
            System.out.println(txt);
        } finally {
            readLock.unlock();
        }
    }

    private Review parseReview(String text) {
        Review review = null;
        try {
            Object[] values = reviewFormat.parse(text);
            review = new Review(
                    Rateable.convert(Integer.parseInt((String) values[0])),
                    (String) values[1]);

        } catch (ParseException | NumberFormatException e) {
            logger.log(Level.WARNING, "Error parsing review " + text, e);
        }
        return review;
    }

    private Product parseProduct(String text) {
        Product product = null;
        try {
            Object[] values = productFormat.parse(text);
            String type = (String) values[0];
            int id = Integer.parseInt((String) values[1]);
            String name = (String) values[2];
            BigDecimal price = BigDecimal.valueOf(Double.parseDouble((String) values[3]));
            Rating rating = Rateable.convert(Integer.parseInt((String) values[4]));
            switch (type) {
                case "D":
                    product = new Drink(id, name, price, rating);
                    break;
                case "F":
                    LocalDate bestBefore = LocalDate.parse((String) values[5]);
                    product = new Food(id, name, price, rating, bestBefore);
                    break;
            }

        } catch (ParseException | NumberFormatException | DateTimeParseException e) {
            logger.log(Level.WARNING, "Error parsing product " + text + " " + e.getMessage(), e);
        }
        return product;
    }

    private List<Review> loadReviews(Product product) {
        List<Review> reviews = null;
        Path file = dataFolder.resolve(
                MessageFormat.format(
                        config.getString("reviews.data.file"),
                        product.getId()
                )
        );

        if (Files.notExists(file)) {
            reviews = new ArrayList<>();
        } else {
            try {
                reviews = Files.lines(file, Charset.forName("UTF-8"))
                        .map(text -> parseReview(text))
                        .filter(r -> r != null)
                        .collect(Collectors.toList());
            } catch (IOException e) {
                logger.log(Level.WARNING, "Error loading reviews " + e.getMessage(), e);
            }
        }
        return reviews;
    }

    private Product loadProduct(Path file) {
        Product product = null;
        if (Files.exists(file)) {
            try {
                product = parseProduct(
                        Files.lines(dataFolder.resolve(file), Charset.forName("UTF-8")).findFirst().orElseThrow()
                );
            } catch (Exception e) {
                logger.log(Level.WARNING, "Error loading product " + e.getMessage(), e);
            }
        }
        return product;
    }

    private void loadAllData() {
        try {
            products = Files.list(dataFolder)
                    .filter(file -> file.getFileName().toString().startsWith("product"))
                    .map(file -> loadProduct(file))
                    .filter(product -> product != null)
                    .collect(Collectors.toMap(
                            product -> product,
                            product -> loadReviews(product)
                    ));
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error loading data " + e.getMessage(), e);
        }
    }

    private void dumpData() {
        try {
            if (Files.notExists(tempFolder)) {
                Files.createDirectory(tempFolder);
            }
            Path tempFile = tempFolder.resolve(
                    MessageFormat.format(config.getString("temp.file"), Instant.now().toString().split("T")[0]));
            try (ObjectOutputStream out = new ObjectOutputStream(
                    Files.newOutputStream(tempFile, StandardOpenOption.CREATE))) {
                out.writeObject(products);
                products = new HashMap<>();
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error dumping data " + e.getMessage(), e);
        }
    }

    @SuppressWarnings("unchecked")
    private void restoreData() {
        try {
            Path tempFile = Files.list(tempFolder)
                    .filter(
                            path -> path.getFileName().toString().endsWith("tmp"))
                    .findFirst().orElseThrow();
            try (ObjectInputStream in = new ObjectInputStream(
                    Files.newInputStream(tempFile, StandardOpenOption.DELETE_ON_CLOSE))) {
                products = (HashMap) in.readObject();

            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error restoring data " + e.getMessage(), e);
        }
    }

    public Rating getAverageRating(List<Review> reviews) {
        return Rateable.convert(
                (int) Math.round(
                        reviews.stream()
                                .mapToInt(r -> r.getRating().ordinal())
                                .average()
                                .orElse(0)
                )
        );
    }

    public Product findProduct(int id) throws ProductManagerException {
        try {

            readLock.lock();
            return products.keySet()
                    .stream()
                    .filter(product -> product.getId() == id)
                    .findFirst()
                    //.get(); // get throws NoSuchElementException for us
                    .orElseThrow(() -> new ProductManagerException(
                            "Product with id " + id + " not found")); // this one is not a runtime exception, it's a checked exception so we need to catch it or propagate to the calling method
//                    .orElseGet(() -> null);
        } finally {
            readLock.unlock();
        }
    }

    public Map<String, String> getDiscounts(String languageTag) {
        try {
            readLock.lock();
            ResourceFormatter formatter = formatters.getOrDefault(languageTag, formatters.get("en-GB"));

            return products.keySet()
                    .stream()
                    .collect(
                            Collectors.groupingBy(
                                    p -> p.getRating().getStars(),
                                    Collectors.collectingAndThen(
                                            Collectors.summingDouble(d -> d.getDiscount().doubleValue()),
                                            discount -> formatter.moneyFormat.format(discount)
                                    )
                            )
                    );
        } finally {
            readLock.unlock();
        }
    }

    private static class ResourceFormatter {
        private Locale locale;
        private ResourceBundle resources;
        private DateTimeFormatter dateFormat;
        private NumberFormat moneyFormat;

        private ResourceFormatter(Locale locale) {
            this.locale = locale;
            resources = ResourceBundle.getBundle("labs.pm.data.resources", locale);
            dateFormat = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).localizedBy(locale);
            moneyFormat = NumberFormat.getCurrencyInstance(locale);
        }

        private String formatProduct(Product product) {
            return MessageFormat.format(resources.getString("product"),
                    product.getName(),
                    moneyFormat.format(product.getPrice()),
                    product.getRating().getStars(),
                    dateFormat.format(product.getBestBefore()));
        }

        private String formatReview(Review review) {
            return MessageFormat.format(
                    resources.getString("review"),
                    review.getRating().getStars(),
                    review.getComments()
            );
        }

        private String getText(String key) {
            return resources.getString(key);
        }
    }
}
