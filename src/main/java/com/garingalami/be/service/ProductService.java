package com.garingalami.be.service;

import com.garingalami.be.model.Product;
import com.garingalami.be.model.ProductImage;
import com.garingalami.be.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    public List<Product> searchProducts(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    @Transactional
    public Product createProduct(Product product) {
        // Ensure bi-directional relationship is set for gallery images
        if (product.getGallery() != null) {
            for (ProductImage image : product.getGallery()) {
                image.setProduct(product);
            }
        }
        return productRepository.save(product);
    }

    @Transactional
    public Product updateProduct(Long id, Product productDetails) {
        return productRepository.findById(id).map(product -> {
            product.setName(productDetails.getName());
            product.setCategory(productDetails.getCategory());
            product.setPrice(productDetails.getPrice());
            product.setDescription(productDetails.getDescription());
            product.setQuota(productDetails.getQuota());
            product.setMainImageUrl(productDetails.getMainImageUrl());
            
            // Handle gallery updates
            if (productDetails.getGallery() != null) {
                product.getGallery().clear();
                for (ProductImage image : productDetails.getGallery()) {
                    image.setProduct(product);
                    product.getGallery().add(image);
                }
            }
            
            return productRepository.save(product);
        }).orElseThrow(() -> new RuntimeException("Product not found with id " + id));
    }

    @Transactional
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
