package com.delta_nutritionMVC.delta.cart.services;

import com.delta_nutritionMVC.delta.cart.models.Cart;
import com.delta_nutritionMVC.delta.product.models.Product;

import java.math.BigDecimal;
import java.util.List;
import jakarta.servlet.http.HttpSession;

public interface CarteService {
    Cart getOrCreateCart(HttpSession session);
    void clearCart(HttpSession session);
    void removeItem(Long cartItemId, HttpSession session);
    void updateItemQuantity(Long cartItemId, int quantity, HttpSession session);
    int calculateItemsCount(Cart cart);
    void addProductToCart(String productId, HttpSession session);
    List<Product> listProducts();
    BigDecimal calculateTotal(Cart cart);
}
