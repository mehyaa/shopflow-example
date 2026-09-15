package com.shopflow.payment.infra;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

// Spring Data interface — JPA only; JpaPaymentRepository fulfills the domain contract.
public interface SpringDataPaymentJpa extends JpaRepository<PaymentJpa, UUID> {
}
