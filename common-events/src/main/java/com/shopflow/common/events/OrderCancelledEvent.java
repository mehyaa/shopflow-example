package com.shopflow.common.events;

import java.util.UUID;

/**
 * Day 4: Saga compensation (payment failed or inventory reservation failed).
 */
public record OrderCancelledEvent(
        UUID orderId,
        UUID customerId,
        String reason
) {
}
