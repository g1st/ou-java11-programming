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
import java.time.LocalDate;

public final class Food extends Product {
    private LocalDate bestBefore;

    Food(int id, String name, BigDecimal price, Shop.Rating rating, LocalDate bestBefore) {
        super(id, name, price, rating);
        this.bestBefore = bestBefore;
    }

    @Override
    public LocalDate getBestBefore() {
        return bestBefore;
    }

    @Override
    public BigDecimal getDiscount() {
        return bestBefore.isEqual(LocalDate.now()) ? super.getDiscount() : BigDecimal.ZERO;
    }

    @Override
    public Food applyRating(Shop.Rating newRating) {
        return new Food(getId(), getName(), getPrice(), newRating, getBestBefore());
    }
}
