package com.ecommerce.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.Repositry.UserRepository;
import com.ecommerce.Repositry.WalletAuditRepository;
import com.ecommerce.Repositry.WalletRepository;
import com.ecommerce.dto.WalletRequest;
import com.ecommerce.model.User;
import com.ecommerce.model.Wallet;
import com.ecommerce.model.WalletAudit;


@RestController
@RequestMapping("/wallet")
public class WalletController {

	@Autowired
    private  WalletRepository walletRepository;
	
	@Autowired
    private  UserRepository userRepository;
	
	@Autowired
    private  WalletAuditRepository walletAuditRepository;

    

    @PostMapping("/create")
    public ResponseEntity<String> createWallet(@RequestBody WalletRequest request) {
        User user = userRepository.findById(request.getUserId()).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        Wallet wallet = new Wallet();
        wallet.setUser(user);
        wallet.setAmount(request.getAmount());

        walletRepository.save(wallet);

        // Save wallet audit entry
        WalletAudit audit = new WalletAudit();
        audit.setWallet(wallet);
        audit.setAmount(request.getAmount());
        audit.setTransactionType("CREDIT"); // Since it's an initial wallet creation
        walletAuditRepository.save(audit);

        return ResponseEntity.ok("Wallet created successfully for User ID: " + request.getUserId());
    }

    @PutMapping("/credit")
    public ResponseEntity<String> creditWallet(@RequestBody WalletRequest request) {
        Wallet wallet = walletRepository.findByUserId(request.getUserId());

        if (wallet != null) {
            wallet.setAmount(wallet.getAmount() + request.getAmount());
            walletRepository.save(wallet);

            // Save wallet audit entry
            WalletAudit audit = new WalletAudit();
            audit.setWallet(wallet);
            audit.setAmount(request.getAmount());
            audit.setTransactionType("CREDIT");

            walletAuditRepository.save(audit);

            return ResponseEntity.ok("Amount credited successfully. New Balance: " + wallet.getAmount());
        } else {
            return ResponseEntity.badRequest().body("Wallet not found for User ID: " + request.getUserId());
        }
    }

    @PutMapping("/debit")
    public ResponseEntity<String> debitWallet(@RequestBody WalletRequest request) {
        Wallet wallet = walletRepository.findByUserId(request.getUserId());

        if (wallet != null) {
            if (wallet.getAmount() >= request.getAmount()) {
                wallet.setAmount(wallet.getAmount() - request.getAmount());
                walletRepository.save(wallet);

                // Save wallet audit entry
                WalletAudit audit = new WalletAudit();
                audit.setWallet(wallet);
                audit.setAmount(request.getAmount());
                audit.setTransactionType("DEBIT");

                walletAuditRepository.save(audit);

                return ResponseEntity.ok("Amount debited successfully. New Balance: " + wallet.getAmount());
            } else {
                return ResponseEntity.badRequest().body("Insufficient balance in wallet.");
            }
        } else {
            return ResponseEntity.badRequest().body("Wallet not found for User ID: " + request.getUserId());
        }
    }
}
