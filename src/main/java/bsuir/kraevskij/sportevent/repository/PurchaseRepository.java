package bsuir.kraevskij.sportevent.repository;
import bsuir.kraevskij.sportevent.model.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

}
