package com.shopflow.order.domain;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// Day 4: Order aggregate root (SPEC §5) — no setters;
// status changes only through rule-enforcing behavior methods.
public class Order {

    private final OrderId id;
    private final UUID customerId;
    private final List<OrderItem> items;
    private OrderStatus status;
    private Money total;
    private final Instant createdAt;

    Order(OrderId id, UUID customerId, List<OrderItem> items, OrderStatus status, Money total, Instant createdAt) {
        this.id = id;
        this.customerId = customerId;
        this.items = items;
        this.status = status;
        this.total = total;
        this.createdAt = createdAt;
    }

    // The aggregate is born — the factory applies business rules inside the aggregate
    public static Order create(UUID customerId, List<OrderItem> items) {
        if (customerId == null) {
            throw new IllegalArgumentException("customerId is required");
        }
        Order order = new Order(OrderId.newId(), customerId, new ArrayList<>(), OrderStatus.PENDING, Money.zero(), Instant.now());
        for (OrderItem item : items) {
            order.addItem(item);
        }
        return order;
    }

    // Rehydration from persistence — mapper only, no rule enforcement
    public static Order reconstitute(OrderId id, UUID customerId, List<OrderItem> items,
                                     OrderStatus status, Money total, Instant createdAt) {
        return new Order(id, customerId, new ArrayList<>(items), status, total, createdAt);
    }

    public void addItem(OrderItem item) {
        ensureStatus(OrderStatus.PENDING, "add an item to");
        if (hasSku(item.sku())) {
            throw new IllegalArgumentException("duplicate sku: " + item.sku());
        }
        items.add(item);
        recalculateTotal();
    }

    // Saga step: inventory reserved
    public void markInventoryReserved() {
        ensureStatus(OrderStatus.PENDING, "mark inventory reserved");
        this.status = OrderStatus.INVENTORY_RESERVED;
    }

    // Saga step: payment received
    public void confirm() {
        ensureStatus(OrderStatus.INVENTORY_RESERVED, "confirm");
        this.status = OrderStatus.CONFIRMED;
    }

    // Compensation step: cancel. reason is not aggregate state — it is flow
    // data that goes to the OrderCancelledEvent payload (SPEC §4).
    public void cancel(String reason) {
        if (status == OrderStatus.CONFIRMED) {
            throw new IllegalStateException("confirmed order cannot be cancelled");
        }
        this.status = OrderStatus.CANCELLED;
    }

    public Money total() {
        return total;
    }

    // Defensive copy: callers cannot mutate the aggregate from outside
    public List<OrderItem> getItems() {
        return List.copyOf(items);
    }

    public OrderId getId() {
        return id;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    private boolean hasSku(String sku) {
        return items.stream().anyMatch(item -> item.sku().equals(sku));
    }

    private void recalculateTotal() {
        this.total = items.stream()
                .map(OrderItem::lineTotal)
                .reduce(Money.zero(), Money::add);
    }

    private void ensureStatus(OrderStatus expected, String action) {
        if (status != expected) {
            throw new IllegalStateException("order cannot be " + action + " in state " + status);
        }
    }
}
