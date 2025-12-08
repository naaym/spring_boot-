package com.delta_nutritionMVC.delta.landing;

import com.delta_nutritionMVC.delta.landing.dtos.CheckoutForm;
import com.delta_nutritionMVC.delta.landing.models.CartItem;
import com.delta_nutritionMVC.delta.landing.services.CartService;
import com.delta_nutritionMVC.delta.landing.services.CheckoutService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
@RequestMapping("/home")
@RequiredArgsConstructor
public class LandingController {

    private final CartService cartService;
    private final CheckoutService checkoutService;

    @GetMapping
    public String landing(Model model, HttpSession session) {
        Map<String, CartItem> cart = cartService.getOrCreateCart(session);
        model.addAttribute("products", cartService.listProducts());
        model.addAttribute("cart", cart);
        model.addAttribute("cartTotal", cartService.calculateTotal(cart));
        return "landing/landing";
    }

    @PostMapping("/add-to-cart")
    public String addToCart(@RequestParam("productId") String productId, HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            cartService.addProductToCart(productId, session);
            redirectAttributes.addFlashAttribute("message", "Produit ajouté au panier.");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/home";
    }

    @GetMapping("/checkout")
    public String checkout(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        Map<String, CartItem> cart = cartService.getOrCreateCart(session);
        if (cart.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Ajoutez au moins un produit avant de passer au paiement.");
            return "redirect:/home";
        }

        if (!model.containsAttribute("orderForm")) {
            model.addAttribute("orderForm", new CheckoutForm());
        }

        model.addAttribute("cart", cart);
        model.addAttribute("cartTotal", cartService.calculateTotal(cart));
        return "landing/checkout";
    }

    @PostMapping("/checkout")
    public String submitCheckout(CheckoutForm form, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        Map<String, CartItem> cart = cartService.getOrCreateCart(session);
        if (cart.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Votre panier est vide.");
            return "redirect:/home";
        }

        if (!checkoutService.hasValidContactDetails(form)) {
            model.addAttribute("cart", cart);
            model.addAttribute("cartTotal", cartService.calculateTotal(cart));
            model.addAttribute("error", "Merci de renseigner vos coordonnées pour finaliser la commande.");
            return "landing/checkout";
        }

        checkoutService.prepareOrder(form, session);
        redirectAttributes.addFlashAttribute("orderSuccess", "Votre commande a été transmise. Merci !");
        return "redirect:/clients/dashboard";
    }
}
