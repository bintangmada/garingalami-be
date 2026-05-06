package com.garingalami.be.controller;

import com.garingalami.be.model.Product;
import com.garingalami.be.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private com.garingalami.be.service.AdminLogService adminLogService;

    // Admin: Get Inventory Stats
    @GetMapping("/admin/stats")
    public ResponseEntity<java.util.Map<String, Long>> getInventoryStats() {
        return ResponseEntity.ok(productService.getInventoryStats());
    }

    // Public: Get all products (Paginated)
    @GetMapping
    public Page<Product> getAllProducts(@PageableDefault(size = 10) Pageable pageable) {
        return productService.getAllProducts(pageable);
    }

    // Public: Get product by ID
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Public: Search and filter (Paginated)
    @GetMapping("/search")
    public Page<Product> searchProducts(@RequestParam(required = false) String name, 
                                        @RequestParam(required = false) String category,
                                        @PageableDefault(size = 10) Pageable pageable) {
        if (category != null) return productService.getProductsByCategory(category, pageable);
        if (name != null) return productService.searchProducts(name, pageable);
        return productService.getAllProducts(pageable);
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

    // Admin: Toggle product status (Active/Inactive)
    @PatchMapping("/admin/{id}/toggle")
    public ResponseEntity<Product> toggleProductStatus(@PathVariable Long id, @RequestHeader(value = "X-Admin-User", defaultValue = "Admin") String adminUser) {
        Product updated = productService.toggleProductStatus(id);
        String status = updated.isActive() ? "ACTIVATED" : "DEACTIVATED";
        adminLogService.log(adminUser, "TOGGLE_STATUS", status + " product: " + updated.getName() + " (ID: " + id + ")");
        return ResponseEntity.ok(updated);
    }

    // Admin: Delete product
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id, @RequestHeader(value = "X-Admin-User", defaultValue = "Admin") String adminUser) {
        productService.deleteProduct(id);
        adminLogService.log(adminUser, "DELETE_PRODUCT", "Permanently removed product ID: " + id);
        return ResponseEntity.ok().build();
    }
}
