package com.itcenter.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * DTO for updating user profile
 */
public class UpdateUserProfileDto {
    
    @NotBlank(message = "Display name is required")
    @Size(min = 2, max = 50, message = "Display name must be between 2 and 50 characters")
    private String displayName;
    
    @Size(max = 10, message = "Locale must be at most 10 characters")
    private String locale;
    
    // Constructors
    public UpdateUserProfileDto() {}
    
    public UpdateUserProfileDto(String displayName, String locale) {
        this.displayName = displayName;
        this.locale = locale;
    }
    
    // Getters and Setters
    public String getDisplayName() {
        return displayName;
    }
    
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    
    public String getLocale() {
        return locale;
    }
    
    public void setLocale(String locale) {
        this.locale = locale;
    }
    
    @Override
    public String toString() {
        return "UpdateUserProfileDto{" +
                "displayName='" + displayName + '\'' +
                ", locale='" + locale + '\'' +
                '}';
    }
}
