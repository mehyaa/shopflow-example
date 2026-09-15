package com.shopflow.inventory.infra;

import com.shopflow.inventory.domain.StockItem;
import com.shopflow.inventory.domain.StockItemRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// @Repository: fulfills the domain interface over JPA.
@Repository
public class JpaStockItemRepository implements StockItemRepository {

    private final SpringDataStockItemJpa springDataStockItemJpa;

    public JpaStockItemRepository(SpringDataStockItemJpa springDataStockItemJpa) {
        this.springDataStockItemJpa = springDataStockItemJpa;
    }

    @Override
    public StockItem save(StockItem item) {
        StockItemJpa saved = springDataStockItemJpa.save(StockItemMapper.toJpa(item));
        return StockItemMapper.toDomain(saved);
    }

    @Override
    public List<StockItem> findAll() {
        return springDataStockItemJpa.findAll().stream()
                .map(StockItemMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<StockItem> findBySku(String sku) {
        return springDataStockItemJpa.findBySku(sku).map(StockItemMapper::toDomain);
    }

    @Override
    public long count() {
        return springDataStockItemJpa.count();
    }
}
