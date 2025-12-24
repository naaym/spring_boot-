package com.delta_nutritionMVC.delta.landing.services;

import com.delta_nutritionMVC.delta.landing.dtos.CheckoutForm;
import com.delta_nutritionMVC.delta.landing.models.Cart;
import com.delta_nutritionMVC.delta.landing.models.Order;
import com.delta_nutritionMVC.delta.landing.models.OrderItem;
import com.delta_nutritionMVC.delta.landing.models.OrderStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface OrderService {
    Order createOrderFromCart(Cart cart, CheckoutForm form, String clientEmail);
    List<Order> fetchLatestOrdersForClient(String clientEmail, int limit);
    List<Order> findAllForClient(String clientEmail);
    Order findById(Long id);
    BigDecimal calculateTotalSpentExcludingCancelled(String clientEmail);
    Optional<Order> updateStatus(Long orderId, OrderStatus status);
    List<Order> findAllOrders();
}
