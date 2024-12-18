package bsuir.kraevskij.sportevent.repository;

import bsuir.kraevskij.sportevent.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByName(String name);
    List<Category> findAllByIdNot(Long id);
}
