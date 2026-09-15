package com.shopflow.inventory.domain;

import java.util.List;
import java.util.Optional;

// The domain is persistence-agnostic. The implementation lives in infra (JPA).
public interface StockItemRepository {

    StockItem save(StockItem item);

    List<StockItem> findAll();

    Optional<StockItem> findBySku(String sku);

    long count();
}
