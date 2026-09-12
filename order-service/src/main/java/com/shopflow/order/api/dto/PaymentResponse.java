package com.shopflow.order.api.dto;

import java.util.UUID;

// Mirrors the payment-service contract for Feign
public record PaymentResponse(UUID paymentId, UUID orderId, String status) {
}
