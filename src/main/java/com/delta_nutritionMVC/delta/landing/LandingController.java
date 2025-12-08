package com.delta_nutritionMVC.delta.landing;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/home")
public class LandingController {

    private static final List<Product> PRODUCTS = List.of(
            new Product("whey-isolate", "Whey Isolate 1kg", "Protéine premium pour la récupération.", new BigDecimal("99.00"), new BigDecimal("129.00")),
            new Product("creatine", "Créatine Monohydrate 300g", "Force et explosivité garanties.", new BigDecimal("49.00"), null),
            new Product("fitness-bundle", "Pack Performance", "Shaker + gants + barre protéinée.", new BigDecimal("69.00"), new BigDecimal("85.00"))
    );

    @GetMapping
    public String landing(Model model, HttpSession session) {
        Map<String, CartItem> cart = getCart(session);
        model.addAttribute("products", PRODUCTS);
        model.addAttribute("cart", cart);
        model.addAttribute("cartTotal", calculateTotal(cart));
        return "landing/landing";
    }

    @PostMapping("/add-to-cart")
    public String addToCart(@RequestParam("productId") String productId, HttpSession session, RedirectAttributes redirectAttributes) {
        Map<String, CartItem> cart = getCart(session);
        Optional<Product> product = findProduct(productId);

        if (product.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Produit introuvable.");
            return "redirect:/home";
        }

        cart.compute(productId, (id, existing) -> {
            if (existing == null) {
                return new CartItem(product.get());
            }
            existing.incrementQuantity();
            return existing;
        });

        session.setAttribute("cart", cart);
        redirectAttributes.addFlashAttribute("message", "Produit ajouté au panier.");
        return "redirect:/home";
    }

    @GetMapping("/checkout")
    public String checkout(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        Map<String, CartItem> cart = getCart(session);
        if (cart.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Ajoutez au moins un produit avant de passer au paiement.");
            return "redirect:/home";
        }

        if (!model.containsAttribute("orderForm")) {
            model.addAttribute("orderForm", new CheckoutForm());
        }

        model.addAttribute("cart", cart);
        model.addAttribute("cartTotal", calculateTotal(cart));
        return "landing/checkout";
    }

    @PostMapping("/checkout")
    public String submitCheckout(CheckoutForm form, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        Map<String, CartItem> cart = getCart(session);
        if (cart.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Votre panier est vide.");
            return "redirect:/home";
        }

        if (!StringUtils.hasText(form.getFullName()) || !StringUtils.hasText(form.getPhone()) || !StringUtils.hasText(form.getAddress())) {
            model.addAttribute("cart", cart);
            model.addAttribute("cartTotal", calculateTotal(cart));
            model.addAttribute("error", "Merci de renseigner vos coordonnées pour finaliser la commande.");
            return "landing/checkout";
        }

        OrderSummary summary = new OrderSummary(
                new ArrayList<>(cart.values()),
                form.getFullName(),
                form.getPhone(),
                form.getAddress(),
                calculateTotal(cart)
        );

        session.setAttribute("lastOrder", summary);
        session.removeAttribute("cart");
        redirectAttributes.addFlashAttribute("orderSuccess", "Votre commande a été transmise. Merci !");
        return "redirect:/clients/dashboard";
    }

    private Map<String, CartItem> getCart(HttpSession session) {
        Map<String, CartItem> cart = (Map<String, CartItem>) session.getAttribute("cart");
        if (cart == null) {
            cart = new HashMap<>();
            session.setAttribute("cart", cart);
        }
        return cart;
    }

    private Optional<Product> findProduct(String productId) {
        return PRODUCTS.stream().filter(p -> p.getId().equals(productId)).findFirst();
    }

    private BigDecimal calculateTotal(Map<String, CartItem> cart) {
        return cart.values().stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
