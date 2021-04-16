/*
 * Copyright (c) 2021. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package labs.pm.app;

import labs.pm.data.Product;

import java.math.BigDecimal;

public class Shop {
    /**
     * {@code Shop} class represents an application that manages Products
     * @param args
     * @author oracle
     */

    public static void main(String[] args) {
        Product p1 = new Product();
        p1.setId(101);
        p1.setName("Tea");
        p1.setPrice(BigDecimal.valueOf(1.99));
        System.out.println(p1.getId() + " " + p1.getName() + " " + p1.getPrice());
        System.out.println("Discount is " + p1.getDiscount() + " (" + p1.DISCOUNT_RATE + ")");
        p1.getEnum();
    }
}
