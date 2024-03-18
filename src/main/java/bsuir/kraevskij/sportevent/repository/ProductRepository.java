package bsuir.kraevskij.sportevent.repository;

import bsuir.kraevskij.sportevent.model.Product;
import bsuir.kraevskij.sportevent.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(Category category);
}

