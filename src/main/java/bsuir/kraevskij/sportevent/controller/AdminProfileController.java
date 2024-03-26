package bsuir.kraevskij.sportevent.controller;

import bsuir.kraevskij.sportevent.model.Category;
import bsuir.kraevskij.sportevent.model.Purchase;
import bsuir.kraevskij.sportevent.model.User;
import bsuir.kraevskij.sportevent.repository.UserRepository;
import bsuir.kraevskij.sportevent.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;
@RestController
public class AdminProfileController {
    private final CategoryService categoryService;
    private final ProductService productService;
    private final AdminMessageService adminMessageService;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PurchaseService purchaseService;
    public AdminProfileController(CategoryService categoryService, ProductService productService, AdminMessageService adminMessageService, JwtService jwtService, UserRepository userRepository, PurchaseService purchaseService) {
        this.categoryService = categoryService;
        this.productService = productService;
        this.adminMessageService = adminMessageService;
        this.jwtService=jwtService;
        this.userRepository = userRepository;
        this.purchaseService = purchaseService;
    }

    @RequestMapping("/profile-admin")
    public ModelAndView showProfile(HttpServletRequest request) {
        String token = jwtService.extractTokenFromCookie(request);
        Optional<User> user = userRepository.findByUsername(jwtService.extractUsername(token));
        List<User> users = userRepository.findAll();
        List<Purchase> purchases = purchaseService.getAllPurchases();
        List<Category> categories = categoryService.getAllCategory();
        double totalAmount = purchases.stream().mapToDouble(Purchase::getAmount).sum();
        ModelAndView modelAndView = new ModelAndView("admin");
        modelAndView.addObject("user", user.get());
        modelAndView.addObject("users", users);
        modelAndView.addObject("purchases", purchases);
        modelAndView.addObject("totalAmount", totalAmount);
        modelAndView.addObject("categories", categories);
        return modelAndView;
    }
}
