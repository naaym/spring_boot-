package com.delta_nutritionMVC.delta.admin.controller;


import com.delta_nutritionMVC.delta.admin.services.AdminDashboardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    private final AdminDashboardService dashboardService;

    public AdminController(AdminDashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/admins/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("stats", dashboardService.fetchStats());
        model.addAttribute("recentOrders", dashboardService.fetchRecentOrders(5));
        return "admin/dashboard";
    }

}
