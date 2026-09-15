package com.shopflow.order;

import com.shopflow.order.domain.Money;
import com.shopflow.order.domain.Order;
import com.shopflow.order.domain.OrderItem;
import com.shopflow.order.domain.OrderStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

// Day 4: aggregate rule tests — Order state machine
class OrderAggregateTest {

    private static OrderItem item(String sku, int quantity, String price) {
        return new OrderItem(sku, quantity, new Money(new BigDecimal(price)));
    }

    @Test
    void moneyRejectsNegativeAndAddsImmutably() {
        assertThrows(IllegalArgumentException.class, () -> new Money(new BigDecimal("-1")));
        Money zero = Money.zero();
        Money sum = zero.add(new Money(new BigDecimal("10.50"))).multiply(2);
        assertEquals(new BigDecimal("21.00"), sum.asBigDecimal());
        assertEquals(BigDecimal.ZERO, zero.asBigDecimal());
    }

    @Test
    void createAppliesRulesAndComputesTotal() {
        Order order = Order.create(UUID.randomUUID(), List.of(
                item("SKU-1", 2, "10.00"),
                item("SKU-2", 1, "5.50")));
        assertEquals(OrderStatus.PENDING, order.getStatus());
        assertEquals(new BigDecimal("25.50"), order.total().asBigDecimal());
        assertThrows(IllegalArgumentException.class,
                () -> order.addItem(item("SKU-1", 1, "10.00"))); // duplicate sku
        assertEquals(2, order.getItems().size());
        // defensive copy: the aggregate cannot be mutated from outside
        assertThrows(UnsupportedOperationException.class,
                () -> order.getItems().add(item("SKU-3", 1, "1.00")));
    }

    @Test
    void statusTransitionsFollowSagaRules() {
        Order order = Order.create(UUID.randomUUID(), List.of(item("SKU-1", 1, "10.00")));
        // cannot jump straight from PENDING to CONFIRMED — stock first
        assertThrows(IllegalStateException.class, order::confirm);
        order.markInventoryReserved();
        assertThrows(IllegalStateException.class,
                () -> order.addItem(item("SKU-2", 1, "5.00"))); // sadece PENDING'e ekleme
        order.confirm();
        assertThrows(IllegalStateException.class, () -> order.cancel("late")); // confirmed iptal edilemez
        assertEquals(OrderStatus.CONFIRMED, order.getStatus());
    }

    @Test
    void cancelFromPendingAndReservedStatesSucceeds() {
        Order pending = Order.create(UUID.randomUUID(), List.of(item("SKU-1", 1, "10.00")));
        pending.cancel("stock insufficient");
        assertEquals(OrderStatus.CANCELLED, pending.getStatus());

        Order reserved = Order.create(UUID.randomUUID(), List.of(item("SKU-1", 1, "10.00")));
        reserved.markInventoryReserved();
        reserved.cancel("payment failed");
        assertEquals(OrderStatus.CANCELLED, reserved.getStatus());
    }
}
