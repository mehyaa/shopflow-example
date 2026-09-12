package com.shopflow.common.events;

/**
 * Day 4: Single source of truth for RabbitMQ topology (see SPEC §4).
 */
public final class EventTopics {

    public static final String EXCHANGE = "shopflow.events";

    public static final String ORDER_CREATED = "order.created";
    public static final String ORDER_CONFIRMED = "order.confirmed";
    public static final String ORDER_CANCELLED = "order.cancelled";
    public static final String PAYMENT_FAILED = "payment.failed";

    public static final String NOTIFICATION_ORDER_QUEUE = "notification.order.queue";
    public static final String ORDER_PAYMENT_QUEUE = "order.payment.queue";

    private EventTopics() {
    }
}
