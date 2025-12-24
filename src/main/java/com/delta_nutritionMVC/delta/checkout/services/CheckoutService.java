package com.delta_nutritionMVC.delta.checkout.services;

import com.delta_nutritionMVC.delta.checkout.dtos.CheckoutForm;
import com.delta_nutritionMVC.delta.order.models.Order;
import jakarta.servlet.http.HttpSession;

public interface CheckoutService {
    Order finalizeOrder(CheckoutForm form, HttpSession session);
    boolean hasValidContactDetails(CheckoutForm form);
    Order loadLastOrderFromSession(HttpSession session);


}
