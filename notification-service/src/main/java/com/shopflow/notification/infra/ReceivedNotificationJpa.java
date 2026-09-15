package com.shopflow.notification.infra;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

// JPA counterpart: the persistence view of the domain aggregate — the mapper
// turns it into the domain ReceivedNotification.
@Entity
@Table(name = "received_notifications")
public class ReceivedNotificationJpa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 64)
    private String routingKey;

    @Column(nullable = false, length = 10000)
    private String payload;

    @Column(name = "received_at", nullable = false)
    private Instant receivedAt;

    protected ReceivedNotificationJpa() {
        // required by JPA
    }

    public ReceivedNotificationJpa(String routingKey, String payload, Instant receivedAt) {
        this.routingKey = routingKey;
        this.payload = payload;
        this.receivedAt = receivedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
