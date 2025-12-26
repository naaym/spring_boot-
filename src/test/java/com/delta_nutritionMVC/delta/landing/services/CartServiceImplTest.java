package com.delta_nutritionMVC.delta.landing.services;

import com.delta_nutritionMVC.delta.landing.models.Cart;
import com.delta_nutritionMVC.delta.landing.models.CartItem;
import com.delta_nutritionMVC.delta.landing.models.Product;
import com.delta_nutritionMVC.delta.landing.repositories.CartRepository;
import com.delta_nutritionMVC.delta.landing.repositories.ProductRepository;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private HttpSession session;

    @InjectMocks
    private CartServiceImpl cartService;

    @Captor
    private ArgumentCaptor<Cart> cartCaptor;

    @Test
    void getOrCreateCart_createsNewCartWhenNoSessionCart() {
        when(session.getAttribute("cartId")).thenReturn(null);

        Cart savedCart = new Cart();
        savedCart.setId(42L);
        when(cartRepository.save(any(Cart.class))).thenReturn(savedCart);

        Cart result = cartService.getOrCreateCart(session);

        assertSame(savedCart, result);
        verify(session).setAttribute("cartId", 42L);
        verify(cartRepository).save(any(Cart.class));
    }

    @Test
    void getOrCreateCart_returnsExistingCartWhenSessionIdPresent() {
        Cart existingCart = new Cart();
        existingCart.setId(5L);

        when(session.getAttribute("cartId")).thenReturn(5L);
        when(cartRepository.findById(5L)).thenReturn(Optional.of(existingCart));

        Cart result = cartService.getOrCreateCart(session);

        assertSame(existingCart, result);
        verify(cartRepository, never()).save(any(Cart.class));
        verify(session, never()).setAttribute(eq("cartId"), any());
    }

    @Test
    void addProductToCart_addsProductAndSavesCart() {
        when(session.getAttribute("cartId")).thenReturn(null);
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> {
            Cart cart = invocation.getArgument(0);
            if (cart.getId() == null) {
                cart.setId(1L);
            }
            return cart;
        });

        Product product = new Product();
        product.setId("P1");
        product.setPrice(new BigDecimal("9.99"));
        when(productRepository.findById("P1")).thenReturn(Optional.of(product));

        cartService.addProductToCart("P1", session);

        verify(cartRepository, atLeastOnce()).save(cartCaptor.capture());
        Cart persistedCart = cartCaptor.getValue();

        assertEquals(1, persistedCart.getItems().size());
        CartItem item = persistedCart.getItems().get(0);
        assertEquals(1, item.getQuantity());
        assertEquals(product, item.getProduct());
    }

    @Test
    void addProductToCart_throwsExceptionWhenProductNotFound() {
        when(session.getAttribute("cartId")).thenReturn(null);
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> {
            Cart cart = invocation.getArgument(0);
            cart.setId(99L);
            return cart;
        });
        when(productRepository.findById("missing")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> cartService.addProductToCart("missing", session));
    }

    @Test
    void updateItemQuantity_removesItemWhenQuantityIsZero() {
        Cart cart = new Cart();
        cart.setId(10L);

        Product product = new Product();
        product.setId("P2");
        product.setPrice(new BigDecimal("5.00"));

        CartItem item = new CartItem(cart, product, 2);
        item.setId(7L);
        cart.getItems().add(item);

        when(session.getAttribute("cartId")).thenReturn(10L);
        when(cartRepository.findById(10L)).thenReturn(Optional.of(cart));

        cartService.updateItemQuantity(7L, 0, session);

        assertTrue(cart.getItems().isEmpty());
        verify(cartRepository).save(cart);
    }

    @Test
    void removeItem_deletesItemAndSavesCart() {
        Cart cart = new Cart();
        cart.setId(12L);

        Product product = new Product();
        product.setId("P3");
        product.setPrice(new BigDecimal("3.00"));

        CartItem item = new CartItem(cart, product, 1);
        item.setId(8L);
        cart.getItems().add(item);

        when(session.getAttribute("cartId")).thenReturn(12L);
        when(cartRepository.findById(12L)).thenReturn(Optional.of(cart));

        cartService.removeItem(8L, session);

        assertTrue(cart.getItems().isEmpty());
        verify(cartRepository).save(cart);
    }
}
