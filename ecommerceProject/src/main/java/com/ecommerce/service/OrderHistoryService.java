package com.ecommerce.service;
import java.util.List;
import com.ecommerce.model.OrderHistory;
public interface OrderHistoryService {
	void saveOrderHistory(Long userId,Long orderId,String status);
	List<OrderHistory> getOrderHistoryByUerId(Long id);
	void updatedOrderHistory(Long userId,Long orderId,String newStatus);
}
