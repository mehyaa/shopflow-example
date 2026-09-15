package com.shopflow.inventory;

import com.shopflow.inventory.domain.InsufficientStockException;
import com.shopflow.inventory.domain.StockItem;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

// Day 3: aggregate rule tests — reserve/release through StockItem
class StockItemAggregateTest {

    @Test
    void createRejectsInvalidInput() {
        assertThrows(IllegalArgumentException.class, () -> StockItem.create(null, 10));
        assertThrows(IllegalArgumentException.class, () -> StockItem.create("SKU-1", -1));
        assertEquals(10, StockItem.create("SKU-1", 10).getQuantity());
    }

    @Test
    void reserveAppliesRuleInAggregate() {
        StockItem item = StockItem.create("SKU-1", 10);
        item.reserve(4);
        assertEquals(6, item.getQuantity());
        assertThrows(InsufficientStockException.class, () -> item.reserve(7));
        assertThrows(IllegalArgumentException.class, () -> item.reserve(0));
    }

    @Test
    void releaseCompensatesReserve() {
        StockItem item = StockItem.create("SKU-1", 10);
        item.reserve(10);
        item.release(10);
        assertEquals(10, item.getQuantity());
        assertThrows(IllegalArgumentException.class, () -> item.release(0));
    }
}
