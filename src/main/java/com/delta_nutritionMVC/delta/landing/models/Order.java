package com.delta_nutritionMVC.delta.landing.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String address;

    @Column(name = "client_email")
    private String clientEmail;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.PAS_ENCORE_LIVREE;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<OrderItem> items = new ArrayList<>();



























    protected Order() {

    }

    public Order(String fullName, String phone, String address, String clientEmail) {
        this.fullName = fullName;
        this.phone = phone;
        this.address = address;
        this.clientEmail = clientEmail;
        this.status = OrderStatus.PAS_ENCORE_LIVREE;
    }

    @PrePersist
    protected void onCreate() {
        if (this.status == null) {
            this.status = OrderStatus.PAS_ENCORE_LIVREE;
        }
        this.createdAt = LocalDateTime.now();
    }

    public void addItem(Product product, int quantity) {
        OrderItem item = new OrderItem(this, product, quantity, product.getPrice());
        items.add(item);
    }


    public boolean isCancelled() {
        return OrderStatus.ANNULEE.equals(this.status);
    }
}
