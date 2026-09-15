package com.shopflow.notification.infra;

import tools.jackson.databind.ObjectMapper;
import com.shopflow.common.events.EventTopics;
import com.shopflow.common.events.OrderCancelledEvent;
import com.shopflow.common.events.OrderConfirmedEvent;
import com.shopflow.common.events.OrderCreatedEvent;
import com.shopflow.notification.app.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

// Day 4: consumes all order lifecycle events from notification.order.queue.
// One listener with a routing-key switch instead of three typed listeners on
// the same queue — a shared queue would otherwise deliver events to whichever
// consumer grabs them first, breaking typed deserialization.
@Component
public class OrderEventListener {

    private static final Logger log = LoggerFactory.getLogger(OrderEventListener.class);

    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;

    public OrderEventListener(NotificationService notificationService, ObjectMapper objectMapper) {
        this.notificationService = notificationService;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = RabbitMqConfig.NOTIFICATION_ORDER_QUEUE)
    public void onOrderEvent(Message message) {
        String routingKey = message.getMessageProperties().getReceivedRoutingKey();
        String payload = new String(message.getBody(), StandardCharsets.UTF_8);
        try {
            switch (routingKey) {
                case EventTopics.ORDER_CREATED -> {
                    OrderCreatedEvent event = objectMapper.readValue(payload, OrderCreatedEvent.class);
                    log.info("Order created: {} (total {})", event.orderId(), event.totalAmount());
                    notificationService.record(routingKey, payload);
                }
                case EventTopics.ORDER_CONFIRMED -> {
                    OrderConfirmedEvent event = objectMapper.readValue(payload, OrderConfirmedEvent.class);
                    log.info("Order confirmed: {}", event.orderId());
                    notificationService.record(routingKey, payload);
                }
                case EventTopics.ORDER_CANCELLED -> {
                    OrderCancelledEvent event = objectMapper.readValue(payload, OrderCancelledEvent.class);
                    log.info("Order cancelled: {} ({})", event.orderId(), event.reason());
                    notificationService.record(routingKey, payload);
                }
                default -> log.warn("Unexpected routing key: {}", routingKey);
            }
        } catch (Exception ex) {
            log.error("Could not process event with routing key {}: {}", routingKey, ex.getMessage());
        }
    }
}
