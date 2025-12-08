package com.delta_nutritionMVC.delta.landing.models;

import java.math.BigDecimal;

public class CartItem {
    private final Product product;
    private int quantity;

    public CartItem(Product product) {
        this.product = product;
        this.quantity = 1;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void incrementQuantity() {
        this.quantity++;
    }

    public BigDecimal getTotalPrice() {
        return product.getPrice().multiply(BigDecimal.valueOf(quantity));
    }
}
