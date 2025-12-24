package com.delta_nutritionMVC.delta.checkout.services;

import com.delta_nutritionMVC.delta.checkout.dtos.CheckoutForm;
import com.delta_nutritionMVC.delta.cart.models.Cart;
import com.delta_nutritionMVC.delta.order.models.Order;
import com.delta_nutritionMVC.delta.cart.services.CarteService;
import com.delta_nutritionMVC.delta.order.services.OrderService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class CheckoutServiceImpl  implements CheckoutService {
    private static final String LAST_ORDER_ID_ATTRIBUTE = "lastOrderId";

    private final CarteService cartServiceImpl;
    private final OrderService orderServiceImpl;
@Override
    public Order finalizeOrder(CheckoutForm form, HttpSession session) {
        Cart cart = cartServiceImpl.getOrCreateCart(session);
        String clientEmail = null;
        Object clientSession = session.getAttribute("clientSession");
        if (clientSession instanceof com.delta_nutritionMVC.delta.auth.dtos.SignInResponse signIn) {
            clientEmail = signIn.getEmail();
        }

        Order order = orderServiceImpl.createOrderFromCart(cart, form, clientEmail);
        session.setAttribute(LAST_ORDER_ID_ATTRIBUTE, order.getId());
        cartServiceImpl.clearCart(session);
        return order;
    }
    @Override
    public boolean hasValidContactDetails(CheckoutForm form) {
        return StringUtils.hasText(form.getFullName())
                && StringUtils.hasText(form.getPhone())
                && StringUtils.hasText(form.getAddress());
    }
    @Override
    public Order loadLastOrderFromSession(HttpSession session) {
        Long lastOrderId = (Long) session.getAttribute(LAST_ORDER_ID_ATTRIBUTE);
        if (lastOrderId == null) {
            return null;
        }
        return orderServiceImpl.findById(lastOrderId);
    }
}
