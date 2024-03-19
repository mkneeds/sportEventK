package bsuir.kraevskij.sportevent.controller;

import bsuir.kraevskij.sportevent.model.Category;
import bsuir.kraevskij.sportevent.service.CategoryService;
import bsuir.kraevskij.sportevent.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ProfileController {
    CategoryService categoryService;
    ProductService productService;
    public ProfileController(CategoryService categoryService, ProductService productService) {
        this.categoryService = categoryService;
        this.productService = productService;
    }
    @GetMapping("/profile")
    public String showProfile(Model model) {
        List<Category> categories = categoryService.getAllCategory();
        Map<Category, Double> categoryPercentages = new HashMap<>();
        for (Category category : categories) {
            int productCount = productService.getProductCountByCategory(category.getId());
            double totalProductCount = productService.getTotalProductCount();
            double percentage = (productCount / totalProductCount) * 100;
            categoryPercentages.put(category, percentage);
        }

        model.addAttribute("categoryPercentages", categoryPercentages);
        model.addAttribute("category",categoryPercentages);
        return "profile";
    }
}
