package com.shopflow.payment.infra;

import com.shopflow.payment.domain.Money;
import com.shopflow.payment.domain.Payment;

import java.util.UUID;

// Domain ↔ JPA mapping — the domain layer stays unaware of Spring/JPA.
public class PaymentMapper {

    private PaymentMapper() {
    }

    public static Payment toDomain(PaymentJpa jpa) {
        return Payment.reconstitute(jpa.getPaymentId(), jpa.getOrderId(),
                new Money(jpa.getAmount()), jpa.getStatus(), jpa.getCreatedAt());
    }

    public static PaymentJpa toJpa(Payment payment) {
        return new PaymentJpa(payment.getPaymentId(), payment.getOrderId(),
                payment.getAmount().asBigDecimal(), payment.getStatus(), payment.getCreatedAt());
    }
}
