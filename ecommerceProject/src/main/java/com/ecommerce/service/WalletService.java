package com.ecommerce.service;


import org.springframework.http.ResponseEntity;

public interface WalletService {
    ResponseEntity<String> createWallet(Long userId, double initialAmount);
}
