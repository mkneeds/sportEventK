package bsuir.kraevskij.sportevent.service;

import bsuir.kraevskij.sportevent.model.PromoCode;
import bsuir.kraevskij.sportevent.repository.PromoCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PromoCodeService {

    @Autowired
    private PromoCodeRepository promoCodeRepository;

    public PromoCode getPromoCodeByName(String promoCodeName) {
        return promoCodeRepository.findByCode(promoCodeName);
    }
}
