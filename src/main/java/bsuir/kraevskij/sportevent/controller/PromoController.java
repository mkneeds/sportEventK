package bsuir.kraevskij.sportevent.controller;

import bsuir.kraevskij.sportevent.model.PromoCode;
import bsuir.kraevskij.sportevent.service.PromoCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
public class PromoController {

    @Autowired
    private PromoCodeService promoCodeService;

    @PostMapping("/promo/use")
    public ResponseEntity<Map<String, Object>> usePromoCode(@RequestBody Map<String, String> requestBody) {
        String promoCodeName = requestBody.get("promoCodeName");
        PromoCode promoCode = promoCodeService.getPromoCodeByName(promoCodeName);
        if (promoCode != null && promoCode.getExpirationDate().after(new Date())) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Промокод успешно применен");
            response.put("createdBy", promoCode.getUser().getId());
            response.put("discountPercentage", promoCode.getDiscountPercentage());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error", "Промокод не найден или неверный продавец"));
        }
    }


}
