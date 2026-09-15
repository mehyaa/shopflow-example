package com.shopflow.product.infra;

import com.shopflow.product.domain.Product;
import com.shopflow.product.domain.ProductRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// @Repository: fulfills the domain interface over JPA.
@Repository
public class JpaProductRepository implements ProductRepository {

    private final SpringDataProductJpa springDataProductJpa;

    public JpaProductRepository(SpringDataProductJpa springDataProductJpa) {
        this.springDataProductJpa = springDataProductJpa;
    }

    @Override
    public Product save(Product product) {
        ProductJpa saved = springDataProductJpa.save(ProductMapper.toJpa(product));
        return ProductMapper.toDomain(saved);
    }

    @Override
    public List<Product> findAll() {
        return springDataProductJpa.findAll().stream()
                .map(ProductMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Product> findById(Long id) {
        return springDataProductJpa.findById(id).map(ProductMapper::toDomain);
    }

    @Override
    public long count() {
        return springDataProductJpa.count();
    }

    @Override
    public boolean existsBySku(String sku) {
        return springDataProductJpa.existsBySku(sku);
    }
}
