package com.shopflow.order.domain;

import java.util.UUID;

// Entity identity modeled as a VO — type safety: no String/Long mix-ups.
public record OrderId(UUID value) {

    public OrderId {
        if (value == null) {
            throw new IllegalArgumentException("orderId is required");
        }
    }

    public static OrderId newId() {
        return new OrderId(UUID.randomUUID());
    }
}
