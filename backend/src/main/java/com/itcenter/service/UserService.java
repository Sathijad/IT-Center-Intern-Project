package com.itcenter.service;

import com.itcenter.dto.*;
import com.itcenter.entity.AppUser;
import com.itcenter.entity.LoginAudit;
import com.itcenter.entity.Role;
import com.itcenter.entity.UserRole;
import com.itcenter.mapper.UserMapper;
import com.itcenter.repository.AppUserRepository;
import com.itcenter.repository.LoginAuditRepository;
import com.itcenter.repository.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service for user profile management
 */
@Service
@Transactional
public class UserService {
    
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    
    private final AppUserRepository userRepository;
    private final RoleRepository roleRepository;
    private final LoginAuditRepository auditRepository;
    private final UserMapper userMapper;
    private final AuditService auditService;
    
    public UserService(AppUserRepository userRepository,
                      RoleRepository roleRepository,
                      LoginAuditRepository auditRepository,
                      UserMapper userMapper,
                      AuditService auditService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.auditRepository = auditRepository;
        this.userMapper = userMapper;
        this.auditService = auditService;
    }
    
    /**
     * Get current user's profile
     */
    @Transactional(readOnly = true)
    public UserProfileDto getCurrentUserProfile() {
        String userId = getCurrentUserId();
        AppUser user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found: " + userId));
        
        logger.info("Retrieved profile for user: {}", userId);
        return userMapper.toUserProfileDto(user);
    }
    
    /**
     * Update current user's profile
     */
    public UserProfileDto updateCurrentUserProfile(UpdateUserProfileDto updateDto) {
        String userId = getCurrentUserId();
        AppUser user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found: " + userId));
        
        // Update fields
        user.setDisplayName(updateDto.getDisplayName());
        if (updateDto.getLocale() != null) {
            user.setLocale(updateDto.getLocale());
        }
        
        AppUser savedUser = userRepository.save(user);
        
        // Log the profile update
        auditService.logEvent(userId, LoginAudit.EventType.PROFILE_UPDATED, 
            getClientIpAddress(), getCurrentUserAgent(), true, null);
        
        logger.info("Updated profile for user: {}", userId);
        return userMapper.toUserProfileDto(savedUser);
    }
    
    /**
     * Search users with pagination (Admin only)
     */
    @Transactional(readOnly = true)
    public PageResponseDto<UserManagementDto> searchUsers(String query, Pageable pageable) {
        Page<AppUser> users;
        
        if (query == null || query.trim().isEmpty()) {
            users = userRepository.findAll(pageable);
        } else {
            users = userRepository.searchUsers(query.trim(), pageable);
        }
        
        List<UserManagementDto> userDtos = users.getContent().stream()
            .map(userMapper::toUserManagementDto)
            .collect(Collectors.toList());
        
        logger.info("Searched users with query: '{}', found {} results", query, users.getTotalElements());
        return userMapper.toPageResponseDto(users, userDtos);
    }
    
    /**
     * Get user by ID (Admin only)
     */
    @Transactional(readOnly = true)
    public UserManagementDto getUserById(String userId) {
        AppUser user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found: " + userId));
        
        logger.info("Retrieved user details for: {}", userId);
        return userMapper.toUserManagementDto(user);
    }
    
    /**
     * Update user roles (Admin only)
     */
    public UserManagementDto updateUserRoles(String userId, UpdateUserRolesDto updateDto) {
        AppUser user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found: " + userId));
        
        // Clear existing roles
        user.getUserRoles().clear();
        
        // Add new roles
        for (String roleName : updateDto.getRoles()) {
            Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
            user.addRole(role);
        }
        
        AppUser savedUser = userRepository.save(user);
        
        // Log role changes
        String currentUserId = getCurrentUserId();
        auditService.logEvent(currentUserId, LoginAudit.EventType.ROLE_ASSIGNED, 
            getClientIpAddress(), getCurrentUserAgent(), true, 
            "Updated roles for user: " + userId);
        
        logger.info("Updated roles for user: {} by admin: {}", userId, currentUserId);
        return userMapper.toUserManagementDto(savedUser);
    }
    
    /**
     * Get current user ID from security context
     */
    private String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }
        
        // Extract user ID from JWT token
        Object principal = authentication.getPrincipal();
        if (principal instanceof org.springframework.security.oauth2.jwt.Jwt) {
            org.springframework.security.oauth2.jwt.Jwt jwt = (org.springframework.security.oauth2.jwt.Jwt) principal;
            return jwt.getSubject();
        }
        
        throw new RuntimeException("Unable to extract user ID from authentication");
    }
    
    /**
     * Get client IP address (simplified implementation)
     */
    private String getClientIpAddress() {
        // In a real implementation, this would extract IP from HttpServletRequest
        return "127.0.0.1";
    }
    
    /**
     * Get current user agent (simplified implementation)
     */
    private String getCurrentUserAgent() {
        // In a real implementation, this would extract User-Agent from HttpServletRequest
        return "IT-Center-API/1.0";
    }
}
