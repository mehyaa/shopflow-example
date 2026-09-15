package com.shopflow.product.infra;

import org.springframework.data.jpa.repository.JpaRepository;

// Spring Data interface — JPA only; JpaProductRepository fulfills the domain contract.
public interface SpringDataProductJpa extends JpaRepository<ProductJpa, Long> {

    boolean existsBySku(String sku);
}
