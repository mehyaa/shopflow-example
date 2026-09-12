package com.shopflow.order.api;

import com.shopflow.order.api.dto.CreateOrderRequest;
import com.shopflow.order.api.dto.OrderCreatedResponse;
import com.shopflow.order.api.dto.OrderResponse;
import com.shopflow.order.api.dto.OrderSummaryResponse;
import com.shopflow.order.app.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

// Day 4: REST contract — SPEC §3
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public OrderCreatedResponse create(@Valid @RequestBody CreateOrderRequest request) {
        return orderService.createOrder(request);
    }

    @GetMapping("/{id}")
    public OrderResponse getOrder(@PathVariable UUID id) {
        return orderService.getOrder(id);
    }

    @GetMapping
    public List<OrderSummaryResponse> listOrders() {
        return orderService.listOrders();
    }

    // Day 4: saga compensation demo endpoint
    @PostMapping("/{id}/cancel")
    public OrderCreatedResponse cancel(@PathVariable UUID id) {
        return orderService.cancelOrder(id, "cancelled by user", true);
    }
}
