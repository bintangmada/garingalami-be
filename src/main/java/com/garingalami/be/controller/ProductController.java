package com.garingalami.be.controller;

import com.garingalami.be.model.Product;
import com.garingalami.be.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*") // Allow frontend to access
public class ProductController {

    @Autowired
    private ProductService productService;

    // Public: Get all products
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    // Public: Get product by ID
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Public: Search and filter
    @GetMapping("/search")
    public List<Product> searchProducts(@RequestParam(required = false) String name, 
                                       @RequestParam(required = false) String category) {
        if (category != null) return productService.getProductsByCategory(category);
        if (name != null) return productService.searchProducts(name);
        return productService.getAllProducts();
    }

    // Admin: Create new product
    @PostMapping("/admin")
    public Product createProduct(@RequestBody Product product) {
        return productService.createProduct(product);
    }

    // Admin: Update product
    @PutMapping("/admin/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        try {
            return ResponseEntity.ok(productService.updateProduct(id, product));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Admin: Delete product
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }
}
