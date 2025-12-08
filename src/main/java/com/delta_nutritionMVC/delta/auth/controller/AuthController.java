package com.delta_nutritionMVC.delta.auth.controller;

import com.delta_nutritionMVC.delta.auth.dtos.SignInRequest;
import com.delta_nutritionMVC.delta.auth.dtos.SignInResponse;
import com.delta_nutritionMVC.delta.auth.services.AuthService;
import jakarta.servlet.http.HttpSession;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class AuthController {

    
    private final AuthService authService;

    @GetMapping("/auth/login")
    public String loginForm(Model model) {
        model.addAttribute("login", new SignInRequest());
        return "auth/login";
    }

    @PostMapping("/auth/login")
    public String processLogin(
             @ModelAttribute("login") SignInRequest loginRequest,
            HttpSession session,
            Model model
    ) {

        try {
            SignInResponse client = authService.signIn(
                   loginRequest
            );

            session.setAttribute("clientSession", client);

            if ("ADMIN".equalsIgnoreCase(client.getRole())) {
                return "redirect:/admins/dashboard";
            }

            return "redirect:/clients/dashboard";

        } catch (RuntimeException ex) {
            model.addAttribute("error", ex.getMessage());
            return "auth/login";
        }
    }

    @GetMapping("/auth/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/auth/login";
    }
}
