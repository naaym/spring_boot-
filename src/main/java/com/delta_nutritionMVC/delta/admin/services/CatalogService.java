package com.delta_nutritionMVC.delta.admin.services;

import com.delta_nutritionMVC.delta.admin.models.Category;
import com.delta_nutritionMVC.delta.admin.repositories.CategoryRepository;
import com.delta_nutritionMVC.delta.admin.web.CategoryForm;
import com.delta_nutritionMVC.delta.admin.web.ProductForm;
import com.delta_nutritionMVC.delta.landing.models.Product;
import com.delta_nutritionMVC.delta.landing.repositories.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
public class CatalogService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public CatalogService(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    public List<Category> findAllCategories() {
        return categoryRepository.findAll();
    }

    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    @Transactional
    public Category createCategory(CategoryForm form) {
        Category category = categoryRepository.findByNameIgnoreCase(form.getName())
                .orElseGet(() -> new Category(form.getName(), form.getImageUrl()));
        category.setImageUrl(form.getImageUrl());
        category.setName(form.getName());
        return categoryRepository.save(category);
    }

    @Transactional
    public Product createProduct(ProductForm form) {
        Category category = null;
        if (form.getCategoryId() != null) {
            category = categoryRepository.findById(form.getCategoryId())
                    .orElse(null);
        }

        Product product = new Product();
        product.setId(generateSlug(form.getName()));
        product.setName(form.getName());
        product.setDescription(form.getDescription());
        product.setPrice(form.getPrice());
        product.setOriginalPrice(form.getOriginalPrice());
        product.setImageUrl(form.getImageUrl());
        product.setCategory(category);

        return productRepository.save(product);
    }

    private String generateSlug(String name) {
        if (name == null || name.isBlank()) {
            return UUID.randomUUID().toString();
        }
        String normalized = Normalizer.normalize(name, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        String slug = normalized.toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-|-$)", "");
        if (slug.isBlank()) {
            slug = UUID.randomUUID().toString();
        }
        return slug;
    }
}
