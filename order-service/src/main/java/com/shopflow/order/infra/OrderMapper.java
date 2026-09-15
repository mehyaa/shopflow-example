package com.shopflow.order.infra;

import com.shopflow.order.domain.Money;
import com.shopflow.order.domain.Order;
import com.shopflow.order.domain.OrderId;
import com.shopflow.order.domain.OrderItem;
import com.shopflow.order.domain.OrderStatus;

import java.util.ArrayList;
import java.util.List;

// Domain ↔ JPA mapping — the domain layer stays unaware of Spring/JPA.
public class OrderMapper {

    private OrderMapper() {
    }

    public static Order toDomain(OrderJpa jpa) {
        List<OrderItem> items = jpa.getItems().stream()
                .map(itemJpa -> new OrderItem(itemJpa.sku(), itemJpa.quantity(), itemJpa.unitPrice()))
                .toList();
        return Order.reconstitute(new OrderId(jpa.getId()), jpa.getCustomerId(), items,
                OrderStatus.valueOf(jpa.getStatus()), new Money(jpa.getTotalAmount()), jpa.getCreatedAt());
    }

    public static OrderJpa toJpa(Order order) {
        List<OrderItemJpa> items = order.getItems().stream()
                .map(item -> new OrderItemJpa(item.sku(), item.quantity(), item.unitPrice()))
                .toList();
        return new OrderJpa(order.getId().value(), order.getCustomerId(), order.getStatus().name(),
                order.total().asBigDecimal(), order.getCreatedAt(), items);
    }
}
