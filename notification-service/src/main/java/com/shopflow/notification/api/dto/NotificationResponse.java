package com.shopflow.notification.api.dto;

import java.time.Instant;

public record NotificationResponse(Long id, String routingKey, String payload, Instant receivedAt) {
}
