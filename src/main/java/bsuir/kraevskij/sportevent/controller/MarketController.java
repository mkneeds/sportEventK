package bsuir.kraevskij.sportevent.controller;

import bsuir.kraevskij.sportevent.model.Category;
import bsuir.kraevskij.sportevent.model.Product;
import bsuir.kraevskij.sportevent.service.CategoryService;
import bsuir.kraevskij.sportevent.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import java.util.List;

@Controller
public class MarketController {

    private final ProductService productService;
    private final CategoryService categoryService;

    public MarketController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping("/market")
    public ModelAndView showMarketPage() {
        List<Product> products = productService.getAllProducts();
        List<Category> categories = categoryService.getAllCategory();
        ModelAndView modelAndView = new ModelAndView("market");
        modelAndView.addObject("products", products);
        modelAndView.addObject("categories", categories);
        return modelAndView;
    }
}
