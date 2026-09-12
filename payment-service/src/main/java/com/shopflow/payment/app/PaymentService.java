package com.shopflow.payment.app;

import com.shopflow.payment.domain.Payment;
import com.shopflow.payment.domain.PaymentStatus;
import com.shopflow.payment.infra.PaymentEventPublisher;
import com.shopflow.payment.infra.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Random;
import java.util.UUID;

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
    public Payment pay(UUID orderId, BigDecimal amount) {
        PaymentStatus status = random.nextInt(100) < 20 ? PaymentStatus.FAILED : PaymentStatus.COMPLETED;
        Payment payment = paymentRepository.save(new Payment(orderId, amount, status));
        if (status == PaymentStatus.FAILED) {
            eventPublisher.publishPaymentFailed(orderId, "Payment declined by stub");
        }
        return payment;
    }
}
