package com.delta_nutritionMVC.delta.landing.services;

import com.delta_nutritionMVC.delta.landing.models.Cart;
import com.delta_nutritionMVC.delta.landing.models.CartItem;
import com.delta_nutritionMVC.delta.landing.models.Product;
import com.delta_nutritionMVC.delta.landing.repositories.CartRepository;
import com.delta_nutritionMVC.delta.landing.repositories.ProductRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {
    private static final String CART_ID_ATTRIBUTE = "cartId";

    private final ProductRepository productRepository;
    private final CartRepository cartRepository;

    public List<Product> listProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> findProduct(String productId) {
        return productRepository.findById(productId);
    }

    @Transactional
    public Cart getOrCreateCart(HttpSession session) {
        Long cartId = (Long) session.getAttribute(CART_ID_ATTRIBUTE);
        if (cartId != null) {
            return cartRepository.findById(cartId).orElseGet(() -> createNewCart(session));
        }
        return createNewCart(session);
    }

    private Cart createNewCart(HttpSession session) {
        Cart cart = new Cart();
        Cart saved = cartRepository.save(cart);
        session.setAttribute(CART_ID_ATTRIBUTE, saved.getId());
        return saved;
    }

    @Transactional
    public void addProductToCart(String productId, HttpSession session) {
        Cart cart = getOrCreateCart(session);
        Product product = findProduct(productId).orElseThrow(() -> new IllegalArgumentException("Produit introuvable"));

        cart.addOrIncrementItem(product);
        cartRepository.save(cart);
    }

    public BigDecimal calculateTotal(Cart cart) {
        return cart.getTotal();
    }

    @Transactional
    public void clearCart(HttpSession session) {
        Long cartId = (Long) session.getAttribute(CART_ID_ATTRIBUTE);
        if (cartId != null) {
            cartRepository.deleteById(cartId);
            session.removeAttribute(CART_ID_ATTRIBUTE);
        }
    }
}
