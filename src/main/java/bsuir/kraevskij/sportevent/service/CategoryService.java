package bsuir.kraevskij.sportevent.service;

import bsuir.kraevskij.sportevent.model.Category;
import bsuir.kraevskij.sportevent.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

}