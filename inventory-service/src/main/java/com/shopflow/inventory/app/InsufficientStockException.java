package com.shopflow.inventory.app;

public class InsufficientStockException extends RuntimeException {

    public InsufficientStockException(String sku) {
        // Exact wording required by SPEC §3
        super("Insufficient stock for SKU: " + sku);
    }
}
