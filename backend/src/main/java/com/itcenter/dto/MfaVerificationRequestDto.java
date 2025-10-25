package com.itcenter.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * MFA verification request DTO
 */
public class MfaVerificationRequestDto {
    
    @NotBlank(message = "MFA code is required")
    @Pattern(regexp = "^\\d{6}$", message = "MFA code must be 6 digits")
    @Schema(description = "6-digit MFA code", example = "123456")
    private String code;
    
    // Constructors
    public MfaVerificationRequestDto() {}
    
    public MfaVerificationRequestDto(String code) {
        this.code = code;
    }
    
    // Getters and Setters
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
}
