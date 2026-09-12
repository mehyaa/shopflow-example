package com.shopflow.inventory.api;

import com.shopflow.inventory.api.dto.InventoryStatusResponse;
import com.shopflow.inventory.api.dto.ReleaseRequest;
import com.shopflow.inventory.api.dto.ReleaseResponse;
import com.shopflow.inventory.api.dto.ReserveRequest;
import com.shopflow.inventory.api.dto.ReserveResponse;
import com.shopflow.inventory.app.InventoryService;
import com.shopflow.inventory.domain.StockItem;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Day 3: REST contract — SPEC §3
@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/{sku}")
    public InventoryStatusResponse getStatus(@PathVariable String sku) {
        StockItem item = inventoryService.getStatus(sku);
        return new InventoryStatusResponse(item.getSku(), item.getQuantity() > 0, item.getQuantity());
    }

    @PostMapping("/{sku}/reserve")
    public ReserveResponse reserve(@PathVariable String sku, @Valid @RequestBody ReserveRequest request) {
        StockItem item = inventoryService.reserve(sku, request.quantity());
        return new ReserveResponse(item.getSku(), request.quantity());
    }

    @PostMapping("/{sku}/release")
    public ReleaseResponse release(@PathVariable String sku, @Valid @RequestBody ReleaseRequest request) {
        StockItem item = inventoryService.release(sku, request.quantity());
        return new ReleaseResponse(item.getSku(), request.quantity());
    }
}
