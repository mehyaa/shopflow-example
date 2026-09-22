package com.shopflow.product.infra;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// Day 3: declarative client — the interface is the whole contract; the proxy
// is generated at runtime, "inventory-service" is resolved via Eureka + LB
// (SPEC §3, Day 3 lab step 6)
@FeignClient(name = "inventory-service", contextId = "inventoryClient", path = "/api/inventory")
public interface InventoryClient {

    @GetMapping("/{sku}")
    StockResponse getStatus(@PathVariable("sku") String sku);

    // DTO nested here: the contract lives in one place
    record StockResponse(String sku, boolean available, int quantity) {
    }
}
