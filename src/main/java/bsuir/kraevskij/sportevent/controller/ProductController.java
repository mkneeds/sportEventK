package bsuir.kraevskij.sportevent.controller;

import bsuir.kraevskij.sportevent.model.Product;
import bsuir.kraevskij.sportevent.service.JwtService;
import bsuir.kraevskij.sportevent.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;
@RestController
public class ProductController {
    private final ProductService productService;
    private final JwtService jwtService;

    public ProductController(ProductService productService, JwtService jwtService) {
        this.productService = productService;
        this.jwtService = jwtService;
    }

    @GetMapping("/product/manage-product")
    public ModelAndView showManageProductPage(Model model, Principal principal, HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = jwtService.extractUsername(jwtService.extractTokenFromCookie(request));
        List<Product> products = productService.getProductsByUsername(username);

        ModelAndView modelAndView = new ModelAndView("manageProduct");
        modelAndView.addObject("products", products);

        return modelAndView;
    }

}
