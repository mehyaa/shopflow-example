package com.shopflow.notification.api;

import com.shopflow.notification.api.dto.NotificationResponse;
import com.shopflow.notification.app.NotificationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// Day 4: demo endpoint to inspect the events captured from RabbitMQ
@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public List<NotificationResponse> findAll() {
        return notificationService.findAll().stream()
                .map(n -> new NotificationResponse(n.getId(), n.getRoutingKey(), n.getPayload(), n.getReceivedAt()))
                .toList();
    }
}
