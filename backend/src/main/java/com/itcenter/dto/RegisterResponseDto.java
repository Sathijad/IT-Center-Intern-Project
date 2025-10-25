package com.itcenter.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Registration response DTO
 */
public class RegisterResponseDto {
    
    @Schema(description = "Success message", example = "Registration successful. Please check your email for verification.")
    private String message;
    
    // Constructors
    public RegisterResponseDto() {}
    
    public RegisterResponseDto(String message) {
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
