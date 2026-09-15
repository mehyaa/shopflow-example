package com.shopflow.order.domain;

// Value Object: carries the snapshot price — unaffected by later price changes.
public record OrderItem(String sku, int quantity, Money unitPrice) {

    public OrderItem {
        if (sku == null || sku.isBlank()) {
            throw new IllegalArgumentException("sku is required");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be positive: " + quantity);
        }
        if (unitPrice == null) {
            throw new IllegalArgumentException("unitPrice is required");
        }
    }

    public Money lineTotal() {
        return unitPrice.multiply(quantity);
    }
}
