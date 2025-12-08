package com.delta_nutritionMVC.delta.landing.repositories;

import com.delta_nutritionMVC.delta.landing.models.Order;
import com.delta_nutritionMVC.delta.landing.models.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findTop2ByClientEmailOrderByCreatedAtDesc(String clientEmail);
    List<Order> findAllByClientEmailOrderByCreatedAtDesc(String clientEmail);
    List<Order> findAllByClientEmailAndStatusNotOrderByCreatedAtDesc(String clientEmail, OrderStatus status);
    List<Order> findTop5ByOrderByCreatedAtDesc();

    default List<Order> findAllOrderedByCreatedDateDesc() {
        return findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }
}
