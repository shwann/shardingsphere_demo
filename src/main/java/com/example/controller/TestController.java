package com.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/hello")
    public Map<String, Object> hello() {
        Map<String, Object> result = new HashMap<>();
        result.put("message", "Hello from ShardingSphere Demo!");
        result.put("status", "success");
        result.put("timestamp", System.currentTimeMillis());
        return result;
    }

    @GetMapping("/info")
    public Map<String, Object> info() {
        Map<String, Object> result = new HashMap<>();
        result.put("application", "ShardingSphere Demo");
        result.put("version", "1.0.0");
        result.put("description", "A demo project for ShardingSphere");
        result.put("status", "running");
        return result;
    }
}
