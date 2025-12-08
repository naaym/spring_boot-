package com.delta_nutritionMVC.delta.landing.services;

import com.delta_nutritionMVC.delta.landing.models.CartItem;
import com.delta_nutritionMVC.delta.landing.models.Product;
import com.delta_nutritionMVC.delta.landing.repositories.ProductRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {
    private static final String CART_ATTRIBUTE = "cart";

    private final ProductRepository productRepository;

    public List<Product> listProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> findProduct(String productId) {
        return productRepository.findById(productId);
    }

    public Map<String, CartItem> getOrCreateCart(HttpSession session) {
        Map<String, CartItem> cart = (Map<String, CartItem>) session.getAttribute(CART_ATTRIBUTE);
        if (cart == null) {
            cart = new HashMap<>();
            session.setAttribute(CART_ATTRIBUTE, cart);
        }
        return cart;
    }

    public void addProductToCart(String productId, HttpSession session) {
        Map<String, CartItem> cart = getOrCreateCart(session);
        Product product = findProduct(productId).orElseThrow(() -> new IllegalArgumentException("Produit introuvable"));

        cart.compute(productId, (id, existing) -> {
            if (existing == null) {
                return new CartItem(product);
            }
            existing.incrementQuantity();
            return existing;
        });

        session.setAttribute(CART_ATTRIBUTE, cart);
    }

    public BigDecimal calculateTotal(Map<String, CartItem> cart) {
        return cart.values().stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void clearCart(HttpSession session) {
        session.removeAttribute(CART_ATTRIBUTE);
    }
}
