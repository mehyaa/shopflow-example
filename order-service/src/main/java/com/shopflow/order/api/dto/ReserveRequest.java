package com.shopflow.order.api.dto;

import jakarta.validation.constraints.Positive;

// Mirrors the inventory-service contract for Feign
public record ReserveRequest(@Positive int quantity) {
}
