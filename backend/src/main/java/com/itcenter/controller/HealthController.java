package com.itcenter.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST controller for health check and system status
 */
@RestController
@RequestMapping("/healthz")
@Tag(name = "Health Check", description = "System health and status endpoints")
public class HealthController {
    
    @GetMapping
    @Operation(summary = "Health check", 
               description = "Check if the service is running and healthy")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Service is healthy"),
        @ApiResponse(responseCode = "503", description = "Service is unhealthy")
    })
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> health = Map.of(
            "status", "UP",
            "timestamp", java.time.Instant.now(),
            "service", "IT Center Staff Auth API",
            "version", "1.0.0"
        );
        
        return ResponseEntity.ok(health);
    }
}
