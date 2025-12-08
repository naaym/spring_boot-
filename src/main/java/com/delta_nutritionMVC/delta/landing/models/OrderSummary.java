package com.delta_nutritionMVC.delta.landing.models;

import java.math.BigDecimal;
import java.util.List;

public class OrderSummary {
    private final List<CartItem> items;
    private final String fullName;
    private final String phone;
    private final String address;
    private final BigDecimal total;

    public OrderSummary(List<CartItem> items, String fullName, String phone, String address, BigDecimal total) {
        this.items = items;
        this.fullName = fullName;
        this.phone = phone;
        this.address = address;
        this.total = total;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public BigDecimal getTotal() {
        return total;
    }
}
