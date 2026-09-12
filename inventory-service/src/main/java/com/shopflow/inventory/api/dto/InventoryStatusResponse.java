package com.shopflow.inventory.api.dto;

public record InventoryStatusResponse(String sku, boolean available, int quantity) {
}
