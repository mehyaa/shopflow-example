package com.shopflow.notification.infra;

import org.springframework.data.jpa.repository.JpaRepository;

// Spring Data interface — JPA only; JpaNotificationRepository fulfills the domain contract.
public interface SpringDataNotificationJpa extends JpaRepository<ReceivedNotificationJpa, Long> {
}
