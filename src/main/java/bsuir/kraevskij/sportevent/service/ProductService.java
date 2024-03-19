package bsuir.kraevskij.sportevent.service;

import bsuir.kraevskij.sportevent.model.Product;
import bsuir.kraevskij.sportevent.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }


    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Product addProduct(Product product) {
        return productRepository.save(product);
    }
    public int getProductCountByCategory(Long categoryId) {
        return productRepository.countByCategoryId(categoryId);
    }

    public double getTotalProductCount() {
        return productRepository.getTotalProductCount();
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
    public List<Product> getProductsByUsername(String username) {
        return productRepository.findByUserUsername(username);
    }

}
