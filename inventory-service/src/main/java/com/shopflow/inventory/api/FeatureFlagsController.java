package com.shopflow.inventory.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

// Day 3: the simplest refresh demo — refreshes re-instantiate @RefreshScope
// beans, so the value comes back changed without a restart (icerik/05 §3.5)
@RestController
@RequestMapping("/api/inventory")
@RefreshScope
public class FeatureFlagsController {

    @Value("${shopflow.feature.free-shipping:false}")
    private boolean freeShipping;

    @GetMapping("/flags")
    public Map<String, Boolean> flags() {
        return Map.of("freeShipping", freeShipping);
    }
}
