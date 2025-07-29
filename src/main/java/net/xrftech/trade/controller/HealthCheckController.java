package net.xrftech.trade.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/health")
@AllArgsConstructor
@Slf4j
public class HealthCheckController {

    @GetMapping("/app")
    public ResponseEntity<Map<String, Object>> checkAppHealth() {
        log.info("Health check endpoint called");
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("message", "Java application is running");
        response.put("timestamp", LocalDateTime.now());
        response.put("service", "trade");
        
        log.info("Health check completed successfully: {}", response);
        return ResponseEntity.ok(response);
    }

} 