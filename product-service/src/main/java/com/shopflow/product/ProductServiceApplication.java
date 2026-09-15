package com.shopflow.product;

import com.shopflow.product.app.ProductService;
import com.shopflow.product.api.dto.ProductRequest;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;

@SpringBootApplication
public class ProductServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductServiceApplication.class, args);
    }

    // Day 2: seed data for the catalog demo — aggregates are born through create()
    @Bean
    CommandLineRunner seedProducts(ProductService productService) {
        return args -> {
            if (productService.count() == 0) {
                productService.create(new ProductRequest("TSHIRT-001", "ShopFlow Classic T-Shirt", new BigDecimal("19.90"), "Cotton t-shirt with ShopFlow logo"));
                productService.create(new ProductRequest("MUG-002", "Coffee Mug", new BigDecimal("9.90"), "350 ml ceramic mug"));
                productService.create(new ProductRequest("POSTER-003", "Microservices Poster", new BigDecimal("5.90"), "Saga pattern wall poster"));
                productService.create(new ProductRequest("STICKER-004", "Sticker Pack", new BigDecimal("3.90"), "Pack of 10 cloud stickers"));
            }
        };
    }
}
