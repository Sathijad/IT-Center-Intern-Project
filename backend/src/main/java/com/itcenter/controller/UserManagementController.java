package com.itcenter.controller;

import com.itcenter.dto.*;
import com.itcenter.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for admin user management
 */
@RestController
@RequestMapping("/admin/users")
@Tag(name = "User Management", description = "Admin user management endpoints")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasRole('ADMIN')")
public class UserManagementController {
    
    private final UserService userService;
    
    public UserManagementController(UserService userService) {
        this.userService = userService;
    }
    
    @GetMapping
    @Operation(summary = "Search users", 
               description = "Search and paginate users with optional query filter")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Users retrieved successfully",
                    content = @Content(schema = @Schema(implementation = PageResponseDto.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Admin access required")
    })
    public ResponseEntity<PageResponseDto<UserManagementDto>> searchUsers(
            @Parameter(description = "Search query for name or email") 
            @RequestParam(required = false) String query,
            @Parameter(description = "Page number (0-based)") 
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") 
            @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field") 
            @RequestParam(defaultValue = "createdAt") String sort,
            @Parameter(description = "Sort direction") 
            @RequestParam(defaultValue = "desc") String direction) {
        
        Sort.Direction sortDirection = "asc".equalsIgnoreCase(direction) ? 
            Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));
        
        PageResponseDto<UserManagementDto> result = userService.searchUsers(query, pageable);
        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/{userId}")
    @Operation(summary = "Get user by ID", 
               description = "Retrieve detailed information about a specific user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User retrieved successfully",
                    content = @Content(schema = @Schema(implementation = UserManagementDto.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Admin access required"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserManagementDto> getUserById(
            @Parameter(description = "User ID") 
            @PathVariable String userId) {
        UserManagementDto user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }
    
    @PatchMapping("/{userId}/roles")
    @Operation(summary = "Update user roles", 
               description = "Assign or remove roles for a specific user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Roles updated successfully",
                    content = @Content(schema = @Schema(implementation = UserManagementDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Admin access required"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserManagementDto> updateUserRoles(
            @Parameter(description = "User ID") 
            @PathVariable String userId,
            @Valid @RequestBody UpdateUserRolesDto updateDto) {
        UserManagementDto updatedUser = userService.updateUserRoles(userId, updateDto);
        return ResponseEntity.ok(updatedUser);
    }
}
