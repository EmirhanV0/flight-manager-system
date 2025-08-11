package com.uys.reference.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Test Controller for verifying application startup
 */
@RestController
public class TestController {
    
    @GetMapping("/test")
    public String test() {
        return "Application is running!";
    }
    
    @GetMapping("/health")
    public String health() {
        return "OK";
    }
} 