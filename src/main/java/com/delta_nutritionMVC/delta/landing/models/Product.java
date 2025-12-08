package com.delta_nutritionMVC.delta.landing.models;

import java.math.BigDecimal;

public class Product {
    private final String id;
    private final String name;
    private final String description;
    private final BigDecimal price;
    private final BigDecimal originalPrice;

    public Product(String id, String name, String description, BigDecimal price, BigDecimal originalPrice) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.originalPrice = originalPrice;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }
}
