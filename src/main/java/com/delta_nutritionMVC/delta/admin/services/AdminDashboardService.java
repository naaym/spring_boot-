package com.delta_nutritionMVC.delta.admin.services;

import com.delta_nutritionMVC.delta.order.models.Order;
import com.delta_nutritionMVC.delta.order.repositories.OrderRepository;
import com.delta_nutritionMVC.delta.product.repositories.ProductRepository;
import com.delta_nutritionMVC.delta.client.repositories.ClientRepository;
import com.delta_nutritionMVC.delta.admin.repositories.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminDashboardService {

    public record AdminStats(long products, long categories, long orders, long clients) {
    }

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;

    public AdminDashboardService(ProductRepository productRepository,
                                 CategoryRepository categoryRepository,
                                 OrderRepository orderRepository,
                                 ClientRepository clientRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.orderRepository = orderRepository;
        this.clientRepository = clientRepository;
    }

    public AdminStats fetchStats() {
        return new AdminStats(
                productRepository.count(),
                categoryRepository.count(),
                orderRepository.count(),
                clientRepository.count()
        );
    }

    public List<Order> fetchRecentOrders(int limit) {
        return orderRepository.findTop5ByOrderByCreatedAtDesc()
                .stream()
                .limit(limit)
                .toList();
    }
}
