package com.ecommerce.Repositry;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long>{

}
