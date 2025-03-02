package com.ecommerce.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ecommerce.Repositry.UserRepository;
import com.ecommerce.Repositry.WalletRepository;
import com.ecommerce.model.User;
import com.ecommerce.model.Wallet;
import com.ecommerce.service.WalletService;

@Service
public class WalletServiceImpl implements WalletService {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseEntity<String> createWallet(Long userId, double initialAmount) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        if (walletRepository.findByUserId(userId) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Wallet already exists for this user");
        }

        Wallet wallet = new Wallet();
        wallet.setUser(user);
        wallet.setAmount(initialAmount);
        walletRepository.save(wallet);

        return ResponseEntity.ok("Wallet created successfully");
    }
}

