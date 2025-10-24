package com.itcenter.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Set;

/**
 * DTO for updating user roles
 */
public class UpdateUserRolesDto {
    
    @NotBlank(message = "At least one role is required")
    private Set<String> roles;
    
    // Constructors
    public UpdateUserRolesDto() {}
    
    public UpdateUserRolesDto(Set<String> roles) {
        this.roles = roles;
    }
    
    // Getters and Setters
    public Set<String> getRoles() {
        return roles;
    }
    
    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
    
    @Override
    public String toString() {
        return "UpdateUserRolesDto{" +
                "roles=" + roles +
                '}';
    }
}
