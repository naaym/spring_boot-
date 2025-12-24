package com.delta_nutritionMVC.delta.landing.services;

import com.delta_nutritionMVC.delta.landing.models.Cart;
import jakarta.servlet.http.HttpSession;

public interface CarteService {
    Cart getOrCreateCart(HttpSession session);
    void clearCart(HttpSession session);
    void removeItem(Long cartItemId, HttpSession session);
    void updateItemQuantity(Long cartItemId, int quantity, HttpSession session);
    int calculateItemsCount(Cart cart);
    void addProductToCart(String productId, HttpSession session);
}
