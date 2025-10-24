package com.itcenter.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * DTO for audit log entries
 */
public class AuditLogDto {
    
    private Long id;
    
    private String userId;
    
    private String userEmail;
    
    private String userDisplayName;
    
    @NotBlank(message = "Event type is required")
    private String eventType;
    
    private String ipAddress;
    
    private String userAgent;
    
    private Boolean success;
    
    private String failureReason;
    
    private String sessionId;
    
    private LocalDateTime createdAt;
    
    // Constructors
    public AuditLogDto() {}
    
    public AuditLogDto(String userId, String eventType, String ipAddress) {
        this.userId = userId;
        this.eventType = eventType;
        this.ipAddress = ipAddress;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public String getUserEmail() {
        return userEmail;
    }
    
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
    
    public String getUserDisplayName() {
        return userDisplayName;
    }
    
    public void setUserDisplayName(String userDisplayName) {
        this.userDisplayName = userDisplayName;
    }
    
    public String getEventType() {
        return eventType;
    }
    
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
    
    public String getIpAddress() {
        return ipAddress;
    }
    
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
    
    public String getUserAgent() {
        return userAgent;
    }
    
    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
    
    public Boolean getSuccess() {
        return success;
    }
    
    public void setSuccess(Boolean success) {
        this.success = success;
    }
    
    public String getFailureReason() {
        return failureReason;
    }
    
    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }
    
    public String getSessionId() {
        return sessionId;
    }
    
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    @Override
    public String toString() {
        return "AuditLogDto{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", userDisplayName='" + userDisplayName + '\'' +
                ", eventType='" + eventType + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", success=" + success +
                ", createdAt=" + createdAt +
                '}';
    }
}
