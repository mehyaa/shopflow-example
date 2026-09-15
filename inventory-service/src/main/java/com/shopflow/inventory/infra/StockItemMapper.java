package com.shopflow.inventory.infra;

import com.shopflow.inventory.domain.StockItem;

// Domain ↔ JPA mapping — the domain layer stays unaware of Spring/JPA.
public class StockItemMapper {

    private StockItemMapper() {
    }

    public static StockItem toDomain(StockItemJpa jpa) {
        return StockItem.reconstitute(jpa.getSku(), jpa.getQuantity());
    }

    public static StockItemJpa toJpa(StockItem item) {
        return new StockItemJpa(item.getSku(), item.getQuantity());
    }
}
