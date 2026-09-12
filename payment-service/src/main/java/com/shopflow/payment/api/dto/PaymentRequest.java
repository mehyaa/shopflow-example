package com.shopflow.payment.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

// Day 4: POST /api/payments request — SPEC §3
public record PaymentRequest(@NotNull UUID orderId, @NotNull @Positive BigDecimal amount) {
}
