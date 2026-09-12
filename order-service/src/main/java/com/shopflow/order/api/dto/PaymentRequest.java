package com.shopflow.order.api.dto;

import java.math.BigDecimal;
import java.util.UUID;

// Mirrors the payment-service contract for Feign
public record PaymentRequest(UUID orderId, BigDecimal amount) {
}
