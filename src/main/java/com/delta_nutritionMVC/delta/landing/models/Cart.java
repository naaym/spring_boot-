package com.delta_nutritionMVC.delta.landing.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<CartItem> items = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void addOrIncrementItem(Product product) {
        Optional<CartItem> existingItem = items.stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().incrementQuantity();
            return;
        }

        CartItem cartItem = new CartItem(this, product, 1);
        items.add(cartItem);
    }

    public void updateItemQuantity(Long cartItemId, int quantity) {
        CartItem item = items.stream()
                .filter(cartItem -> cartItem.getId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Article introuvable dans le panier"));

        if (quantity <= 0) {
            items.remove(item);
            return;
        }

        item.setQuantity(quantity);
    }

    public void removeItem(Long cartItemId) {
        CartItem item = items.stream()
                .filter(cartItem -> cartItem.getId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Article introuvable dans le panier"));

        items.remove(item);
    }

    public BigDecimal getTotal() {
        return items.stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
