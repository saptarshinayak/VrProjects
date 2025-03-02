package com.ecommerce.Repositry;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.model.PromoCode;

public interface PromoCodeRepository extends JpaRepository<PromoCode, Long>{
	Optional<PromoCode> findByCode(String code);

	PromoCode findByCodeAndStatus(String promoCode, String string);
}
