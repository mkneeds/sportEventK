package bsuir.kraevskij.sportevent.controller;

import bsuir.kraevskij.sportevent.model.Category;
import bsuir.kraevskij.sportevent.model.Product;
import bsuir.kraevskij.sportevent.model.User;
import bsuir.kraevskij.sportevent.service.AuthenticationService;
import bsuir.kraevskij.sportevent.service.CategoryService;
import bsuir.kraevskij.sportevent.service.JwtService;
import bsuir.kraevskij.sportevent.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;
@RestController
public class ProductController {
    private final ProductService productService;
    private final JwtService jwtService;
    private final CategoryService categoryService;

    public ProductController(ProductService productService, JwtService jwtService, CategoryService categoryService) {
        this.productService = productService;
        this.jwtService = jwtService;
        this.categoryService = categoryService;
    }

    @GetMapping("/product/manage-product")
    public ModelAndView showManageProductPage(Model model, Principal principal, HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = jwtService.extractUsername(jwtService.extractTokenFromCookie(request));
        List<Product> products = productService.getProductsByUsername(username);
        List<Category> categories = categoryService.getAllCategory();

        ModelAndView modelAndView = new ModelAndView("manageProduct");
        modelAndView.addObject("products", products);
        modelAndView.addObject("categories", categories);
        return modelAndView;
    }
    @DeleteMapping("/product/delete/{productId}")
    public ResponseEntity<Object> deleteProduct(@PathVariable Long productId, HttpServletRequest request) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/product/edit/{productId}")
    public ResponseEntity<Object> editProduct(@PathVariable Long productId, @RequestBody Product updatedProduct, HttpServletRequest request) {
        String username = jwtService.extractUsername(jwtService.extractTokenFromCookie(request));
        productService.editProduct(productId, updatedProduct, username);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/product/add")
    public ResponseEntity<Object> addProduct(@RequestBody Product newProduct, HttpServletRequest request) {
        try {
            String username = jwtService.extractUsername(jwtService.extractTokenFromCookie(request));
            productService.addProduct(newProduct, username);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ошибка при добавлении продукта: " + e.getMessage());
        }
    }
}
