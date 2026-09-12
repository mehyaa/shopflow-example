package com.shopflow.common.events;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Day 4: Saga completed successfully.
 */
public record OrderConfirmedEvent(
        UUID orderId,
        UUID customerId,
        BigDecimal totalAmount
) {
}
