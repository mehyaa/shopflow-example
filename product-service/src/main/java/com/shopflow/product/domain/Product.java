package com.shopflow.product.domain;

// Day 2: Product aggregate — identity + state + behavior.
// No setters: changes only through rule-enforcing behavior methods.
public class Product {

    private Long id;
    private final String sku;
    private String name;
    private Money price;
    private String description;

    Product(Long id, String sku, String name, Money price, String description) {
        this.id = id;
        this.sku = sku;
        this.name = name;
        this.price = price;
        this.description = description;
    }

    // The aggregate is born — the factory applies validation at birth
    public static Product create(String sku, String name, Money price, String description) {
        validate(sku, name, price);
        return new Product(null, sku, name, price, description);
    }

    // Rehydration from persistence — mapper only
    public static Product reconstitute(Long id, String sku, String name, Money price, String description) {
        return new Product(id, sku, name, price, description);
    }

    public void updatePrice(Money newPrice) {
        if (newPrice == null) {
            throw new IllegalArgumentException("price is required");
        }
        this.price = newPrice;
    }

    public void rename(String newName) {
        if (newName == null || newName.isBlank()) {
            throw new IllegalArgumentException("name is required");
        }
        this.name = newName;
    }

    public void updateDescription(String newDescription) {
        this.description = newDescription;
    }

    private static void validate(String sku, String name, Money price) {
        if (sku == null || sku.isBlank()) {
            throw new IllegalArgumentException("sku is required");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name is required");
        }
        if (price == null) {
            throw new IllegalArgumentException("price is required");
        }
    }

    public Long getId() {
        return id;
    }

    public String getSku() {
        return sku;
    }

    public String getName() {
        return name;
    }

    public Money getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }
}
