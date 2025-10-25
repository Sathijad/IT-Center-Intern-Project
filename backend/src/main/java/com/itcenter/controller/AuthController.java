package com.itcenter.controller;

import com.itcenter.dto.LoginRequestDto;
import com.itcenter.dto.LoginResponseDto;
import com.itcenter.dto.RegisterRequestDto;
import com.itcenter.dto.RegisterResponseDto;
import com.itcenter.dto.MfaVerificationRequestDto;
import com.itcenter.dto.MfaVerificationResponseDto;
import com.itcenter.dto.ForgotPasswordRequestDto;
import com.itcenter.dto.ForgotPasswordResponseDto;
import com.itcenter.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST controller for authentication operations
 */
@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Authentication endpoints")
public class AuthController {
    
    private final AuthService authService;
    
    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    
    @PostMapping("/login")
    @Operation(summary = "User login", 
               description = "Authenticate user with email and password")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login successful"),
        @ApiResponse(responseCode = "401", description = "Invalid credentials"),
        @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto request) {
        LoginResponseDto response = authService.login(request);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/register")
    @Operation(summary = "User registration", 
               description = "Register a new user account")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Registration successful"),
        @ApiResponse(responseCode = "400", description = "Invalid request or user already exists"),
        @ApiResponse(responseCode = "409", description = "User already exists")
    })
    public ResponseEntity<RegisterResponseDto> register(@Valid @RequestBody RegisterRequestDto request) {
        RegisterResponseDto response = authService.register(request);
        return ResponseEntity.status(201).body(response);
    }
    
    @PostMapping("/verify-mfa")
    @Operation(summary = "Verify MFA code", 
               description = "Verify multi-factor authentication code")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "MFA verification successful"),
        @ApiResponse(responseCode = "401", description = "Invalid MFA code"),
        @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    public ResponseEntity<MfaVerificationResponseDto> verifyMfa(@Valid @RequestBody MfaVerificationRequestDto request) {
        MfaVerificationResponseDto response = authService.verifyMfa(request);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/forgot-password")
    @Operation(summary = "Forgot password", 
               description = "Initiate password reset process")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Password reset instructions sent"),
        @ApiResponse(responseCode = "400", description = "Invalid request"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<ForgotPasswordResponseDto> forgotPassword(@Valid @RequestBody ForgotPasswordRequestDto request) {
        ForgotPasswordResponseDto response = authService.forgotPassword(request);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/cognito/callback")
    @Operation(summary = "Cognito callback", 
               description = "Handle Cognito authentication callback")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Authentication successful"),
        @ApiResponse(responseCode = "400", description = "Invalid request"),
        @ApiResponse(responseCode = "401", description = "Authentication failed")
    })
    public ResponseEntity<Map<String, Object>> cognitoCallback(@RequestBody Map<String, String> request) {
        String code = request.get("code");
        String redirectUri = request.get("redirectUri");
        
        if (code == null || redirectUri == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Missing code or redirectUri"));
        }
        
        try {
            Map<String, Object> response = authService.handleCognitoCallback(code, redirectUri);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
