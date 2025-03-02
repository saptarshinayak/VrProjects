package com.ecommerce.service.impl;

import com.ecommerce.Repositry.PromoCodeRepository;
import com.ecommerce.model.PromoCode;

import com.ecommerce.service.PromoCodeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PromoCodeServiceImpl implements PromoCodeService {

	@Autowired
    private  PromoCodeRepository promoCodeRepository;


    @Override
    public PromoCode createPromoCode(PromoCode promoCode) {
        return promoCodeRepository.save(promoCode);
    }

    @Override
    public PromoCode getPromoCodeById(Long id) {
        return promoCodeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PromoCode not found"));
    }

    @Override
    public PromoCode getPromoCodeByCode(String code) {
        return promoCodeRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("PromoCode not found"));
    }

    @Override
    public List<PromoCode> getAllPromoCodes() {
        return promoCodeRepository.findAll();
    }

    @Override
    public void deletePromoCode(Long id) {
        if (!promoCodeRepository.existsById(id)) {
            throw new RuntimeException("PromoCode not found");
        }
        promoCodeRepository.deleteById(id);
    }
}
