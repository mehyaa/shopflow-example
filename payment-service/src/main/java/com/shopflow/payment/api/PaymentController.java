package com.shopflow.payment.api;

import com.shopflow.payment.api.dto.PaymentRequest;
import com.shopflow.payment.api.dto.PaymentResponse;
import com.shopflow.payment.app.PaymentService;
import com.shopflow.payment.domain.Money;
import com.shopflow.payment.domain.Payment;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Day 4: REST contract — SPEC §3 (note: FAILED is still a 200 response;
// the saga learns about it from the status field and the payment.failed event)
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public PaymentResponse pay(@Valid @RequestBody PaymentRequest request) {
        // DTO → domain: BigDecimal in the DTO, Money in the aggregate
        Payment payment = paymentService.pay(request.orderId(), new Money(request.amount()));
        return new PaymentResponse(payment.getPaymentId(), payment.getOrderId(), payment.getStatus().name());
    }
}
