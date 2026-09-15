package com.shopflow.notification;

import com.shopflow.notification.domain.ReceivedNotification;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

// Day 4: aggregate rule tests — receive factory validation
class ReceivedNotificationTest {

    @Test
    void receiveRejectsBlankInput() {
        Instant now = Instant.now();
        assertThrows(IllegalArgumentException.class, () -> ReceivedNotification.receive(" ", "{}", now));
        assertThrows(IllegalArgumentException.class, () -> ReceivedNotification.receive("order.created", " ", now));
        assertThrows(IllegalArgumentException.class, () -> ReceivedNotification.receive("order.created", "{}", null));
    }

    @Test
    void receiveCapturesPayload() {
        ReceivedNotification notification = ReceivedNotification.receive("order.created", "{}", Instant.now());
        assertEquals("order.created", notification.getRoutingKey());
        assertEquals("{}", notification.getPayload());
    }
}
