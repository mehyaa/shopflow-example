package com.shopflow.order.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

// unitPrice is optional in the request (training shortcut — SPEC §3 defines sku + quantity);
// null falls back to ZERO so the saga flow stays the focus
public record OrderItemRequest(
        @NotBlank String sku,
        @Positive int quantity,
        BigDecimal unitPrice) {
}
