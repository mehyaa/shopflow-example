package com.shopflow.inventory.app;

import com.shopflow.inventory.domain.InsufficientStockException;
import com.shopflow.inventory.domain.StockItem;
import com.shopflow.inventory.domain.StockItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// Application service: orchestration only (transaction, repo calls); the stock rule
// lives in the aggregate — reserve/release go through StockItem.
@Service
public class InventoryService {

    private final StockItemRepository stockItemRepository;

    public InventoryService(StockItemRepository stockItemRepository) {
        this.stockItemRepository = stockItemRepository;
    }

    @Transactional
    public StockItem create(StockItem item) {
        return stockItemRepository.save(item);
    }

    @Transactional(readOnly = true)
    public long count() {
        return stockItemRepository.count();
    }

    @Transactional(readOnly = true)
    public StockItem getStatus(String sku) {
        return findBySku(sku);
    }

    // Day 3: called synchronously by order-service over Feign (saga step 3) —
    // kural aggregate'te: yetersiz stokta InsufficientStockException
    @Transactional
    public StockItem reserve(String sku, int quantity) {
        StockItem item = findBySku(sku);
        item.reserve(quantity);
        return stockItemRepository.save(item);
    }

    // Day 4: saga compensation — order-service releases stock when payment fails
    @Transactional
    public StockItem release(String sku, int quantity) {
        StockItem item = findBySku(sku);
        item.release(quantity);
        return stockItemRepository.save(item);
    }

    private StockItem findBySku(String sku) {
        return stockItemRepository.findBySku(sku)
                .orElseThrow(() -> new StockItemNotFoundException(sku));
    }
}
