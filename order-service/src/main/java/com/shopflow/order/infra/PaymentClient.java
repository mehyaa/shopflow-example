package com.shopflow.order.infra;

import com.shopflow.order.api.dto.PaymentRequest;
import com.shopflow.order.api.dto.PaymentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

// Day 4: Feign client resolved via Eureka (SPEC §5, saga step 4 —
// wrapped with CircuitBreaker + Retry in PaymentInvoker)
@FeignClient(name = "payment-service", contextId = "paymentClient")
public interface PaymentClient {

    @PostMapping("/api/payments")
    PaymentResponse pay(@RequestBody PaymentRequest request);
}
