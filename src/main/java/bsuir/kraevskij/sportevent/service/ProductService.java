package bsuir.kraevskij.sportevent.service;

import bsuir.kraevskij.sportevent.model.Product;
import bsuir.kraevskij.sportevent.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Другие методы сервиса, например, добавление продукта, удаление и т.д.
}
