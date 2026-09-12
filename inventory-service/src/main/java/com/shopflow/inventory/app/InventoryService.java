package com.shopflow.inventory.app;

import com.shopflow.inventory.domain.StockItem;
import com.shopflow.inventory.infra.StockItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventoryService {

    private final StockItemRepository stockItemRepository;

    public InventoryService(StockItemRepository stockItemRepository) {
        this.stockItemRepository = stockItemRepository;
    }

    @Transactional(readOnly = true)
    public long count() {
        return stockItemRepository.count();
    }

    @Transactional
    public StockItem create(StockItem item) {
        return stockItemRepository.save(item);
    }

    @Transactional(readOnly = true)
    public StockItem getStatus(String sku) {
        return findBySku(sku);
    }

    // Day 3: called synchronously by order-service over Feign (saga step 3)
    @Transactional
    public StockItem reserve(String sku, int quantity) {
        StockItem item = findBySku(sku);
        if (item.getQuantity() < quantity) {
            throw new InsufficientStockException(sku);
        }
        item.setQuantity(item.getQuantity() - quantity);
        return item;
    }

    // Day 4: saga compensation — order-service releases stock when payment fails
    @Transactional
    public StockItem release(String sku, int quantity) {
        StockItem item = findBySku(sku);
        item.setQuantity(item.getQuantity() + quantity);
        return item;
    }

    private StockItem findBySku(String sku) {
        return stockItemRepository.findBySku(sku)
                .orElseThrow(() -> new StockItemNotFoundException(sku));
    }
}
