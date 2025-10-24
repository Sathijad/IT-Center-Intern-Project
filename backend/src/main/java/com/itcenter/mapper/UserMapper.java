package com.itcenter.mapper;

import com.itcenter.dto.*;
import com.itcenter.entity.AppUser;
import com.itcenter.entity.LoginAudit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * MapStruct mapper for converting between entities and DTOs
 */
@Mapper(componentModel = "spring")
public interface UserMapper {
    
    /**
     * Convert AppUser entity to UserProfileDto
     */
    @Mapping(target = "roles", source = "userRoles", qualifiedByName = "userRolesToRoleNames")
    UserProfileDto toUserProfileDto(AppUser user);
    
    /**
     * Convert AppUser entity to UserManagementDto
     */
    @Mapping(target = "roles", source = "userRoles", qualifiedByName = "userRolesToRoleNames")
    @Mapping(target = "lastLoginAt", source = "loginAudits", qualifiedByName = "getLastLoginAt")
    @Mapping(target = "active", constant = "true")
    UserManagementDto toUserManagementDto(AppUser user);
    
    /**
     * Convert LoginAudit entity to AuditLogDto
     */
    @Mapping(target = "userEmail", source = "user.email")
    @Mapping(target = "userDisplayName", source = "user.displayName")
    AuditLogDto toAuditLogDto(LoginAudit audit);
    
    /**
     * Convert Page of entities to PageResponseDto
     */
    default <T, U> PageResponseDto<U> toPageResponseDto(Page<T> page, List<U> content) {
        return new PageResponseDto<>(
            content,
            page.getNumber(),
            page.getSize(),
            page.getTotalElements(),
            page.getTotalPages()
        );
    }
    
    /**
     * Convert Set of UserRole to Set of role names
     */
    @Named("userRolesToRoleNames")
    default Set<String> userRolesToRoleNames(Set<com.itcenter.entity.UserRole> userRoles) {
        if (userRoles == null) {
            return Set.of();
        }
        return userRoles.stream()
            .map(ur -> ur.getRole().getName())
            .collect(Collectors.toSet());
    }
    
    /**
     * Get last login time from login audits
     */
    @Named("getLastLoginAt")
    default java.time.LocalDateTime getLastLoginAt(Set<LoginAudit> loginAudits) {
        if (loginAudits == null || loginAudits.isEmpty()) {
            return null;
        }
        return loginAudits.stream()
            .filter(audit -> "LOGIN".equals(audit.getEventType()) && Boolean.TRUE.equals(audit.getSuccess()))
            .map(LoginAudit::getCreatedAt)
            .max(java.time.LocalDateTime::compareTo)
            .orElse(null);
    }
}
