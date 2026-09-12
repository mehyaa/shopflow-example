package com.shopflow.order.api.dto;

import com.shopflow.order.domain.OrderStatus;

import java.util.UUID;

// Day 4: POST /api/orders answers 202 Accepted with the current saga state
public record OrderCreatedResponse(UUID orderId, OrderStatus status) {
}
