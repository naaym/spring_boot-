package com.delta_nutritionMVC.delta.catalog.services;

import com.delta_nutritionMVC.delta.admin.models.Category;
import com.delta_nutritionMVC.delta.product.models.Product;

import java.math.BigDecimal;
import java.util.List;

public interface CatalogBrowsingService {
    CatalogBrowsingServiceImpl.PriceRange getPriceRangeForCategory(Long categoryId);
    List<Product> filterProducts(Long categoryId, BigDecimal minPrice, BigDecimal maxPrice);
    BigDecimal resolvePrice(String rawValue, BigDecimal defaultValue);
    List<Category> listCategories();
    Category getCategory(Long categoryId);
}
