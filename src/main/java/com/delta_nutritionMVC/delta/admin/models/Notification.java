package com.delta_nutritionMVC.delta.admin.models;

import com.delta_nutritionMVC.delta.landing.models.Order;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private boolean readFlag = false;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    protected Notification() {
        // JPA
    }

    public Notification(Order order, String message) {
        this.order = order;
        this.message = message;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Order getOrder() {
        return order;
    }

    public String getMessage() {
        return message;
    }

    public boolean isReadFlag() {
        return readFlag;
    }

    public void markAsRead() {
        this.readFlag = true;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
