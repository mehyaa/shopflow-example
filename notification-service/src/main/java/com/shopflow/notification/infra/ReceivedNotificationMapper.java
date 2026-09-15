package com.shopflow.notification.infra;

import com.shopflow.notification.domain.ReceivedNotification;

// Domain ↔ JPA mapping — the domain layer stays unaware of Spring/JPA.
public class ReceivedNotificationMapper {

    private ReceivedNotificationMapper() {
    }

    public static ReceivedNotification toDomain(ReceivedNotificationJpa jpa) {
        return ReceivedNotification.reconstitute(jpa.getId(), jpa.getRoutingKey(),
                jpa.getPayload(), jpa.getReceivedAt());
    }

    public static ReceivedNotificationJpa toJpa(ReceivedNotification notification) {
        return new ReceivedNotificationJpa(notification.getRoutingKey(), notification.getPayload(),
                notification.getReceivedAt());
    }
}
