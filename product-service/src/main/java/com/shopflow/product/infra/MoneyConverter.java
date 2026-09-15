package com.shopflow.product.infra;

import com.shopflow.product.domain.Money;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.math.BigDecimal;

// Infra: Money ↔ BigDecimal mapping — the domain keeps its Money type,
// the database sees a plain DECIMAL column.
@Converter(autoApply = true)
public class MoneyConverter implements AttributeConverter<Money, BigDecimal> {

    @Override
    public BigDecimal convertToDatabaseColumn(Money money) {
        return money == null ? null : money.asBigDecimal();
    }

    @Override
    public Money convertToEntityAttribute(BigDecimal amount) {
        return amount == null ? null : new Money(amount);
    }
}
