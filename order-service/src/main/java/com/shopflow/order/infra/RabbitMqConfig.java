package com.shopflow.order.infra;

import com.shopflow.common.events.EventTopics;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Day 4: RabbitMQ topology — topic exchange shopflow.events (SPEC §4).
// No DLQ on purpose: the training keeps the failure handling in the saga code.
@Configuration
public class RabbitMqConfig {

    public static final String ORDER_PAYMENT_QUEUE = "order.payment.queue";

    @Bean
    public TopicExchange eventsExchange() {
        return new TopicExchange(EventTopics.EXCHANGE);
    }

    @Bean
    public Queue orderPaymentQueue() {
        return QueueBuilder.durable(ORDER_PAYMENT_QUEUE).build();
    }

    @Bean
    public Binding orderPaymentBinding() {
        return BindingBuilder.bind(orderPaymentQueue()).to(eventsExchange()).with(EventTopics.PAYMENT_FAILED);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
