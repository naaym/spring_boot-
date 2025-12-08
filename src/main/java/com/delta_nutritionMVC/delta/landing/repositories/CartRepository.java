package com.delta_nutritionMVC.delta.landing.repositories;

import com.delta_nutritionMVC.delta.landing.models.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
