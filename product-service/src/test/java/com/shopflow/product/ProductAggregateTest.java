package com.shopflow.product;

import com.shopflow.product.domain.Money;
import com.shopflow.product.domain.PriceFormatter;
import com.shopflow.product.domain.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

// Day 2: aggregate rule tests — Money immutability + Product validation
class ProductAggregateTest {

    private static Money money(String amount) {
        return new Money(new BigDecimal(amount));
    }

    @Test
    void moneyRejectsNegativeAndAddsImmutably() {
        assertThrows(IllegalArgumentException.class, () -> new Money(new BigDecimal("-1")));
        Money sum = Money.zero().add(money("0.10")).add(money("0.20"));
        assertEquals(new BigDecimal("0.30"), sum.asBigDecimal());
    }

    @Test
    void createRejectsInvalidInput() {
        assertThrows(IllegalArgumentException.class, () -> Product.create(null, "iPhone", money("1"), null));
        assertThrows(IllegalArgumentException.class, () -> Product.create("SKU", " ", money("1"), null));
        assertThrows(IllegalArgumentException.class, () -> Product.create("SKU", "iPhone", null, null));
    }

    @Test
    void updatePriceAndRenameWithoutSetters() {
        Product product = Product.create("SKU-1", "iPhone 15", money("40000.00"), "test");
        product.updatePrice(money("42999.00"));
        product.rename("iPhone 15 Pro");
        assertEquals(new BigDecimal("42999.00"), product.getPrice().asBigDecimal());
        assertEquals("iPhone 15 Pro", product.getName());
        assertThrows(IllegalArgumentException.class, () -> product.rename(" "));
    }

    @Test
    void priceFormatterFormatsSingleResponsibility() {
        assertEquals("42999.00 TL", PriceFormatter.format(money("42999.00")));
    }
}
