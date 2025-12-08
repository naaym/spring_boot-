package com.delta_nutritionMVC.delta.admin.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {
    @GetMapping("/admins/dashboard")
    public String dashboard() {
        return "admin/dashboard";
    }
}
