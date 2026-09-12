package com.shopflow.order.app;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopflow.common.events.OrderCancelledEvent;
import com.shopflow.common.events.OrderConfirmedEvent;
import com.shopflow.common.events.OrderCreatedEvent;
import com.shopflow.common.events.OrderItemLine;
import com.shopflow.order.api.dto.CreateOrderRequest;
import com.shopflow.order.api.dto.OrderCreatedResponse;
import com.shopflow.order.api.dto.OrderItemRequest;
import com.shopflow.order.api.dto.OrderItemResponse;
import com.shopflow.order.api.dto.OrderResponse;
import com.shopflow.order.api.dto.OrderSummaryResponse;
import com.shopflow.order.api.dto.PaymentResponse;
import com.shopflow.order.api.dto.ReleaseRequest;
import com.shopflow.order.api.dto.ReserveRequest;
import com.shopflow.order.domain.Order;
import com.shopflow.order.domain.OrderLine;
import com.shopflow.order.domain.OrderStatus;
import com.shopflow.order.domain.OutboxMessage;
import com.shopflow.order.infra.InventoryClient;
import com.shopflow.order.infra.OrderRepository;
import com.shopflow.order.infra.OutboxRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

// Day 4: saga orchestrator — hybrid design on purpose (SPEC §5):
// sync Feign calls for inventory/payment + async events through the outbox.
@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final OutboxRepository outboxRepository;
    private final InventoryClient inventoryClient;
    private final PaymentInvoker paymentInvoker;
    private final ObjectMapper objectMapper;

    public OrderService(OrderRepository orderRepository,
                        OutboxRepository outboxRepository,
                        InventoryClient inventoryClient,
                        PaymentInvoker paymentInvoker,
                        ObjectMapper objectMapper) {
        this.orderRepository = orderRepository;
        this.outboxRepository = outboxRepository;
        this.inventoryClient = inventoryClient;
        this.paymentInvoker = paymentInvoker;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public OrderCreatedResponse createOrder(CreateOrderRequest request) {
        // Step 1: persist the aggregate as PENDING + write OrderCreatedEvent to the outbox
        Order order = new Order(request.customerId());
        for (OrderItemRequest item : request.items()) {
            // Day 4: kept simple for the training — the request carries the unit price;
            // a production flow would look the price up in product-service
            BigDecimal unitPrice = item.unitPrice() != null ? item.unitPrice() : BigDecimal.ZERO;
            order.addLine(new OrderLine(item.sku(), item.quantity(), unitPrice));
        }
        order.recalculateTotal();
        orderRepository.save(order);

        appendOutbox(order, "OrderCreatedEvent",
                new OrderCreatedEvent(order.getId(), order.getCustomerId(), order.getTotalAmount(), toItemLines(order)));

        // Step 2: synchronous inventory reservation (Feign)
        try {
            for (OrderLine line : order.getItems()) {
                inventoryClient.reserve(line.getSku(), new ReserveRequest(line.getQuantity()));
            }
            order.setStatus(OrderStatus.INVENTORY_RESERVED);
        } catch (RuntimeException ex) {
            log.warn("Inventory reservation failed for order {}: {}", order.getId(), ex.getMessage());
            return cancel(order, "Insufficient stock");
        }

        // Step 3: payment call wrapped with CircuitBreaker + Retry
        PaymentResponse payment;
        try {
            payment = paymentInvoker.pay(order.getId(), order.getTotalAmount());
            if (!"COMPLETED".equals(payment.status())) {
                throw new PaymentFailedException(order.getId());
            }
        } catch (PaymentFailedException ex) {
            releaseInventory(order);
            return cancel(order, "payment failed");
        }

        // Step 4: happy path — publish the confirmation through the outbox
        order.setStatus(OrderStatus.CONFIRMED);
        appendOutbox(order, "OrderConfirmedEvent",
                new OrderConfirmedEvent(order.getId(), order.getCustomerId(), order.getTotalAmount()));
        orderRepository.save(order);
        return new OrderCreatedResponse(order.getId(), order.getStatus());
    }

    // Day 4: saga compensation — called by the REST cancel endpoint and by the
    // PaymentFailedEvent consumer; idempotent on purpose
    @Transactional
    public OrderCreatedResponse cancelOrder(UUID orderId, String reason, boolean releaseInventory) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        if (order.getStatus() == OrderStatus.CANCELLED || order.getStatus() == OrderStatus.CONFIRMED) {
            log.info("Order {} already {}, ignoring cancel request", orderId, order.getStatus());
            return new OrderCreatedResponse(order.getId(), order.getStatus());
        }
        if (releaseInventory && order.getStatus() == OrderStatus.INVENTORY_RESERVED) {
            releaseInventory(order);
        }
        return cancel(order, reason);
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        List<OrderItemResponse> items = order.getItems().stream()
                .map(line -> new OrderItemResponse(line.getSku(), line.getQuantity(), line.getUnitPrice()))
                .toList();
        return new OrderResponse(order.getId(), order.getCustomerId(), order.getStatus(),
                items, order.getTotalAmount(), order.getCreatedAt());
    }

    // Day 4: simple read model — CQRS discussion.
    // The query side reads directly from the orders table and projects into a
    // slim summary DTO; a full CQRS setup would use a dedicated read model/view.
    @Transactional(readOnly = true)
    public List<OrderSummaryResponse> listOrders() {
        return orderRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(order -> new OrderSummaryResponse(order.getId(), order.getCustomerId(),
                        order.getStatus(), order.getTotalAmount(), order.getCreatedAt()))
                .toList();
    }

    private OrderCreatedResponse cancel(Order order, String reason) {
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
        appendOutbox(order, "OrderCancelledEvent",
                new OrderCancelledEvent(order.getId(), order.getCustomerId(), reason));
        return new OrderCreatedResponse(order.getId(), order.getStatus());
    }

    private void releaseInventory(Order order) {
        for (OrderLine line : order.getItems()) {
            try {
                inventoryClient.release(line.getSku(), new ReleaseRequest(line.getQuantity()));
            } catch (RuntimeException ex) {
                log.warn("Inventory release failed for order {} sku {}: {}",
                        order.getId(), line.getSku(), ex.getMessage());
            }
        }
    }

    private void appendOutbox(Order order, String type, Object event) {
        try {
            outboxRepository.save(new OutboxMessage(order.getId(), type, objectMapper.writeValueAsString(event)));
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("Could not serialize outbox payload for order " + order.getId(), ex);
        }
    }

    private List<OrderItemLine> toItemLines(Order order) {
        return order.getItems().stream()
                .map(line -> new OrderItemLine(line.getSku(), line.getQuantity(), line.getUnitPrice()))
                .toList();
    }
}
