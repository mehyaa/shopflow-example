package com.shopflow.product;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

// Day 2: minimal context-load test (SPEC §10)
@SpringBootTest
@ActiveProfiles("test")
class ProductServiceApplicationTests {

    @Test
    void contextLoads() {
    }
}
