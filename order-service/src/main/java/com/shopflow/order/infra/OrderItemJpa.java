package com.shopflow.order.infra;

import com.shopflow.order.domain.Money;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

// The persistence view of the OrderItem VO — lives in an @ElementCollection;
// carries the snapshot price, unaffected by later price changes.
@Embeddable
public record OrderItemJpa(
        @Column(nullable = false, length = 64) String sku,
        @Column(nullable = false) int quantity,
        @Column(name = "unit_price", nullable = false, precision = 19, scale = 2) Money unitPrice) {
}
