package com.shopflow.order.api.dto;

import com.shopflow.order.domain.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

// Day 4: GET /api/orders/{id} response — SPEC §3
public record OrderResponse(
        UUID orderId,
        UUID customerId,
        OrderStatus status,
        List<OrderItemResponse> items,
        BigDecimal totalAmount,
        Instant createdAt) {
}
