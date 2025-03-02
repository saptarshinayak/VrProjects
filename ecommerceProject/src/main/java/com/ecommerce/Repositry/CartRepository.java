package com.ecommerce.Repositry;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ecommerce.model.Cart;

public interface CartRepository extends JpaRepository<Cart,Long>{
	@Query("SELECT c FROM Cart c WHERE c.userId = :userId AND c.productId = :productId")
    Cart findByUserIdAndProductId(@Param("userId") Long userId, @Param("productId") Long productId);
	void deleteByUserId(Long userId);
	List<Cart> findByUserId(Long userId);
}
