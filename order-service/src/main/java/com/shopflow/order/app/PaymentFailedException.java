package com.shopflow.order.app;

import java.util.UUID;

// Day 4: thrown when the payment stub answers status FAILED
public class PaymentFailedException extends RuntimeException {

    public PaymentFailedException(UUID orderId) {
        super("Payment failed for order: " + orderId);
    }
}
