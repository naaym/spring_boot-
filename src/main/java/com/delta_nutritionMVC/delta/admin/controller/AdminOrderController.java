package com.delta_nutritionMVC.delta.admin.controller;

import com.delta_nutritionMVC.delta.order.models.OrderStatus;
import com.delta_nutritionMVC.delta.order.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admins/orders")
public class AdminOrderController {

    private final OrderService orderServiceImpl;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("orders", orderServiceImpl.findAllOrders());
        model.addAttribute("statuses", OrderStatus.values());
        return "admin/orders";
    }

    @PostMapping("/{orderId}/status")
    public String updateStatus(@PathVariable Long orderId, @RequestParam("status") OrderStatus status) {
        orderServiceImpl.updateStatus(orderId, status);
        return "redirect:/admins/orders";
    }

    @PostMapping("/notifications/{notificationId}/read")
    public String markAsRead(@PathVariable Long notificationId) {
        return "redirect:/admins/orders";
    }
}
