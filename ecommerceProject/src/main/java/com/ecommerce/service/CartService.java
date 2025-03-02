package com.ecommerce.service;

import com.ecommerce.model.Cart;
import org.springframework.http.ResponseEntity;

public interface CartService {
    ResponseEntity<String> addToCart(Cart cart);
    ResponseEntity<String> updateCart(Long cartId, int quantity);
    ResponseEntity<String> deleteCart(Long cartId);
}
