package com.shopflow.inventory;

import com.shopflow.inventory.app.InventoryService;
import com.shopflow.inventory.domain.StockItem;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InventoryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryServiceApplication.class, args);
    }

    // Day 3: seed data — POSTER-003 has zero stock on purpose (insufficient-stock saga demo)
    @Bean
    CommandLineRunner seedStock(InventoryService inventoryService) {
        return args -> {
            if (inventoryService.count() == 0) {
                inventoryService.create(StockItem.create("TSHIRT-001", 100));
                inventoryService.create(StockItem.create("MUG-002", 50));
                inventoryService.create(StockItem.create("POSTER-003", 0));
            }
        };
    }
}
