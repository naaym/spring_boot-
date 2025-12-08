package com.delta_nutritionMVC.delta.landing.repositories;

import com.delta_nutritionMVC.delta.landing.models.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
