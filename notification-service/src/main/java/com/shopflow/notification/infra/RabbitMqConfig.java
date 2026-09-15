package com.shopflow.notification.infra;

import com.shopflow.common.events.EventTopics;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Day 4: queue topology — notification.order.queue bound with order.* (SPEC §4)
@Configuration
public class RabbitMqConfig {

    public static final String NOTIFICATION_ORDER_QUEUE = "notification.order.queue";

    @Bean
    public TopicExchange eventsExchange() {
        return new TopicExchange(EventTopics.EXCHANGE);
    }

    @Bean
    public Queue notificationOrderQueue() {
        return QueueBuilder.durable(NOTIFICATION_ORDER_QUEUE).build();
    }

    @Bean
    public Binding notificationOrderBinding() {
        return BindingBuilder.bind(notificationOrderQueue()).to(eventsExchange()).with("order.*");
    }

    @Bean
    public JacksonJsonMessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }
}
