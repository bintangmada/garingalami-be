package com.garingalami.be.service;

import com.garingalami.be.model.Product;
import com.garingalami.be.model.ProductImage;
import com.garingalami.be.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Map;
import java.util.HashMap;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Map<String, Long> getInventoryStats() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("totalActive", productRepository.countByActiveTrue());
        stats.put("totalStock", productRepository.getTotalStock());
        stats.put("archivedCount", productRepository.getArchivedCount());
        stats.put("categoryCount", productRepository.getCategoryCount());
        return stats;
    }

    @Transactional
    public Product toggleProductStatus(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setActive(!product.isActive());
        return productRepository.save(product);
    }

    public Page<Product> getProductsByCategory(String category, Pageable pageable) {
        return productRepository.findByCategory(category, pageable);
    }

    public Page<Product> searchProducts(String name, Pageable pageable) {
        return productRepository.findByNameContainingIgnoreCase(name, pageable);
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
            product.setExpiryDate(productDetails.getExpiryDate());
            
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
        productRepository.findById(id).ifPresent(product -> {
            product.setActive(false);
            productRepository.save(product);
        });
    }
}
