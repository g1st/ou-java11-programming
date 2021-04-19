/*
 * Copyright (c) 2021. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package labs.pm.data;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;

// this is factory class
public class ProductManager {
    //    private Product product;
//    private Review[] reviews = new Review[5];
    private Map<Product, List<Review>> products = new HashMap<>();
    private Locale locale;
    private ResourceBundle resources;
    private DateTimeFormatter dateFormat;
    private NumberFormat moneyFormat;

    public ProductManager(Locale locale) {
        this.locale = locale;
        resources = ResourceBundle.getBundle("labs.pm.data.resources", locale);
        dateFormat = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).localizedBy(locale);
        moneyFormat = NumberFormat.getCurrencyInstance(locale);
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

        txt.append(MessageFormat.format(resources.getString("product"),
                product.getName(),
                moneyFormat.format(product.getPrice()),
                product.getRating().getStars(),
                dateFormat.format(product.getBestBefore())));

        txt.append('\n');

        Collections.sort(reviews);

        for (Review review : reviews) {
            txt.append(MessageFormat.format(
                    resources.getString("review"),
                    review.getRating().getStars(),
                    review.getComments()
            ));
            txt.append('\n');
        }

        if (products.get(product).isEmpty()) {
            txt.append(resources.getString("no.reviews"));
            txt.append('\n');
        }

        System.out.println(txt);
    }

    public Rating getAverageRating(List<Review> reviews) {
        int stars = 0;
        for (Review review : reviews) {
            if (review == null) break;
            stars += review.getRating().ordinal();
        }

        float averageStars = (float) stars / reviews.size();
        int averageNumOfStars = (int) Math.round(averageStars);

        return Rateable.convert(averageNumOfStars);
    }

    public Product findProduct(int id) {
        for (Product p : products.keySet()) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

}
