package com.shopflow.order.infra;

import com.shopflow.order.api.dto.ReleaseRequest;
import com.shopflow.order.api.dto.ReleaseResponse;
import com.shopflow.order.api.dto.ReserveRequest;
import com.shopflow.order.api.dto.ReserveResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

// Day 4: Feign client resolved via Eureka (SPEC §5, saga step 2)
@FeignClient(name = "inventory-service", contextId = "inventoryClient", path = "/api/inventory")
public interface InventoryClient {

    @PostMapping("/{sku}/reserve")
    ReserveResponse reserve(@PathVariable("sku") String sku, @RequestBody ReserveRequest request);

    @PostMapping("/{sku}/release")
    ReleaseResponse release(@PathVariable("sku") String sku, @RequestBody ReleaseRequest request);
}
