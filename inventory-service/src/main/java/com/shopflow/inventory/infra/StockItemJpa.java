package com.shopflow.inventory.infra;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// JPA counterpart: the persistence view of the domain aggregate — the mapper
// turns it into the domain StockItem.
// Identity: sku (same natural identity as the domain aggregate).
@Entity
@Table(name = "inventory_items")
public class StockItemJpa {

    @Id
    @Column(nullable = false, unique = true, length = 64)
    private String sku;

    @Column(nullable = false)
    private int quantity;

    protected StockItemJpa() {
        // required by JPA
    }

    public StockItemJpa(String sku, int quantity) {
        this.sku = sku;
        this.quantity = quantity;
    }

    public String getSku() {
        return sku;
    }

    public int getQuantity() {
        return quantity;
    }
}
