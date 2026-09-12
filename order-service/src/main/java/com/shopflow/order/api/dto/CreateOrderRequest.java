package com.shopflow.order.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

// Day 4: POST /api/orders request — SPEC §3
public record CreateOrderRequest(
        @NotNull UUID customerId,
        @NotEmpty @Valid List<OrderItemRequest> items) {
}
