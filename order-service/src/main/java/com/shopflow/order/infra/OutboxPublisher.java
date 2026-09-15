package com.shopflow.order.infra;

import com.shopflow.common.events.EventTopics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class OutboxPublisher {

    private static final Logger log = LoggerFactory.getLogger(OutboxPublisher.class);

    private final OutboxRepository outboxRepository;
    private final RabbitTemplate rabbitTemplate;

    public OutboxPublisher(OutboxRepository outboxRepository, RabbitTemplate rabbitTemplate) {
        this.outboxRepository = outboxRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    // Day 4: outbox poller publishes NEW messages every 500 ms (SPEC §6).
    // The write and the publish are decoupled: if RabbitMQ is down the rows
    // simply stay NEW and get picked up on the next tick.
    @Scheduled(fixedDelay = 500)
    @Transactional
    public void publishNewMessages() {
        List<OutboxMessage> messages = outboxRepository.findByStatusOrderByIdAsc(OutboxStatus.NEW);
        for (OutboxMessage message : messages) {
            try {
                rabbitTemplate.send(EventTopics.EXCHANGE, routingKeyFor(message.getType()),
                        toJsonMessage(message.getPayload()));
                message.setStatus(OutboxStatus.SENT);
                outboxRepository.save(message);
            } catch (RuntimeException ex) {
                // keep the row NEW — the next poll retries
                log.error("Failed to publish outbox message {} ({}): {}",
                        message.getId(), message.getType(), ex.getMessage());
            }
        }
    }

    private String routingKeyFor(String type) {
        return switch (type) {
            case "OrderCreatedEvent" -> EventTopics.ORDER_CREATED;
            case "OrderConfirmedEvent" -> EventTopics.ORDER_CONFIRMED;
            case "OrderCancelledEvent" -> EventTopics.ORDER_CANCELLED;
            default -> throw new IllegalArgumentException("Unknown outbox event type: " + type);
        };
    }

    // The payload is already JSON in the outbox, so we send the raw bytes with a
    // JSON content type instead of letting the converter quote it as a string
    private Message toJsonMessage(String payload) {
        MessageProperties properties = new MessageProperties();
        properties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
        properties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
        return new Message(payload.getBytes(StandardCharsets.UTF_8), properties);
    }
}
