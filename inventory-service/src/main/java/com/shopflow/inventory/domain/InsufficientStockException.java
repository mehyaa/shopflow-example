package com.shopflow.inventory.domain;

// The aggregate's rule exception: reservation with insufficient stock is rejected.
// (GlobalExceptionHandler maps it to 409 — SPEC §3)
public class InsufficientStockException extends RuntimeException {

    public InsufficientStockException(String sku) {
        // Exact wording required by SPEC §3
        super("Insufficient stock for SKU: " + sku);
    }
}
