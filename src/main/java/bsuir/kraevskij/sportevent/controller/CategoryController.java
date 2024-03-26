package bsuir.kraevskij.sportevent.controller;

import bsuir.kraevskij.sportevent.model.Category;
import bsuir.kraevskij.sportevent.service.CategoryService;
import bsuir.kraevskij.sportevent.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

@RestController
public class CategoryController {

    private final ProductService productService;
    private final CategoryService categoryService;

    public CategoryController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }
    @PostMapping("/admin/categories/add")
    public ResponseEntity<Object> addCategory(@RequestBody Category category) {
        categoryService.saveCategory(category);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/admin/categories/delete/{id}")
    public ResponseEntity<Object> deleteCategory(@PathVariable("id") Long id) {
        Category category = new Category();
        category.setId(id);
        categoryService.deleteCategory(category);
        return ResponseEntity.ok().build(); // Возвращаем статус 200 OK
    }

    @PostMapping("/admin/categories/edit/{id}")
    public ResponseEntity<Object> editCategory(@PathVariable("id") Long id, @RequestBody Map<String, String> requestBody) {
        String newName = requestBody.get("name");
        Category category = new Category();
        if (category == null) {
            return ResponseEntity.notFound().build();
        }
        category.setId(id);
        category.setName(newName);
        categoryService.saveCategory(category);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/popular-category")
    public ResponseEntity<Map<String, Object>> getPopularCategory() {
        Map<String, Object> response = new HashMap<>();

        List<Category> categories = categoryService.getAllCategory();
        double totalProductCount = productService.getTotalProductCount();
        String popularCategoryName = "";
        double popularCategoryPercentage = 0.0;

        for (Category category : categories) {
            int productCount = productService.getProductCountByCategory(category.getId());
            double percentage = (double) productCount / totalProductCount * 100;
            if (percentage > popularCategoryPercentage) {
                popularCategoryName = category.getName();
                popularCategoryPercentage = percentage;
            }
        }

        response.put("categoryName", popularCategoryName);
        response.put("percentage", popularCategoryPercentage);

        return ResponseEntity.ok(response);
    }
}
