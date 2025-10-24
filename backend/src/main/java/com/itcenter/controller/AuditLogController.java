package com.itcenter.controller;

import com.itcenter.dto.AuditLogDto;
import com.itcenter.dto.PageResponseDto;
import com.itcenter.service.AuditService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * REST controller for audit log management
 */
@RestController
@RequestMapping("/admin/audit-log")
@Tag(name = "Audit Log", description = "Audit log management endpoints")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasRole('ADMIN')")
public class AuditLogController {
    
    private final AuditService auditService;
    
    public AuditLogController(AuditService auditService) {
        this.auditService = auditService;
    }
    
    @GetMapping
    @Operation(summary = "Get audit logs", 
               description = "Retrieve audit logs with optional filtering by user, event type, and date range")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Audit logs retrieved successfully",
                    content = @Content(schema = @Schema(implementation = PageResponseDto.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Admin access required")
    })
    public ResponseEntity<PageResponseDto<AuditLogDto>> getAuditLogs(
            @Parameter(description = "Filter by user ID") 
            @RequestParam(required = false) String user_id,
            @Parameter(description = "Filter by event type") 
            @RequestParam(required = false) String event_type,
            @Parameter(description = "Start date for range filter") 
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start_date,
            @Parameter(description = "End date for range filter") 
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end_date,
            @Parameter(description = "Page number (0-based)") 
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") 
            @RequestParam(defaultValue = "20") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        
        PageResponseDto<AuditLogDto> result = auditService.getAuditLogs(
            user_id, event_type, start_date, end_date, pageable);
        
        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/user/{userId}")
    @Operation(summary = "Get user audit logs", 
               description = "Retrieve audit logs for a specific user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User audit logs retrieved successfully",
                    content = @Content(schema = @Schema(implementation = PageResponseDto.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Admin access required"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<PageResponseDto<AuditLogDto>> getUserAuditLogs(
            @Parameter(description = "User ID") 
            @PathVariable String userId,
            @Parameter(description = "Page number (0-based)") 
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") 
            @RequestParam(defaultValue = "20") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        
        PageResponseDto<AuditLogDto> result = auditService.getUserAuditLogs(userId, pageable);
        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/recent/{userId}")
    @Operation(summary = "Get recent logins", 
               description = "Get recent successful login attempts for a user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Recent logins retrieved successfully",
                    content = @Content(schema = @Schema(implementation = List.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Admin access required"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<List<AuditLogDto>> getRecentLogins(
            @Parameter(description = "User ID") 
            @PathVariable String userId,
            @Parameter(description = "Number of recent logins to retrieve") 
            @RequestParam(defaultValue = "10") int limit) {
        
        List<AuditLogDto> recentLogins = auditService.getRecentLogins(userId, limit);
        return ResponseEntity.ok(recentLogins);
    }
}
