package com.delta_nutritionMVC.delta.cart.repositories;

import com.delta_nutritionMVC.delta.cart.models.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
