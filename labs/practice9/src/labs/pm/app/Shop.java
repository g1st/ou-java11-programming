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

        Product p1 = pm.createProduct(101, "Tea", BigDecimal.valueOf(1.99), NOT_RATED);
        pm.printProductReport(p1);
        p1 = pm.reviewProduct(p1, FOUR_STAR, "Nice hot cup of tea");
        p1 = pm.reviewProduct(p1, TWO_STAR, "Rather weak tea");
        p1 = pm.reviewProduct(p1, FOUR_STAR, "Fine tea");
        p1 = pm.reviewProduct(p1, FOUR_STAR, "Good tea");
        p1 = pm.reviewProduct(p1, FIVE_STAR, "Perfect tea");
        p1 = pm.reviewProduct(p1, THREE_STAR, "Just add some lemon");
        pm.printProductReport(p1);

        Product p2 = pm.createProduct(102, "Coffee", BigDecimal.valueOf(1.99), NOT_RATED);
        pm.reviewProduct(p2, THREE_STAR, "Coffee was ok");
        pm.reviewProduct(p2, ONE_STAR, "Where is the milk?!");
        pm.reviewProduct(p2, FIVE_STAR, "It's perfect with ten spoons of sugar!");
        pm.printProductReport(p2);

        Product p3 = pm.createProduct(103, "Cake", BigDecimal.valueOf(3.99), NOT_RATED, LocalDate.now().plusDays(2));
        pm.reviewProduct(p3, FIVE_STAR, "Very nice cake");
        pm.reviewProduct(p3, FOUR_STAR, "It's good, but I've expected more chocolate");
        pm.reviewProduct(p3, FIVE_STAR, "This cake is perfect!");
        pm.printProductReport(p3);

        Product p4 = pm.createProduct(104, "Cookie", BigDecimal.valueOf(2.99), NOT_RATED, LocalDate.now());
        pm.reviewProduct(p4, THREE_STAR, "Just another cookie");
        pm.reviewProduct(p4, THREE_STAR, "Ok");
        pm.printProductReport(p4);

        Product p5 = pm.createProduct(105, "Hot Chocolate", BigDecimal.valueOf(2.50), NOT_RATED);
        pm.reviewProduct(p5, FOUR_STAR, "Tasty");
        pm.reviewProduct(p5, FOUR_STAR, "Not bast at all");
        pm.printProductReport(p5);

        Product p6 = pm.createProduct(106, "Chocolate", BigDecimal.valueOf(2.50), NOT_RATED, LocalDate.now().plusDays(3));
        pm.reviewProduct(p6, TWO_STAR, "Too seet");
        pm.reviewProduct(p6, THREE_STAR, "Better than cookie");
        pm.reviewProduct(p6, TWO_STAR, "Too bitter");
        pm.reviewProduct(p6, ONE_STAR, "I don't get it!");
        pm.printProductReport(p6);
    }
}
