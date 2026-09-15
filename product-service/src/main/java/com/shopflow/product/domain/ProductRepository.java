package com.shopflow.product.domain;

import java.util.List;
import java.util.Optional;

// The domain is persistence-agnostic. The implementation lives in infra (JPA).
public interface ProductRepository {

    Product save(Product product);

    List<Product> findAll();

    Optional<Product> findById(Long id);

    long count();

    boolean existsBySku(String sku);
}
