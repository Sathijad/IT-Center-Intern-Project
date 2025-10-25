package com.itcenter.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Login response DTO
 */
public class LoginResponseDto {
    
    @Schema(description = "JWT access token", example = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;
    
    @Schema(description = "Whether MFA is required", example = "false")
    private boolean needsMfa;
    
    @Schema(description = "Success message", example = "Login successful")
    private String message;
    
    // Constructors
    public LoginResponseDto() {}
    
    public LoginResponseDto(String token, boolean needsMfa, String message) {
        this.token = token;
        this.needsMfa = needsMfa;
        this.message = message;
    }
    
    // Getters and Setters
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public boolean isNeedsMfa() {
        return needsMfa;
    }
    
    public void setNeedsMfa(boolean needsMfa) {
        this.needsMfa = needsMfa;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
}
