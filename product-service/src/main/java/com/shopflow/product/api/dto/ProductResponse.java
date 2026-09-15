package com.shopflow.product.api.dto;

import com.shopflow.product.domain.Product;

import java.math.BigDecimal;

public record ProductResponse(Long id, String sku, String name, BigDecimal price, String description) {

    public static ProductResponse from(Product product) {
        return new ProductResponse(product.getId(), product.getSku(), product.getName(),
                product.getPrice().asBigDecimal(), product.getDescription());
    }
}
