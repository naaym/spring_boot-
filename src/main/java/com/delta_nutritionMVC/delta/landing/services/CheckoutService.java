package com.delta_nutritionMVC.delta.landing.services;

import com.delta_nutritionMVC.delta.landing.dtos.CheckoutForm;
import com.delta_nutritionMVC.delta.landing.models.Cart;
import com.delta_nutritionMVC.delta.landing.models.Order;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class CheckoutService {
    private static final String LAST_ORDER_ID_ATTRIBUTE = "lastOrderId";

    private final CartService cartService;
    private final OrderService orderService;

    public Order finalizeOrder(CheckoutForm form, HttpSession session) {
        Cart cart = cartService.getOrCreateCart(session);
        Order order = orderService.createOrderFromCart(cart, form);
        session.setAttribute(LAST_ORDER_ID_ATTRIBUTE, order.getId());
        cartService.clearCart(session);
        return order;
    }

    public boolean hasValidContactDetails(CheckoutForm form) {
        return StringUtils.hasText(form.getFullName())
                && StringUtils.hasText(form.getPhone())
                && StringUtils.hasText(form.getAddress());
    }

    public Order loadLastOrderFromSession(HttpSession session) {
        Long lastOrderId = (Long) session.getAttribute(LAST_ORDER_ID_ATTRIBUTE);
        if (lastOrderId == null) {
            return null;
        }
        return orderService.findById(lastOrderId);
    }
}
