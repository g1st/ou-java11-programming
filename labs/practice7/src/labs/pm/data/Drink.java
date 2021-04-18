/*
 * Copyright (c) 2021. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package labs.pm.data;

import labs.pm.app.Shop;

import java.math.BigDecimal;
import java.time.LocalTime;

public final class Drink extends Product {
    Drink(int id, String name, BigDecimal price, Shop.Rating rating) {
        super(id, name, price, rating);
    }

    @Override
    public BigDecimal getDiscount() {
        LocalTime now = LocalTime.now();
        return (now.isAfter(LocalTime.of(16, 30)) && now.isBefore(LocalTime.of(17, 30))) ? super.getDiscount() : BigDecimal.ZERO;
    }

    @Override
    public Drink applyRating(Shop.Rating newRating) {
        return new Drink(getId(), getName(), getPrice(), newRating);
    }
}
