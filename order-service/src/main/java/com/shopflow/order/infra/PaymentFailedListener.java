package com.shopflow.order.infra;

import com.shopflow.common.events.PaymentFailedEvent;
import com.shopflow.order.app.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

// Day 4: saga compensation trigger — payment-service publishes payment.failed
// when the stub rejects the payment (SPEC §4)
@Component
public class PaymentFailedListener {

    private static final Logger log = LoggerFactory.getLogger(PaymentFailedListener.class);

    private final OrderService orderService;

    public PaymentFailedListener(OrderService orderService) {
        this.orderService = orderService;
    }

    @RabbitListener(queues = RabbitMqConfig.ORDER_PAYMENT_QUEUE)
    public void onPaymentFailed(PaymentFailedEvent event) {
        log.info("PaymentFailedEvent received for order {}: {}", event.orderId(), event.reason());
        orderService.cancelOrder(event.orderId(), event.reason(), true);
    }
}
