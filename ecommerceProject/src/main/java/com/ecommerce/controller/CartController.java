package com.ecommerce.controller;

import com.ecommerce.model.Cart;
import com.ecommerce.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<String> addToCart(@RequestBody Cart cart) {
        return cartService.addToCart(cart);
    }

    @PutMapping("/update/{cartId}")
    public ResponseEntity<String> updateCart(@PathVariable Long cartId, @RequestBody Cart cart) {
        return cartService.updateCart(cartId, cart.getQuantity());
    }


    @DeleteMapping("/delete/{cartId}")
    public ResponseEntity<String> deleteCart(@PathVariable Long cartId) {
        return cartService.deleteCart(cartId);
    }
}
