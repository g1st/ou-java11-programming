/*
 * Copyright (c) 2021. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package labs.pm.data;

import java.math.BigDecimal;
import java.time.LocalDate;

// this is factory class
public class ProductManager {

    // this is factory method
    public Product createProduct(int id, String name, BigDecimal price, Rating rating, LocalDate bestBefore) {
        return new Food(id, name, price, rating, bestBefore);
    }
    // this is factory method too
    public Product createProduct(int id, String name, BigDecimal price, Rating rating) {
        return new Drink(id, name, price, rating);
    }
}
