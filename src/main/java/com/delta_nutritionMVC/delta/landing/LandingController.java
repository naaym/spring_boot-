package com.delta_nutritionMVC.delta.landing;

import com.delta_nutritionMVC.delta.landing.dtos.CheckoutForm;
import com.delta_nutritionMVC.delta.landing.models.Cart;
import com.delta_nutritionMVC.delta.landing.services.CartService;
import com.delta_nutritionMVC.delta.landing.services.CheckoutService;
import com.delta_nutritionMVC.delta.landing.services.CatalogBrowsingService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/home")
@RequiredArgsConstructor
public class LandingController {

    private final CartService cartService;
    private final CheckoutService checkoutService;
    private final CatalogBrowsingService catalogBrowsingService;

    @GetMapping
    public String landing(Model model, HttpSession session) {
        return "redirect:/auth/login";
    }

    @PostMapping("/add-to-cart")
    public String addToCart(@RequestParam("productId") String productId,
                            @RequestParam(value = "redirectTo", required = false) String redirectTo,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        try {
            cartService.addProductToCart(productId, session);
            redirectAttributes.addFlashAttribute("message", "Produit ajouté au panier.");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }
        if (redirectTo != null && redirectTo.startsWith("/")) {
            return "redirect:" + redirectTo;
        }
        return "redirect:/home";
    }

    @PostMapping("/update-item")
    public String updateItemQuantity(@RequestParam("itemId") Long itemId,
                                     @RequestParam("quantity") int quantity,
                                     @RequestParam(value = "redirectTo", required = false) String redirectTo,
                                     HttpSession session,
                                     RedirectAttributes redirectAttributes) {
        try {
            cartService.updateItemQuantity(itemId, quantity, session);
            redirectAttributes.addFlashAttribute("message", "Quantité mise à jour.");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }
        if (redirectTo != null && redirectTo.startsWith("/")) {
            return "redirect:" + redirectTo;
        }
        return "redirect:/home";
    }

    @PostMapping("/remove-item")
    public String removeItem(@RequestParam("itemId") Long itemId,
                             @RequestParam(value = "redirectTo", required = false) String redirectTo,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        try {
            cartService.removeItem(itemId, session);
            redirectAttributes.addFlashAttribute("message", "Article supprimé du panier.");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }
        if (redirectTo != null && redirectTo.startsWith("/")) {
            return "redirect:" + redirectTo;
        }
        return "redirect:/home";
    }

    @GetMapping("/checkout")
    public String checkout(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        Cart cart = cartService.getOrCreateCart(session);
        if (cart.getItems().isEmpty()) {
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
        Cart cart = cartService.getOrCreateCart(session);
        if (cart.getItems().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Votre panier est vide.");
            return "redirect:/home";
        }

        if (!checkoutService.hasValidContactDetails(form)) {
            model.addAttribute("cart", cart);
            model.addAttribute("cartTotal", cartService.calculateTotal(cart));
            model.addAttribute("error", "Merci de renseigner vos coordonnées pour finaliser la commande.");
            return "landing/checkout";
        }

        checkoutService.finalizeOrder(form, session);
        redirectAttributes.addFlashAttribute("orderSuccess", "Votre commande a été transmise. Merci !");
        return "redirect:/clients/dashboard";
    }

    @GetMapping("/categories/{categoryId}")
    public String browseCategory(@PathVariable("categoryId") Long categoryId,
                                 @RequestParam(value = "minPrice", required = false) String minPrice,
                                 @RequestParam(value = "maxPrice", required = false) String maxPrice,
                                 Model model,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes,
                                 jakarta.servlet.http.HttpServletRequest request) {
        Cart cart = cartService.getOrCreateCart(session);

        var category = catalogBrowsingService.getCategory(categoryId);
        var priceRange = catalogBrowsingService.getPriceRangeForCategory(categoryId);
        if (!priceRange.hasProducts()) {
            redirectAttributes.addFlashAttribute("error", "Cette catégorie ne contient aucun produit pour le moment.");
            return "redirect:/home";
        }

        var resolvedMin = catalogBrowsingService.resolvePrice(minPrice, priceRange.min());
        var resolvedMax = catalogBrowsingService.resolvePrice(maxPrice, priceRange.max());

        model.addAttribute("categories", catalogBrowsingService.listCategories());
        model.addAttribute("category", category);
        model.addAttribute("products", catalogBrowsingService.filterProducts(categoryId, resolvedMin, resolvedMax));
        model.addAttribute("availableMinPrice", priceRange.min());
        model.addAttribute("availableMaxPrice", priceRange.max());
        model.addAttribute("selectedMinPrice", resolvedMin);
        model.addAttribute("selectedMaxPrice", resolvedMax);

        model.addAttribute("cart", cart);
        model.addAttribute("cartTotal", cartService.calculateTotal(cart));
        model.addAttribute("cartItemCount", cartService.calculateItemsCount(cart));

        String currentUrl = request.getRequestURI();
        if (request.getQueryString() != null && !request.getQueryString().isEmpty()) {
            currentUrl += "?" + request.getQueryString();
        }
        model.addAttribute("currentCategoryUrl", currentUrl);

        return "landing/category";
    }
}
