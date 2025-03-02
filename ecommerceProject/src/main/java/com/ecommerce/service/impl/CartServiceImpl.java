package com.ecommerce.service.impl;

import com.ecommerce.Repositry.CartRepository;
import com.ecommerce.Repositry.ProductRepository;
import com.ecommerce.Repositry.UserRepository;
import com.ecommerce.model.Cart;
import com.ecommerce.model.Product;
import com.ecommerce.model.User;
import com.ecommerce.service.CartService;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService {

	@Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;
    @Autowired
	private UserRepository userRepository;

    @Override
    public ResponseEntity<String> addToCart(Cart cart) {
        Optional<User> user = userRepository.findById(cart.getUserId());
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        Optional<Product> product = productRepository.findById(cart.getProductId());
        if (product.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }

        // Check if the requested quantity is available in stock
        if (cart.getQuantity() > product.get().getStockQuantity()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Requested quantity exceeds available stock");
        }

        Cart existingCart = cartRepository.findByUserIdAndProductId(cart.getUserId(), cart.getProductId());

        if (existingCart != null) {
            // Check if the current quantity plus the added quantity exceeds stock
            if (existingCart.getQuantity() + cart.getQuantity() > product.get().getStockQuantity()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Total quantity exceeds available stock");
            }
            existingCart.setQuantity(existingCart.getQuantity() + cart.getQuantity());
            existingCart.setTotalAmount(product.get().getPrice() * existingCart.getQuantity());
            cartRepository.save(existingCart);
            return ResponseEntity.ok("Cart updated successfully");
        } else {
            cart.setTotalAmount(product.get().getPrice() * cart.getQuantity());
            cartRepository.save(cart);
            return ResponseEntity.ok("Product added to cart successfully");
        }
    }


    @Override
    public ResponseEntity<String> updateCart(Long cartId, int quantity) {
        Cart cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cart item not found");
        }

        if (quantity <= 0) {
            cartRepository.deleteById(cartId);
            return ResponseEntity.ok("Cart item removed successfully");
        }

        Product product = productRepository.findById(cart.getProductId()).orElse(null);
        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }

        cart.setQuantity(quantity);
        cart.setTotalAmount(product.getPrice() * quantity);
        cartRepository.save(cart);
        return ResponseEntity.ok("Cart updated successfully");
    }


    @Override
    public ResponseEntity<String> deleteCart(Long cartId) {
        if (!cartRepository.existsById(cartId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cart item not found");
        }

        cartRepository.deleteById(cartId);
        return ResponseEntity.ok("Cart item deleted successfully");
    }
}
