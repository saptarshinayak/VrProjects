package com.ecommerce.service;

import java.util.List;

import com.ecommerce.model.Order;

public interface OrderService {
	Order placeOrder(Long userId, String promoCode,String address);
    List<Order> getOrdersByUserId(Long userId);
    void updateOrderStatus(Long orderId, String status);
	void saveOrderHistory(Long userId, Long orderId, String status);
}
