package com.delta_nutritionMVC.delta.landing.services;

import com.delta_nutritionMVC.delta.landing.dtos.CheckoutForm;
import com.delta_nutritionMVC.delta.landing.models.Cart;
import com.delta_nutritionMVC.delta.landing.models.CartItem;
import com.delta_nutritionMVC.delta.landing.models.Order;
import com.delta_nutritionMVC.delta.landing.models.OrderItem;
import com.delta_nutritionMVC.delta.landing.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    @Transactional
    public Order createOrderFromCart(Cart cart, CheckoutForm form) {
        if (cart.getItems().isEmpty()) {
            throw new IllegalStateException("Le panier est vide");
        }

        Order order = new Order(form.getFullName(), form.getPhone(), form.getAddress());

        for (CartItem item : cart.getItems()) {
            order.addItem(item.getProduct(), item.getQuantity());
        }

        order.setTotal(calculateTotal(order.getItems()));
        return orderRepository.save(order);
    }

    public List<Order> fetchLatestOrders(int limit) {
        if (limit <= 0) {
            return List.of();
        }
        List<Order> latestTwo = orderRepository.findTop2ByOrderByCreatedAtDesc();
        return latestTwo.stream().limit(limit).toList();
    }

    public List<Order> findAllOrdered() {
        return orderRepository.findAllByOrderByCreatedAtDesc();
    }

    public Order findById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    private BigDecimal calculateTotal(List<OrderItem> items) {
        return items.stream()
                .map(OrderItem::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
