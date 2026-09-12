package com.shopflow.product.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

// Day 2: request DTO as a record (SPEC §10 — records for DTOs, classes for entities)
public record ProductRequest(
        @NotBlank String sku,
        @NotBlank String name,
        @NotNull @Positive BigDecimal price,
        String description) {
}
