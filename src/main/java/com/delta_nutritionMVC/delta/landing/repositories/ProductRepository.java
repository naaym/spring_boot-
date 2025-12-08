package com.delta_nutritionMVC.delta.landing.repositories;

import com.delta_nutritionMVC.delta.landing.models.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    List<Product> findAll();

    Optional<Product> findById(String id);
}
