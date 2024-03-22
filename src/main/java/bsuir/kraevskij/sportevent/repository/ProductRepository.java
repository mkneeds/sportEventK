package bsuir.kraevskij.sportevent.repository;

import bsuir.kraevskij.sportevent.model.Product;
import bsuir.kraevskij.sportevent.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(Category category);
    @Query("SELECT COUNT(p) FROM Product p WHERE p.category.id = :categoryId")
    int countByCategoryId(Long categoryId);

    @Query("SELECT COUNT(p) FROM Product p")
    double getTotalProductCount();
    List<Product> findByUserUsername(String username);
    Optional<Product> findByName(String name);
    boolean existsByName(String name);

}

