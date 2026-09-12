package com.shopflow.inventory;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

// Day 3: minimal context-load test (SPEC §10)
@SpringBootTest
@ActiveProfiles("test")
class InventoryServiceApplicationTests {

    @Test
    void contextLoads() {
    }
}
