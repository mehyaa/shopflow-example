package com.shopflow.order.domain;

import java.util.Optional;
import java.util.List;

// The domain is persistence-agnostic. The implementation lives in infra (JPA).
public interface OrderRepository {

    Order save(Order order);                // make the aggregate persistent (new or updated)

    Optional<Order> findById(OrderId id);   // look up by identity; Optional (no nulls)

    List<Order> findAllNewestFirst();       // read model: newest first (SPEC §3 list endpoint)
}
