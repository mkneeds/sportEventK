package bsuir.kraevskij.sportevent.service;

import bsuir.kraevskij.sportevent.exception.ResourceNotFoundException;
import bsuir.kraevskij.sportevent.exception.UnauthorizedException;
import bsuir.kraevskij.sportevent.model.Category;
import bsuir.kraevskij.sportevent.model.Product;
import bsuir.kraevskij.sportevent.model.User;
import bsuir.kraevskij.sportevent.repository.CategoryRepository;
import bsuir.kraevskij.sportevent.repository.ProductRepository;
import bsuir.kraevskij.sportevent.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private  final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, UserRepository userRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }
    public List<Product> findProductsByCategoryId(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    public Optional<Product> getProductByTitles(String title) {
        return productRepository.findByName(title);
    }
    public void updateProductCategory(Long productId, Category newCategory) {
        Product product = productRepository.findById(productId).orElse(null);
        if (product != null) {
            product.setCategory(newCategory);
            productRepository.save(product);
        }
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Product addProduct(Product product) {
        return productRepository.save(product);
    }
    public Product addProduct(Product product, String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (productRepository.existsByName(product.getName())) {
                throw new RuntimeException("Product with name '" + product.getName() + "' already exists.");
            }
            product.setUser(user);
            return productRepository.save(product);
        } else {
            throw new RuntimeException("User not found for username: " + username);
        }
    }

    public void updateProductsCategoryForDeletedCategory(Long categoryId) {
        List<Product> products = productRepository.findByCategoryId(categoryId);
        List<Category> categoriesExceptDeleted = categoryRepository.findAllByIdNot(categoryId);
        if (!categoriesExceptDeleted.isEmpty()) {
            Random random = new Random();

            for (Product product : products) {

                Category randomCategory = categoriesExceptDeleted.get(random.nextInt(categoriesExceptDeleted.size()));
                product.setCategory(randomCategory);
            }

            productRepository.saveAll(products);
        }
        categoryRepository.deleteById(categoryId);
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
    public boolean isProductNameUnique(String name) {
        Optional<Product> existingProduct = productRepository.findByName(name);
        return existingProduct.isEmpty();
    }
    public Product editProduct(Long productId, Product updatedProduct, String username) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            if (product.getUser().getUsername().equals(username)) {
                product.setName(updatedProduct.getName());
                product.setPrice(updatedProduct.getPrice());
                product.setImageUrl(updatedProduct.getImageUrl());
                product.setDescription(updatedProduct.getDescription());
                product.setDate(updatedProduct.getDate());
                product.setCategory(updatedProduct.getCategory());
                return productRepository.save(product);
            } else {
                throw new UnauthorizedException("Вы не уполномочены редактировать этот продукт");
            }
        } else {
            throw new ResourceNotFoundException("Product not found with id: " + productId);
        }
    }
}
