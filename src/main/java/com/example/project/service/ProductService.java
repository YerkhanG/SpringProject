package com.example.project.service;

import com.example.project.model.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductService {

    private final Map<String, Product> productRepository = new HashMap<>();

    public void createProduct(Product product) {
        productRepository.put(product.getProductId(), product);
    }

    public Product getProductById(String productId) {
        return productRepository.get(productId);
    }

    public List<Product> getAllProducts() {
        return new ArrayList<>(productRepository.values());
    }

    public boolean updateProductQuantity(String productId, int quantityChange) {
        Product product = productRepository.get(productId);
        if (product != null) {
            product.setQuantity(product.getQuantity() + quantityChange);
            return true;
        }
        return false;
    }

    // Новый метод для обновления продукта
    public boolean updateProduct(Product updatedProduct) {
        if (productRepository.containsKey(updatedProduct.getProductId())) {
            productRepository.put(updatedProduct.getProductId(), updatedProduct);
            return true;
        }
        return false;
    }
}
