package com.shopflow.notification.infra;

import com.shopflow.notification.domain.ReceivedNotification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<ReceivedNotification, Long> {
}
