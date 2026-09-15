package com.shopflow.payment.domain;

import java.util.Optional;
import java.util.UUID;

// The domain is persistence-agnostic. The implementation lives in infra (JPA).
public interface PaymentRepository {

    Payment save(Payment payment);

    Optional<Payment> findById(UUID paymentId);
}
