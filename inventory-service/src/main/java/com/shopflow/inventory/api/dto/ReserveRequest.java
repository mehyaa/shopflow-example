package com.shopflow.inventory.api.dto;

import jakarta.validation.constraints.Positive;

public record ReserveRequest(@Positive int quantity) {
}
