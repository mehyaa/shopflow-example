package com.shopflow.payment.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

// Day 4: Payment aggregate root — status transitions go through rule-
// enforcing behavior methods; no setters.
public class Payment {

    private final UUID paymentId;
    private final UUID orderId;
    private final Money amount;
    private PaymentStatus status;
    private final Instant createdAt;

    Payment(UUID paymentId, UUID orderId, Money amount, PaymentStatus status, Instant createdAt) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.amount = amount;
        this.status = status;
        this.createdAt = createdAt;
    }

    // The aggregate is born — factory method
    public static Payment create(UUID orderId, Money amount) {
        if (orderId == null) {
            throw new IllegalArgumentException("orderId is required");
        }
        if (amount == null) {
            throw new IllegalArgumentException("amount is required");
        }
        return new Payment(UUID.randomUUID(), orderId, amount, PaymentStatus.PENDING, Instant.now());
    }

    // Rehydration from persistence — mapper only
    public static Payment reconstitute(UUID paymentId, UUID orderId, Money amount,
                                       PaymentStatus status, Instant createdAt) {
        return new Payment(paymentId, orderId, amount, status, createdAt);
    }

    // Payment approved — only from PENDING
    public void markCompleted() {
        ensureStatus(PaymentStatus.PENDING, "complete");
        this.status = PaymentStatus.COMPLETED;
    }

    // Payment declined — reason is not aggregate state, it goes to the event payload
    public void markFailed(String reason) {
        ensureStatus(PaymentStatus.PENDING, "fail");
        this.status = PaymentStatus.FAILED;
    }

    private void ensureStatus(PaymentStatus expected, String action) {
        if (status != expected) {
            throw new IllegalStateException("payment cannot be " + action + " in state " + status);
        }
    }

    public UUID getPaymentId() {
        return paymentId;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public Money getAmount() {
        return amount;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
