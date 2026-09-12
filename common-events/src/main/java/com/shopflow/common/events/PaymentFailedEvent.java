package com.shopflow.common.events;

import java.util.UUID;

/**
 * Day 4: Payment stub failed (~20% random), triggers saga compensation in order-service.
 */
public record PaymentFailedEvent(
        UUID orderId,
        String reason
) {
}
