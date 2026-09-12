package com.shopflow.payment.infra;

import com.shopflow.common.events.EventTopics;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Day 4: only the exchange is declared here — the payment service publishes,
// it does not consume (SPEC §4)
@Configuration
public class RabbitMqConfig {

    @Bean
    public TopicExchange eventsExchange() {
        return new TopicExchange(EventTopics.EXCHANGE);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
