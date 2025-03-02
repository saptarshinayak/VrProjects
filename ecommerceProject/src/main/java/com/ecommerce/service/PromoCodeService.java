package com.ecommerce.service;

import com.ecommerce.model.PromoCode;
import java.util.List;

public interface PromoCodeService {
    PromoCode createPromoCode(PromoCode promoCode);
    PromoCode getPromoCodeById(Long id);
    PromoCode getPromoCodeByCode(String code);
    List<PromoCode> getAllPromoCodes();
    void deletePromoCode(Long id);
}
