/*
 * Copyright (c) 2021. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package labs.pm.app;

import labs.pm.data.Product;
import labs.pm.data.ProductManager;
import static labs.pm.data.Rating.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Locale;

public class Shop {
    /**
     * {@code Shop} class represents an application that manages Products
     *
     * @param args
     * @author oracle
     */

    public static void main(String[] args) {
//        ProductManager pm = new ProductManager(Locale.US);
        ProductManager pm = new ProductManager(Locale.UK);

        pm.createProduct(101, "Tea", BigDecimal.valueOf(1.99), NOT_RATED);
        pm.printProductReport(101);
        pm.reviewProduct(101, FOUR_STAR, "Nice hot cup of tea");
        pm.reviewProduct(101, TWO_STAR, "Rather weak tea");
        pm.reviewProduct(101, FOUR_STAR, "Fine tea");
        pm.reviewProduct(101, FOUR_STAR, "Good tea");
        pm.reviewProduct(101, FIVE_STAR, "Perfect tea");
        pm.reviewProduct(101, THREE_STAR, "Just add some lemon");
        pm.printProductReport(101);

        pm.createProduct(102, "Coffee", BigDecimal.valueOf(1.99), NOT_RATED);
        pm.reviewProduct(102, THREE_STAR, "Coffee was ok");
        pm.reviewProduct(102, ONE_STAR, "Where is the milk?!");
        pm.reviewProduct(102, FIVE_STAR, "It's perfect with ten spoons of sugar!");
        pm.printProductReport(102);

        pm.createProduct(103, "Cake", BigDecimal.valueOf(3.99), NOT_RATED, LocalDate.now().plusDays(2));
        pm.reviewProduct(103, FIVE_STAR, "Very nice cake");
        pm.reviewProduct(103, FOUR_STAR, "It's good, but I've expected more chocolate");
        pm.reviewProduct(103, FIVE_STAR, "This cake is perfect!");
        pm.printProductReport(103);

        pm.createProduct(104, "Cookie", BigDecimal.valueOf(2.99), NOT_RATED, LocalDate.now());
        pm.reviewProduct(104, THREE_STAR, "Just another cookie");
        pm.reviewProduct(104, THREE_STAR, "Ok");
        pm.printProductReport(104);

        pm.createProduct(105, "Hot Chocolate", BigDecimal.valueOf(2.50), NOT_RATED);
        pm.reviewProduct(105, FOUR_STAR, "Tasty");
        pm.reviewProduct(105, FOUR_STAR, "Not bast at all");
        pm.printProductReport(105);

        pm.createProduct(106, "Chocolate", BigDecimal.valueOf(2.50), NOT_RATED, LocalDate.now().plusDays(3));
        pm.reviewProduct(106, TWO_STAR, "Too seet");
        pm.reviewProduct(106, THREE_STAR, "Better than cookie");
        pm.reviewProduct(106, TWO_STAR, "Too bitter");
        pm.reviewProduct(106, ONE_STAR, "I don't get it!");
        pm.printProductReport(106);
    }
}
