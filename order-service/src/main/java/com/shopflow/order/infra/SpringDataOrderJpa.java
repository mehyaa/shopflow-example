package com.shopflow.order.infra;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

// Spring Data interface — JPA only; JpaOrderRepository fulfills the domain contract.
public interface SpringDataOrderJpa extends JpaRepository<OrderJpa, UUID> {

    List<OrderJpa> findAllByOrderByCreatedAtDesc();
}
