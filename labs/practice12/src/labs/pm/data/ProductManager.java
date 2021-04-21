/*
 * Copyright (c) 2021. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package labs.pm.data;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ProductManager {
    private Map<Product, List<Review>> products = new HashMap<>();
    private ResourceFormatter formatter;
    private static Map<String, ResourceFormatter> formatters =
            Map.of("en-GB", new ResourceFormatter(Locale.UK),
                    "en-US", new ResourceFormatter(Locale.US),
                    "fr-FR", new ResourceFormatter(Locale.FRANCE),
                    "ru-RU", new ResourceFormatter(new Locale("ru", "RU")),
                    "zh-CN", new ResourceFormatter(Locale.CHINA));

    public ProductManager(Locale locale) {
        this(locale.toLanguageTag());
    }

    public ProductManager(String languageTag) {
        changeLocale(languageTag);
    }

    public void changeLocale(String languageTag) {
        formatter = formatters.getOrDefault(languageTag, formatters.get("en-GB"));
    }

    public static Set<String> getSupportedLocales() {
        return formatters.keySet();
    }

    // this is factory method
    public Product createProduct(int id, String name, BigDecimal price, Rating rating, LocalDate bestBefore) {
        Product product = new Food(id, name, price, rating, bestBefore);
        products.putIfAbsent(product, new ArrayList<>());
        return product;
    }

    // this is factory method too
    public Product createProduct(int id, String name, BigDecimal price, Rating rating) {
        Product product = new Drink(id, name, price, rating);
        products.putIfAbsent(product, new ArrayList<>());
        return product;
    }

    public Product reviewProduct(Product product, Rating rating, String comments) {
        List<Review> reviews = products.get(product);

        reviews.add(new Review(rating, comments));

        products.remove(product);

        Rating avgRating = getAverageRating(reviews);
        Product newProduct = product.applyRating(avgRating);
        products.putIfAbsent(newProduct, reviews);
        return newProduct;
    }

    public Product reviewProduct(int id, Rating rating, String comments) {
        return reviewProduct(findProduct(id), rating, comments);
    }

    public void printProductReport(int id) {
        printProductReport(findProduct(id));
    }

    public void printProductReport(Product product) {
        StringBuilder txt = new StringBuilder();
        List<Review> reviews = products.get(product);
        Collections.sort(reviews);
        txt.append(formatter.formatProduct(product));

        txt.append('\n');

        if (reviews.isEmpty()) {
            txt.append(formatter.getText("no.reviews"));
            txt.append('\n');
        } else {
            String reviewsText = reviews
                    .stream()
                    .map(review -> formatter.formatReview(review) + '\n')
                    .collect(Collectors.joining());
            txt.append(reviewsText);
        }

        System.out.println(txt);
    }

    public void printProducts(Predicate<Product> filter, Comparator<Product> sorter) {
        StringBuilder txt = new StringBuilder();

        products.keySet()
                .stream()
                .sorted(sorter)
                .filter(filter)
                .forEach(product ->
                        txt.append(formatter.formatProduct(product) + '\n'));

        System.out.println(txt);
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

    public Product findProduct(int id) {
            return products.keySet()
                    .stream()
                    .filter(product -> product.getId() == id)
                    .findFirst()
                    .get(); // get throws NoSuchElementException for us
//                    .orElseGet(() -> null);
    }

    public Map<String, String> getDiscounts() {
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
