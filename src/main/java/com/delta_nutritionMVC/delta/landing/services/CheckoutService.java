package com.delta_nutritionMVC.delta.landing.services;

import com.delta_nutritionMVC.delta.landing.dtos.CheckoutForm;
import com.delta_nutritionMVC.delta.landing.models.Order;
import jakarta.servlet.http.HttpSession;

public interface CheckoutService {
    Order finalizeOrder(CheckoutForm form, HttpSession session);
    boolean hasValidContactDetails(CheckoutForm form);
    Order loadLastOrderFromSession(HttpSession session);


}
