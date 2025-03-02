package com.ecommerce.Repositry;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.model.Wallet;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
    Wallet findByUserId(Long userId);
    
}

