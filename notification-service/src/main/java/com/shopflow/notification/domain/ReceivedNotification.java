package com.shopflow.notification.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

// Day 4: every event consumed from RabbitMQ is stored here (demo audit log)
@Entity
@Table(name = "received_notifications")
public class ReceivedNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 64)
    private String routingKey;

    @Column(nullable = false, length = 10000)
    private String payload;

    @Column(name = "received_at", nullable = false)
    private Instant receivedAt;

    protected ReceivedNotification() {
        // required by JPA
    }

    public ReceivedNotification(String routingKey, String payload, Instant receivedAt) {
        this.routingKey = routingKey;
        this.payload = payload;
        this.receivedAt = receivedAt;
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
