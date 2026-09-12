package com.shopflow.payment.api.dto;

import java.util.UUID;

// Day 4: POST /api/payments response — SPEC §3
public record PaymentResponse(UUID paymentId, UUID orderId, String status) {
}
