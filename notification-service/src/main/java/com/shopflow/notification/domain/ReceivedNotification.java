package com.shopflow.notification.domain;

import java.time.Instant;

// Day 4: record of every consumed event (demo audit log) — validation
// happens in the receive factory; no setters.
public class ReceivedNotification {

    private Long id;
    private final String routingKey;
    private final String payload;
    private final Instant receivedAt;

    ReceivedNotification(Long id, String routingKey, String payload, Instant receivedAt) {
        this.id = id;
        this.routingKey = routingKey;
        this.payload = payload;
        this.receivedAt = receivedAt;
    }

    // The aggregate is born — the factory rejects blank values at birth
    public static ReceivedNotification receive(String routingKey, String payload, Instant receivedAt) {
        if (routingKey == null || routingKey.isBlank()) {
            throw new IllegalArgumentException("routingKey is required");
        }
        if (payload == null || payload.isBlank()) {
            throw new IllegalArgumentException("payload is required");
        }
        if (receivedAt == null) {
            throw new IllegalArgumentException("receivedAt is required");
        }
        return new ReceivedNotification(null, routingKey, payload, receivedAt);
    }

    // Rehydration from persistence — mapper only
    public static ReceivedNotification reconstitute(Long id, String routingKey, String payload, Instant receivedAt) {
        return new ReceivedNotification(id, routingKey, payload, receivedAt);
    }

    public Long getId() {
        return id;
    }

    public String getRoutingKey() {
        return routingKey;
    }

    public String getPayload() {
        return payload;
    }

    public Instant getReceivedAt() {
        return receivedAt;
    }
}
