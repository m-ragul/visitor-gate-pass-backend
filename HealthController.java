package com.security.visitor_gate_pass.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/")
    public String home() {
        return "Visitor Gate Pass Backend Running";
    }

    @GetMapping("/health")
    public String health() {
        return "OK";
    }
}
