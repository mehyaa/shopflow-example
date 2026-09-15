package com.shopflow.product.domain;

import java.math.BigDecimal;

// SRP: a single responsibility — price formatting.
// If formatPrice/sendEmail/checkStock shared one Helper class, every caller
// would be indirectly coupled to every method (low cohesion, high coupling).
public class PriceFormatter {

    private PriceFormatter() {
    }

    public static String format(Money price) {
        return price.amount().toPlainString() + " TL";
    }
}
