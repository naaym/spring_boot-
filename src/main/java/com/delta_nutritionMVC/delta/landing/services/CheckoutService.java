package com.delta_nutritionMVC.delta.landing.services;

import com.delta_nutritionMVC.delta.landing.dtos.CheckoutForm;
import com.delta_nutritionMVC.delta.landing.models.Cart;
import com.delta_nutritionMVC.delta.landing.models.CartItem;
import com.delta_nutritionMVC.delta.landing.models.OrderSummary;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CheckoutService {
    private static final String LAST_ORDER_ATTRIBUTE = "lastOrder";

    private final CartService cartService;

    public OrderSummary prepareOrder(CheckoutForm form, HttpSession session) {
        Cart cart = cartService.getOrCreateCart(session);
        BigDecimal total = cartService.calculateTotal(cart);

        OrderSummary summary = new OrderSummary(
                new ArrayList<>(cart.getItems()),
                form.getFullName(),
                form.getPhone(),
                form.getAddress(),
                total
        );

        session.setAttribute(LAST_ORDER_ATTRIBUTE, summary);
        cartService.clearCart(session);
        return summary;
    }

    public boolean hasValidContactDetails(CheckoutForm form) {
        return StringUtils.hasText(form.getFullName())
                && StringUtils.hasText(form.getPhone())
                && StringUtils.hasText(form.getAddress());
    }
}
