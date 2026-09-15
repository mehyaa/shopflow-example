package com.shopflow.inventory.infra;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// Spring Data interface — JPA only; JpaStockItemRepository fulfills the domain contract.
public interface SpringDataStockItemJpa extends JpaRepository<StockItemJpa, String> {

    java.util.Optional<StockItemJpa> findBySku(String sku);
}
