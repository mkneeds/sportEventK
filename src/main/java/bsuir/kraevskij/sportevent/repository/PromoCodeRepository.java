package bsuir.kraevskij.sportevent.repository;

import bsuir.kraevskij.sportevent.model.PromoCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromoCodeRepository extends JpaRepository<PromoCode, Long> {
    PromoCode findByCode(String code);
}
