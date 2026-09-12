package com.shopflow.payment.infra;

import com.shopflow.common.events.EventTopics;
import com.shopflow.common.events.PaymentFailedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PaymentEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(PaymentEventPublisher.class);

    private final RabbitTemplate rabbitTemplate;

    public PaymentEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    // Day 4: publish payment.failed — order-service consumes it and compensates the saga
    public void publishPaymentFailed(UUID orderId, String reason) {
        log.info("Publishing PaymentFailedEvent for order {}", orderId);
        rabbitTemplate.convertAndSend(EventTopics.EXCHANGE, EventTopics.PAYMENT_FAILED,
                new PaymentFailedEvent(orderId, reason));
    }
}
