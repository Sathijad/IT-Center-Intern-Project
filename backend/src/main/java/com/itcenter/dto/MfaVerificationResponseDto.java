package com.itcenter.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * MFA verification response DTO
 */
public class MfaVerificationResponseDto {
    
    @Schema(description = "JWT access token", example = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;
    
    @Schema(description = "Success message", example = "MFA verification successful")
    private String message;
    
    // Constructors
    public MfaVerificationResponseDto() {}
    
    public MfaVerificationResponseDto(String token, String message) {
        this.token = token;
        this.message = message;
    }
    
    // Getters and Setters
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
}
