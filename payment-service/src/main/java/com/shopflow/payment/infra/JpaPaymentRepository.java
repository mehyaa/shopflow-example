package com.shopflow.payment.infra;

import com.shopflow.payment.domain.Payment;
import com.shopflow.payment.domain.PaymentRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

// @Repository: fulfills the domain interface over JPA.
@Repository
public class JpaPaymentRepository implements PaymentRepository {

    private final SpringDataPaymentJpa springDataPaymentJpa;

    public JpaPaymentRepository(SpringDataPaymentJpa springDataPaymentJpa) {
        this.springDataPaymentJpa = springDataPaymentJpa;
    }

    @Override
    public Payment save(Payment payment) {
        PaymentJpa saved = springDataPaymentJpa.save(PaymentMapper.toJpa(payment));
        return PaymentMapper.toDomain(saved);
    }

    @Override
    public Optional<Payment> findById(UUID paymentId) {
        return springDataPaymentJpa.findById(paymentId).map(PaymentMapper::toDomain);
    }
}
