package com.shopflow.common.events;

import java.math.BigDecimal;

/**
 * Day 4: Line item inside OrderCreatedEvent.
 */
public record OrderItemLine(
        String sku,
        int quantity,
        BigDecimal unitPrice
) {
}
