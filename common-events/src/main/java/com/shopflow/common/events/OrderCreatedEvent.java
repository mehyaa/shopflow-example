package com.shopflow.common.events;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Day 4: Published by order-service on the outbox poller, consumed by notification-service.
 */
public record OrderCreatedEvent(
        UUID orderId,
        UUID customerId,
        BigDecimal totalAmount,
        List<OrderItemLine> items
) {
}
