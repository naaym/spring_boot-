package com.delta_nutritionMVC.delta.client.controller;

import com.delta_nutritionMVC.delta.client.dtos.ClientSignUpRequest;
import com.delta_nutritionMVC.delta.client.services.ClientService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
@Controller
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @GetMapping("/clients/signup")
    public String showSignupForm(Model model) {
        model.addAttribute("client", new ClientSignUpRequest());
        return "clients/signup";
    }

    @PostMapping("/clients/signup")
    public String submitSignup(
            @Valid @ModelAttribute("client") ClientSignUpRequest form,
            Model model
    ) {

        try {
            clientService.signUpClient(form);
        } catch (RuntimeException ex) {
            model.addAttribute("error", ex.getMessage());
            return "clients/signup";
        }

        return "auth/login";
    }
    @GetMapping("/clients/dashboard")
    public String dashboard(HttpSession session, Model model) {

        Object client = session.getAttribute("clientSession");

        if (client == null) {
            return "redirect:/auth/login";
        }

        model.addAttribute("name",client );

        return "clients/dashboard";
    }
}

