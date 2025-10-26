package com.itcenter.controller;

import com.itcenter.dto.*;
import com.itcenter.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Authentication endpoints")
public class AuthController {

    private final AuthService authService;
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticate user with email and password")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Login successful"),
        @ApiResponse(responseCode = "401", description = "Invalid credentials"),
        @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    @Operation(summary = "User registration", description = "Register a new user account")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Registration successful"),
        @ApiResponse(responseCode = "400", description = "Invalid request or user already exists"),
        @ApiResponse(responseCode = "409", description = "User already exists")
    })
    public ResponseEntity<RegisterResponseDto> register(@Valid @RequestBody RegisterRequestDto request) {
        return ResponseEntity.status(201).body(authService.register(request));
    }

    @PostMapping("/verify-mfa")
    @Operation(summary = "Verify MFA code", description = "Verify multi-factor authentication code")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "MFA verification successful"),
        @ApiResponse(responseCode = "401", description = "Invalid MFA code"),
        @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    public ResponseEntity<MfaVerificationResponseDto> verifyMfa(@Valid @RequestBody MfaVerificationRequestDto request) {
        return ResponseEntity.ok(authService.verifyMfa(request));
    }

    @PostMapping("/forgot-password")
    @Operation(summary = "Forgot password", description = "Initiate password reset process")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Password reset instructions sent"),
        @ApiResponse(responseCode = "400", description = "Invalid request"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<ForgotPasswordResponseDto> forgotPassword(@Valid @RequestBody ForgotPasswordRequestDto request) {
        return ResponseEntity.ok(authService.forgotPassword(request));
    }
}
