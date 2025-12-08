package com.delta_nutritionMVC.delta.client.controller;

import com.delta_nutritionMVC.delta.auth.dtos.SignInResponse;
import com.delta_nutritionMVC.delta.client.dtos.ClientSignUpRequest;
import com.delta_nutritionMVC.delta.client.dtos.ClientUpdateProfilRequest;
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

        SignInResponse client = (SignInResponse) session.getAttribute("clientSession");

        if (client == null) {
            return "redirect:/auth/login";
        }

        model.addAttribute("name", client.getFullName());

        return "clients/dashboard";
    }

    @GetMapping("/clients/profile")
    public String showProfile(HttpSession session, Model model) {
        SignInResponse clientSession = (SignInResponse) session.getAttribute("clientSession");

        if (clientSession == null) {
            return "redirect:/auth/login";
        }

        var clientInfo = clientService.getClientByEmail(clientSession.getEmail());

        if (clientInfo == null) {
            return "redirect:/auth/login";
        }

        ClientUpdateProfilRequest form = new ClientUpdateProfilRequest();
        form.setFullName(clientInfo.getFullName());
        form.setPhone(clientInfo.getPhone());
        form.setAddressLiv(clientInfo.getAddressLiv());
        form.setCityName(clientInfo.getCityName());

        model.addAttribute("client", clientSession);
        model.addAttribute("profileForm", form);

        return "clients/profile";
    }

    @PostMapping("/clients/profile")
    public String updateProfile(
            @ModelAttribute("profileForm") ClientUpdateProfilRequest request,
            HttpSession session,
            Model model
    ) {
        SignInResponse clientSession = (SignInResponse) session.getAttribute("clientSession");

        if (clientSession == null) {
            return "redirect:/auth/login";
        }

        try {
            var updatedClient = clientService.updateClientProfile(clientSession.getEmail(), request);

            clientSession.setFullName(updatedClient.getFullName());
            session.setAttribute("clientSession", clientSession);

            model.addAttribute("success", "Profil mis à jour avec succès");
            model.addAttribute("client", clientSession);
            model.addAttribute("profileForm", request);
            model.addAttribute("updatedInfo", updatedClient);

            return "clients/profile";
        } catch (RuntimeException ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("client", clientSession);
            model.addAttribute("profileForm", request);
            return "clients/profile";
        }
    }
}

