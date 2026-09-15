package com.shopflow.payment.app;

import com.shopflow.payment.domain.Money;
import com.shopflow.payment.domain.Payment;
import com.shopflow.payment.domain.PaymentRepository;
import com.shopflow.payment.infra.PaymentEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;
import java.util.UUID;

// Application service: orchestration only; status transitions live in
// the aggregate (markCompleted/markFailed).
@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentEventPublisher eventPublisher;
    private final Random random = new Random();

    public PaymentService(PaymentRepository paymentRepository, PaymentEventPublisher eventPublisher) {
        this.paymentRepository = paymentRepository;
        this.eventPublisher = eventPublisher;
    }

    // Day 4 stub: ~20% random failure on purpose, to trigger the saga
    // compensation demo in order-service (SPEC §3)
    @Transactional
    public Payment pay(UUID orderId, Money amount) {
        Payment payment = Payment.create(orderId, amount);
        if (random.nextInt(100) < 20) {
            payment.markFailed("Payment declined by stub");
            payment = paymentRepository.save(payment);
            eventPublisher.publishPaymentFailed(orderId, "Payment declined by stub");
        } else {
            payment.markCompleted();
            payment = paymentRepository.save(payment);
        }
        return payment;
    }
}
