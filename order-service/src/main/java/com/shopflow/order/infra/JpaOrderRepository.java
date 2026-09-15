package com.shopflow.order.infra;

import com.shopflow.order.domain.Order;
import com.shopflow.order.domain.OrderId;
import com.shopflow.order.domain.OrderRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// @Repository: fulfills the domain interface over JPA.
@Repository
public class JpaOrderRepository implements OrderRepository {

    private final SpringDataOrderJpa springDataOrderJpa;

    public JpaOrderRepository(SpringDataOrderJpa springDataOrderJpa) {
        this.springDataOrderJpa = springDataOrderJpa;
    }

    @Override
    public Order save(Order order) {
        OrderJpa saved = springDataOrderJpa.save(OrderMapper.toJpa(order));
        return OrderMapper.toDomain(saved);
    }

    @Override
    public Optional<Order> findById(OrderId id) {
        return springDataOrderJpa.findById(id.value()).map(OrderMapper::toDomain);
    }

    @Override
    public List<Order> findAllNewestFirst() {
        return springDataOrderJpa.findAllByOrderByCreatedAtDesc().stream()
                .map(OrderMapper::toDomain)
                .toList();
    }
}
