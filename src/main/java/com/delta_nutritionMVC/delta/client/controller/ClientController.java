package com.delta_nutritionMVC.delta.client.controller;

import com.delta_nutritionMVC.delta.auth.dtos.SignInResponse;
import com.delta_nutritionMVC.delta.client.dtos.ClientSignUpRequest;
import com.delta_nutritionMVC.delta.client.dtos.ClientUpdateProfilRequest;
import com.delta_nutritionMVC.delta.client.services.ClientService;
import com.delta_nutritionMVC.delta.order.models.Order;
import com.delta_nutritionMVC.delta.checkout.services.CheckoutService;
import com.delta_nutritionMVC.delta.order.services.OrderService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;
    private final CheckoutService checkoutServiceImpl;
    private final OrderService orderServiceImpl;

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

        return "redirect:/auth/login";
    }

    @GetMapping("/clients/dashboard")
    public String dashboard(HttpSession session, Model model) {
        SignInResponse client = (SignInResponse) session.getAttribute("clientSession");

        Order lastOrderFromSession = checkoutServiceImpl.loadLastOrderFromSession(session);
        List<Order> recentOrders = client != null
                ? orderServiceImpl.fetchLatestOrdersForClient(client.getEmail(), 2)
                : List.of();
        List<Order> allOrders = client != null
                ? orderServiceImpl.findAllForClient(client.getEmail())
                : List.of();

        BigDecimal totalSpent = BigDecimal.ZERO;
        if (client != null) {
            totalSpent = orderServiceImpl.calculateTotalSpentExcludingCancelled(client.getEmail());
        } else if (lastOrderFromSession != null && !lastOrderFromSession.isCancelled()) {
            totalSpent = lastOrderFromSession.getTotal();
        }

        if (client == null && lastOrderFromSession == null && recentOrders.isEmpty()) {
            return "redirect:/auth/login";
        }

        String displayName = client != null
                ? client.getFullName()
                : (lastOrderFromSession != null ? lastOrderFromSession.getFullName() : "Client");

        model.addAttribute("name", displayName);
        model.addAttribute("recentOrders", recentOrders);
        model.addAttribute("lastOrder", recentOrders.isEmpty() ? lastOrderFromSession : recentOrders.get(0));
        model.addAttribute("orders", allOrders);
        model.addAttribute("totalSpent", totalSpent);

        return "clients/dashboard";
    }

    @GetMapping("/clients/orders")
    public String listOrders(HttpSession session, Model model) {
        SignInResponse client = (SignInResponse) session.getAttribute("clientSession");

        Order lastOrderFromSession = checkoutServiceImpl.loadLastOrderFromSession(session);
        List<Order> allOrders = client != null
                ? orderServiceImpl.findAllForClient(client.getEmail())
                : (lastOrderFromSession != null ? List.of(lastOrderFromSession) : List.of());

        if (client == null && lastOrderFromSession == null && allOrders.isEmpty()) {
            return "redirect:/auth/login";
        }

        String displayName = client != null
                ? client.getFullName()
                : (lastOrderFromSession != null ? lastOrderFromSession.getFullName() : "Client");

        model.addAttribute("name", displayName);
        model.addAttribute("orders", allOrders);

        return "clients/orders";
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
