package com.itcenter.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Forgot password response DTO
 */
public class ForgotPasswordResponseDto {
    
    @Schema(description = "Success message", example = "Password reset instructions sent to your email.")
    private String message;
    
    // Constructors
    public ForgotPasswordResponseDto() {}
    
    public ForgotPasswordResponseDto(String message) {
        this.message = message;
    }
    
    // Getters and Setters
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
}
