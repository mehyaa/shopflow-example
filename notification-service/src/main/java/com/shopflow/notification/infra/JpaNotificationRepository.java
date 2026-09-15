package com.shopflow.notification.infra;

import com.shopflow.notification.domain.NotificationRepository;
import com.shopflow.notification.domain.ReceivedNotification;
import org.springframework.stereotype.Repository;

import java.util.List;

// @Repository: fulfills the domain interface over JPA.
@Repository
public class JpaNotificationRepository implements NotificationRepository {

    private final SpringDataNotificationJpa springDataNotificationJpa;

    public JpaNotificationRepository(SpringDataNotificationJpa springDataNotificationJpa) {
        this.springDataNotificationJpa = springDataNotificationJpa;
    }

    @Override
    public ReceivedNotification save(ReceivedNotification notification) {
        ReceivedNotificationJpa saved = springDataNotificationJpa.save(ReceivedNotificationMapper.toJpa(notification));
        return ReceivedNotificationMapper.toDomain(saved);
    }

    @Override
    public List<ReceivedNotification> findAll() {
        return springDataNotificationJpa.findAll().stream()
                .map(ReceivedNotificationMapper::toDomain)
                .toList();
    }
}
