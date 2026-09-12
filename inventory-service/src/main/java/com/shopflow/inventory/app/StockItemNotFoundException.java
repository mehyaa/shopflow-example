package com.shopflow.inventory.app;

public class StockItemNotFoundException extends RuntimeException {

    public StockItemNotFoundException(String sku) {
        super("Stock item not found for SKU: " + sku);
    }
}
