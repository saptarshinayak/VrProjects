package com.ecommerce.Repositry;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.model.User;
import com.ecommerce.secure.Secure;

public interface UserRepository extends JpaRepository<User, Long>{
	User findByEmailOrPhoneNumber(String email, String phoneNumber);
}
