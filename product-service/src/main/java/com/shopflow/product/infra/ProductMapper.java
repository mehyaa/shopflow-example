package com.shopflow.product.infra;

import com.shopflow.product.domain.Money;
import com.shopflow.product.domain.Product;

// Domain ↔ JPA mapping — the domain layer stays unaware of Spring/JPA.
public class ProductMapper {

    private ProductMapper() {
    }

    public static Product toDomain(ProductJpa jpa) {
        return Product.reconstitute(jpa.getId(), jpa.getSku(), jpa.getName(),
                new Money(jpa.getPrice()), jpa.getDescription());
    }

    public static ProductJpa toJpa(Product product) {
        // new aggregate: id stays null, JPA generates it; the mapper carries it back
        return new ProductJpa(product.getId(), product.getSku(), product.getName(),
                product.getPrice().asBigDecimal(), product.getDescription());
    }
}
