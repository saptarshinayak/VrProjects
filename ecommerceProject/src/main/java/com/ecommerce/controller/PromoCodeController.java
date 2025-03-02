package com.ecommerce.controller;

import com.ecommerce.model.PromoCode;
import com.ecommerce.service.PromoCodeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/promocodes")
public class PromoCodeController {

	@Autowired
    private PromoCodeService promoCodeService;



    @PostMapping("/create")
    public ResponseEntity<PromoCode> createPromoCode(@RequestBody PromoCode promoCode) {
        return ResponseEntity.ok(promoCodeService.createPromoCode(promoCode));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PromoCode> getPromoCodeById(@PathVariable Long id) {
        return ResponseEntity.ok(promoCodeService.getPromoCodeById(id));
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<PromoCode> getPromoCodeByCode(@PathVariable String code) {
        return ResponseEntity.ok(promoCodeService.getPromoCodeByCode(code));
    }

    @GetMapping("/all")
    public ResponseEntity<List<PromoCode>> getAllPromoCodes() {
        return ResponseEntity.ok(promoCodeService.getAllPromoCodes());
    }  

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePromoCode(@PathVariable Long id) {
        promoCodeService.deletePromoCode(id);
        return ResponseEntity.ok("PromoCode deleted successfully");
    }
}
