package com.garingalami.be.controller;

import com.garingalami.be.model.Product;
import com.garingalami.be.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private com.garingalami.be.service.AdminLogService adminLogService;

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
    public Product createProduct(@RequestBody Product product, @RequestHeader(value = "X-Admin-User", defaultValue = "Admin") String adminUser) {
        Product created = productService.createProduct(product);
        adminLogService.log(adminUser, "CREATE_PRODUCT", "Created product: " + created.getName() + " (ID: " + created.getId() + ")");
        return created;
    }

    // Admin: Update product
    @PutMapping("/admin/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product, @RequestHeader(value = "X-Admin-User", defaultValue = "Admin") String adminUser) {
        try {
            Product updated = productService.updateProduct(id, product);
            adminLogService.log(adminUser, "UPDATE_PRODUCT", "Updated product: " + updated.getName() + " (ID: " + id + ")");
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Admin: Delete product
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id, @RequestHeader(value = "X-Admin-User", defaultValue = "Admin") String adminUser) {
        productService.deleteProduct(id);
        adminLogService.log(adminUser, "DELETE_PRODUCT", "Permanently removed product ID: " + id);
        return ResponseEntity.ok().build();
    }
}
