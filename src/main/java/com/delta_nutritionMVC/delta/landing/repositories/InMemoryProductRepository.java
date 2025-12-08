package com.delta_nutritionMVC.delta.landing.repositories;

import com.delta_nutritionMVC.delta.landing.models.Product;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public class InMemoryProductRepository implements ProductRepository {

    private final List<Product> products = List.of(
            new Product("whey-isolate", "Whey Isolate 1kg", "Protéine premium pour la récupération.", new BigDecimal("99.00"), new BigDecimal("129.00")),
            new Product("creatine", "Créatine Monohydrate 300g", "Force et explosivité garanties.", new BigDecimal("49.00"), null),
            new Product("fitness-bundle", "Pack Performance", "Shaker + gants + barre protéinée.", new BigDecimal("69.00"), new BigDecimal("85.00"))
    );

    @Override
    public List<Product> findAll() {
        return products;
    }

    @Override
    public Optional<Product> findById(String id) {
        return products.stream().filter(product -> product.getId().equals(id)).findFirst();
    }
}
