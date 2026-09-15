package com.shopflow.product.domain;

import java.math.BigDecimal;

// Value Object: identity-less, immutable, compared by value.
// double is never used for money — BigDecimal is exact for decimals.
public record Money(BigDecimal amount) {

    public Money {
        if (amount == null) {
            throw new IllegalArgumentException("money amount is required");
        }
        if (amount.signum() < 0) {
            throw new IllegalArgumentException("money amount cannot be negative: " + amount);
        }
    }

    public static Money zero() {
        return new Money(BigDecimal.ZERO);
    }

    public Money add(Money other) {
        return new Money(amount.add(other.amount()));
    }

    public Money multiply(int quantity) {
        return new Money(amount.multiply(BigDecimal.valueOf(quantity)));
    }

    public BigDecimal asBigDecimal() {
        return amount;
    }
}
