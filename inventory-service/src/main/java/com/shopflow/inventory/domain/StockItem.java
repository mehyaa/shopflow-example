package com.shopflow.inventory.domain;

// Day 3: StockItem aggregate root — stock rules live in the aggregate, no setters.
// Identity: sku (natural identity — services speak in skus).
public class StockItem {

    private final String sku;
    private int quantity;

    StockItem(String sku, int quantity) {
        this.sku = sku;
        this.quantity = quantity;
    }

    // The aggregate is born — the factory applies validation at birth
    public static StockItem create(String sku, int quantity) {
        validate(sku, quantity);
        return new StockItem(sku, quantity);
    }

    // Rehydration from persistence — mapper only
    public static StockItem reconstitute(String sku, int quantity) {
        return new StockItem(sku, quantity);
    }

    // Order rule: reservation is rejected when stock is insufficient
    public void reserve(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("reserved quantity must be positive: " + quantity);
        }
        if (this.quantity < quantity) {
            throw new InsufficientStockException(sku);
        }
        this.quantity -= quantity;
    }

    // Saga telafisi: rezerve edilen stok geri verilir
    public void release(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("released quantity must be positive: " + quantity);
        }
        this.quantity += quantity;
    }

    private static void validate(String sku, int quantity) {
        if (sku == null || sku.isBlank()) {
            throw new IllegalArgumentException("sku is required");
        }
        if (quantity < 0) {
            throw new IllegalArgumentException("quantity cannot be negative: " + quantity);
        }
    }

    public String getSku() {
        return sku;
    }

    public int getQuantity() {
        return quantity;
    }
}
