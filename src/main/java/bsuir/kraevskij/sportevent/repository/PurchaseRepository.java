package bsuir.kraevskij.sportevent.repository;

import bsuir.kraevskij.sportevent.model.Purchase;
import bsuir.kraevskij.sportevent.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    @Query("SELECT SUM(p.amount) FROM Purchase p")
    Double sumAmount();
    List<Purchase> findByBuyer(User buyer);
    List<Purchase> findBySeller(User seller);
}
