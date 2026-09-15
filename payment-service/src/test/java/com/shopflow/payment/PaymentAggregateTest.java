package com.shopflow.payment;

import com.shopflow.payment.domain.Money;
import com.shopflow.payment.domain.Payment;
import com.shopflow.payment.domain.PaymentStatus;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

// Day 4: aggregate rule tests — Payment status transitions
class PaymentAggregateTest {

    @Test
    void createRejectsInvalidInput() {
        assertThrows(IllegalArgumentException.class, () -> Payment.create(null, Money.zero()));
        assertThrows(IllegalArgumentException.class, () -> Payment.create(UUID.randomUUID(), null));
    }

    @Test
    void transitionsFollowRules() {
        Payment payment = Payment.create(UUID.randomUUID(), new Money(new java.math.BigDecimal("49.90")));
        assertEquals(PaymentStatus.PENDING, payment.getStatus());
        payment.markCompleted();
        assertEquals(PaymentStatus.COMPLETED, payment.getStatus());
        assertThrows(IllegalStateException.class, () -> payment.markFailed("late")); // sadece PENDING'den

        Payment declined = Payment.create(UUID.randomUUID(), new Money(new java.math.BigDecimal("49.90")));
        declined.markFailed("declined");
        assertEquals(PaymentStatus.FAILED, declined.getStatus());
        assertThrows(IllegalStateException.class, declined::markCompleted);
    }

    @Test
    void moneyRejectsNegative() {
        assertThrows(IllegalArgumentException.class, () -> new Money(new java.math.BigDecimal("-5")));
    }
}
