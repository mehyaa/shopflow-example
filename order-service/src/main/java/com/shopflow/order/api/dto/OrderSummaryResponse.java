package com.shopflow.order.api.dto;

import com.shopflow.order.domain.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

// Day 4: CQRS read model projection for the order list (SPEC §3)
public record OrderSummaryResponse(
        UUID orderId,
        UUID customerId,
        OrderStatus status,
        BigDecimal totalAmount,
        Instant createdAt) {
}
