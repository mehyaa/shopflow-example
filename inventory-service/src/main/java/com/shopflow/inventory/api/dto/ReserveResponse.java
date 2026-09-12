package com.shopflow.inventory.api.dto;

public record ReserveResponse(String sku, int reservedQuantity) {
}
