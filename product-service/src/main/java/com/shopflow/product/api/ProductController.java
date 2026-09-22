package com.shopflow.product.api;

import com.shopflow.product.api.dto.ProductRequest;
import com.shopflow.product.api.dto.ProductResponse;
import com.shopflow.product.app.ProductService;
import com.shopflow.product.infra.InventoryClient;
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

// Day 2: REST contract — SPEC §3
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<ProductResponse> findAll() {
        return productService.findAll().stream().map(ProductResponse::from).toList();
    }

    @GetMapping("/{id}")
    public ProductResponse findById(@PathVariable Long id) {
        return ProductResponse.from(productService.findById(id));
    }

    // Day 3: sync chain through the gateway — gateway → product → Feign → inventory
    @GetMapping("/{sku}/stock")
    public InventoryClient.StockResponse checkStock(@PathVariable String sku) {
        return productService.checkStock(sku);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse create(@Valid @RequestBody ProductRequest request) {
        // DTO in, DTO out — the domain never leaks into the api layer
        return ProductResponse.from(productService.create(request));
    }
}
