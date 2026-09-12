package com.shopflow.order.app;

import com.shopflow.order.api.dto.PaymentRequest;
import com.shopflow.order.api.dto.PaymentResponse;
import com.shopflow.order.infra.PaymentClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

// Day 4: resilience wrapper around the payment call (SPEC §5).
// @Retry protects against transient errors; @CircuitBreaker stops the traffic
// when payment-service is fully down. Instance configs live in application.yml.
@Component
public class PaymentInvoker {

    private static final Logger log = LoggerFactory.getLogger(PaymentInvoker.class);

    private final PaymentClient paymentClient;

    public PaymentInvoker(PaymentClient paymentClient) {
        this.paymentClient = paymentClient;
    }

    @CircuitBreaker(name = "payment", fallbackMethod = "payFallback")
    @Retry(name = "payment")
    public PaymentResponse pay(UUID orderId, BigDecimal amount) {
        return paymentClient.pay(new PaymentRequest(orderId, amount));
    }

    // Day 4 fallback: open circuit or exhausted retries — the order will be
    // CANCELLED with reason "payment unavailable"
    public PaymentResponse payFallback(UUID orderId, BigDecimal amount, Throwable cause) {
        log.warn("Payment unavailable for order {} ({}), falling back", orderId,
                cause.getClass().getSimpleName());
        return new PaymentResponse(null, orderId, "UNAVAILABLE");
    }
}
