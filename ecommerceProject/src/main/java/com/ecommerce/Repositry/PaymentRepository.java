package com.ecommerce.Repositry;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long>{

}
