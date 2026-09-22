package com.shopflow.product.app;

import com.shopflow.product.api.dto.ProductRequest;
import com.shopflow.product.domain.Money;
import com.shopflow.product.domain.Product;
import com.shopflow.product.domain.ProductRepository;
import com.shopflow.product.infra.InventoryClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// Application service: orchestration only (transaction, repo calls, SKU uniqueness
// check); business rules live in the aggregate.
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final InventoryClient inventoryClient;

    public ProductService(ProductRepository productRepository, InventoryClient inventoryClient) {
        this.productRepository = productRepository;
        this.inventoryClient = inventoryClient;
    }

    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Transactional(readOnly = true)
    public long count() {
        return productRepository.count();
    }

    @Transactional(readOnly = true)
    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Transactional
    public Product create(ProductRequest request) {
        if (productRepository.existsBySku(request.sku())) {
            throw new IllegalArgumentException("SKU already exists: " + request.sku());
        }
        // DTO → domain: the client never sees the domain's inside
        Product product = Product.create(request.sku(), request.name(),
                new Money(request.price()), request.description());
        return productRepository.save(product);
    }

    // Day 3: first inter-service sync chain — product → Feign → inventory
    public InventoryClient.StockResponse checkStock(String sku) {
        return inventoryClient.getStatus(sku);
    }
}
