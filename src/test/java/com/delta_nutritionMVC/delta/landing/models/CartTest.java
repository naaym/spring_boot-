package com.delta_nutritionMVC.delta.landing.models;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CartTest {

    @Test
    void addOrIncrementItem_addsNewItemWhenNotPresent() {
        Cart cart = new Cart();
        Product product = new Product();
        product.setId("P1");
        product.setPrice(new BigDecimal("10.00"));

        cart.addOrIncrementItem(product);

        assertEquals(1, cart.getItems().size());
        assertEquals(1, cart.getItems().get(0).getQuantity());
    }

    @Test
    void addOrIncrementItem_incrementsQuantityWhenProductAlreadyInCart() {
        Cart cart = new Cart();
        Product product = new Product();
        product.setId("P1");
        product.setPrice(new BigDecimal("10.00"));

        cart.addOrIncrementItem(product);
        cart.addOrIncrementItem(product);

        assertEquals(1, cart.getItems().size());
        assertEquals(2, cart.getItems().get(0).getQuantity());
    }

    @Test
    void updateItemQuantity_updatesOrRemovesAccordingToQuantity() {
        Cart cart = new Cart();
        Product product = new Product();
        product.setId("P1");
        product.setPrice(new BigDecimal("10.00"));

        CartItem item = new CartItem(cart, product, 1);
        item.setId(1L);
        cart.getItems().add(item);

        cart.updateItemQuantity(1L, 3);
        assertEquals(3, item.getQuantity());

        cart.updateItemQuantity(1L, 0);
        assertTrue(cart.getItems().isEmpty());
    }

    @Test
    void removeItem_deletesMatchingItem() {
        Cart cart = new Cart();
        Product product = new Product();
        product.setId("P1");
        product.setPrice(new BigDecimal("5.00"));

        CartItem item = new CartItem(cart, product, 2);
        item.setId(10L);
        cart.getItems().add(item);

        cart.removeItem(10L);

        assertTrue(cart.getItems().isEmpty());
    }

    @Test
    void getTotal_calculatesSumOfLineItems() {
        Cart cart = new Cart();

        Product first = new Product();
        first.setId("P1");
        first.setPrice(new BigDecimal("2.50"));
        CartItem firstItem = new CartItem(cart, first, 2);
        firstItem.setId(1L);

        Product second = new Product();
        second.setId("P2");
        second.setPrice(new BigDecimal("1.00"));
        CartItem secondItem = new CartItem(cart, second, 3);
        secondItem.setId(2L);

        cart.getItems().add(firstItem);
        cart.getItems().add(secondItem);

        assertEquals(new BigDecimal("8.00"), cart.getTotal());
    }
}
