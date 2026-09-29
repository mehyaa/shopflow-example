package com.shopflow.order.infra;

import java.math.BigDecimal;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;

// Day 4+: saga fiyat zenginleştirme — request'te unitPrice yoksa katalog fiyatı kullanılır
// (production flow: OrderService.createOrder içindeki yorumla uyumlu)
@FeignClient(name = "product-service", contextId = "orderProductClient", path = "/api/products")
public interface ProductClient {

    @org.springframework.web.bind.annotation.GetMapping("/sku/{sku}")
    PriceResponse getPrice(@PathVariable("sku") String sku);

    record PriceResponse(Long id, String sku, String name, BigDecimal price, String description) {
    }
}
