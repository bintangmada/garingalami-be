package com.garingalami.be.repository;

import com.garingalami.be.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Query;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByCategory(String category, Pageable pageable);
    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);
    long countByActiveTrue();
    
    @Query(value = "SELECT COALESCE(SUM(quota), 0) FROM products WHERE active = true", nativeQuery = true)
    Long getTotalStock();
    
    @Query(value = "SELECT COUNT(*) FROM products WHERE active = false", nativeQuery = true)
    Long getArchivedCount();

    @Query(value = "SELECT COUNT(DISTINCT category) FROM products WHERE active = true", nativeQuery = true)
    Long getCategoryCount();
}
