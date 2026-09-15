package com.shopflow.notification.app;

import com.shopflow.notification.domain.NotificationRepository;
import com.shopflow.notification.domain.ReceivedNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

// Application service: orchestration only; validation lives in the aggregate (receive factory)
@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Transactional
    public void record(String routingKey, String payload) {
        notificationRepository.save(ReceivedNotification.receive(routingKey, payload, Instant.now()));
        log.info("Event received [{}]: {}", routingKey, payload);
    }

    @Transactional(readOnly = true)
    public List<ReceivedNotification> findAll() {
        return notificationRepository.findAll();
    }
}
