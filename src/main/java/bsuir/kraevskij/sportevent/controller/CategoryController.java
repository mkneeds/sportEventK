package bsuir.kraevskij.sportevent.controller;

import bsuir.kraevskij.sportevent.model.Category;
import bsuir.kraevskij.sportevent.service.CategoryService;
import bsuir.kraevskij.sportevent.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
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
