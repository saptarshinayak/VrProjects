package com.ecommerce.Repositry;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.model.OrderHistory;

public interface OrderHistoryRepository extends JpaRepository<OrderHistory, Long>{

}
