package com.delta_nutritionMVC.delta.landing.services;

import com.delta_nutritionMVC.delta.admin.services.NotificationService;
import com.delta_nutritionMVC.delta.landing.dtos.CheckoutForm;
import com.delta_nutritionMVC.delta.landing.models.Cart;
import com.delta_nutritionMVC.delta.landing.models.CartItem;
import com.delta_nutritionMVC.delta.landing.models.Order;
import com.delta_nutritionMVC.delta.landing.models.OrderItem;
import com.delta_nutritionMVC.delta.landing.models.OrderStatus;
import com.delta_nutritionMVC.delta.landing.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final NotificationService notificationService;

    @Transactional
    @Override
    public Order createOrderFromCart(Cart cart, CheckoutForm form, String clientEmail) {
        if (cart.getItems().isEmpty()) {
            throw new IllegalStateException("Le panier est vide");
        }

        Order order = new Order(form.getFullName(), form.getPhone(), form.getAddress(), clientEmail);

        for (CartItem item : cart.getItems()) {
            order.addItem(item.getProduct(), item.getQuantity());
        }

        order.setTotal(calculateTotal(order.getItems()));
        Order savedOrder = orderRepository.save(order);
        notificationService.notifyNewOrder(savedOrder);
        return savedOrder;
    }
    @Override
    public List<Order> fetchLatestOrdersForClient(String clientEmail, int limit) {
        if (limit <= 0 || clientEmail == null) {
            return List.of();
        }
        List<Order> latestTwo = orderRepository.findTop2ByClientEmailOrderByCreatedAtDesc(clientEmail);
        return latestTwo.stream().limit(limit).toList();
    }
    @Override
    public List<Order> findAllForClient(String clientEmail) {
        if (clientEmail == null) {
            return List.of();
        }
        return orderRepository.findAllByClientEmailOrderByCreatedAtDesc(clientEmail);
    }
    @Override
    public Order findById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }
    @Override
    public BigDecimal calculateTotalSpentExcludingCancelled(String clientEmail) {
        if (clientEmail == null) {
            return BigDecimal.ZERO;
        }

        return orderRepository.findAllByClientEmailAndStatusNotOrderByCreatedAtDesc(clientEmail, OrderStatus.ANNULEE)
                .stream()
                .map(Order::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Transactional
    @Override
    public Optional<Order> updateStatus(Long orderId, OrderStatus status) {
        if (status == null) {
            return Optional.empty();
        }

        return orderRepository.findById(orderId)
                .map(order -> {
                    order.setStatus(status);
                    notificationService.markOrderNotificationsAsRead(orderId);
                    return orderRepository.save(order);
                });
    }
    @Override
    public List<Order> findAllOrders() {
        return orderRepository.findAllOrderedByCreatedDateDesc();
    }

    private BigDecimal calculateTotal(List<OrderItem> items) {
        return items.stream()
                .map(OrderItem::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
