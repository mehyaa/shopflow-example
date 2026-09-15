package com.shopflow.notification.domain;

import java.util.List;

// The domain is persistence-agnostic. The implementation lives in infra (JPA).
public interface NotificationRepository {

    ReceivedNotification save(ReceivedNotification notification);

    List<ReceivedNotification> findAll();
}
