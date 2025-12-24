package com.delta_nutritionMVC.delta.landing.services;

import com.delta_nutritionMVC.delta.admin.models.Category;
import com.delta_nutritionMVC.delta.admin.repositories.CategoryRepository;
import com.delta_nutritionMVC.delta.landing.models.Product;
import com.delta_nutritionMVC.delta.landing.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CatalogBrowsingServiceImpl implements  CatalogBrowsingService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public List<Category> listCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategory(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Catégorie introuvable"));
    }
@Override
    public PriceRange getPriceRangeForCategory(Long categoryId) {
        List<Product> products = productRepository.findByCategoryId(categoryId);

        if (products.isEmpty()) {
            return new PriceRange(BigDecimal.ZERO, BigDecimal.ZERO, false);
        }

        BigDecimal min = products.stream()
                .map(Product::getPrice)
                .min(Comparator.naturalOrder())
                .orElse(BigDecimal.ZERO);
        BigDecimal max = products.stream()
                .map(Product::getPrice)
                .max(Comparator.naturalOrder())
                .orElse(min);

        return new PriceRange(min, max, true);
    }
    @Override
    public List<Product> filterProducts(Long categoryId, BigDecimal minPrice, BigDecimal maxPrice) {
        if (minPrice != null && maxPrice != null) {
            return productRepository.findByCategoryIdAndPriceBetween(categoryId, minPrice, maxPrice);
        }
        return productRepository.findByCategoryId(categoryId);
    }
    @Override
    public BigDecimal resolvePrice(String rawValue, BigDecimal defaultValue) {
        try {
            if (rawValue == null || rawValue.isBlank()) {
                return defaultValue;
            }
            return new BigDecimal(rawValue);
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }

    public record PriceRange(BigDecimal min, BigDecimal max, boolean hasProducts) {
    }
}
