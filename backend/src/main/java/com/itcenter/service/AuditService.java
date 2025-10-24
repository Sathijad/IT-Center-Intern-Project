package com.itcenter.service;

import com.itcenter.dto.AuditLogDto;
import com.itcenter.dto.PageResponseDto;
import com.itcenter.entity.AppUser;
import com.itcenter.entity.LoginAudit;
import com.itcenter.mapper.UserMapper;
import com.itcenter.repository.AppUserRepository;
import com.itcenter.repository.LoginAuditRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for audit logging and retrieval
 */
@Service
@Transactional
public class AuditService {
    
    private static final Logger logger = LoggerFactory.getLogger(AuditService.class);
    
    private final LoginAuditRepository auditRepository;
    private final AppUserRepository userRepository;
    private final UserMapper userMapper;
    
    public AuditService(LoginAuditRepository auditRepository,
                       AppUserRepository userRepository,
                       UserMapper userMapper) {
        this.auditRepository = auditRepository;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }
    
    /**
     * Log an audit event
     */
    public void logEvent(String userId, String eventType, String ipAddress, 
                        String userAgent, Boolean success, String failureReason) {
        AppUser user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found: " + userId));
        
        LoginAudit audit = new LoginAudit(user, eventType, ipAddress);
        audit.setUserAgent(userAgent);
        audit.setSuccess(success);
        audit.setFailureReason(failureReason);
        audit.setSessionId(generateSessionId());
        
        auditRepository.save(audit);
        
        logger.info("Logged audit event: {} for user: {} with success: {}", 
            eventType, userId, success);
    }
    
    /**
     * Get audit logs with pagination and filtering
     */
    @Transactional(readOnly = true)
    public PageResponseDto<AuditLogDto> getAuditLogs(String userId, String eventType, 
                                                    LocalDateTime startDate, LocalDateTime endDate, 
                                                    Pageable pageable) {
        Page<LoginAudit> audits;
        
        if (userId != null && eventType != null) {
            audits = auditRepository.findByUserIdAndEventTypeOrderByCreatedAtDesc(userId, eventType, pageable);
        } else if (userId != null) {
            audits = auditRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
        } else if (eventType != null) {
            audits = auditRepository.findByEventTypeOrderByCreatedAtDesc(eventType, pageable);
        } else if (startDate != null && endDate != null) {
            audits = auditRepository.findByDateRange(startDate, endDate, pageable);
        } else {
            audits = auditRepository.findAll(pageable);
        }
        
        List<AuditLogDto> auditDtos = audits.getContent().stream()
            .map(userMapper::toAuditLogDto)
            .collect(Collectors.toList());
        
        logger.info("Retrieved {} audit logs", audits.getTotalElements());
        return userMapper.toPageResponseDto(audits, auditDtos);
    }
    
    /**
     * Get audit logs for a specific user
     */
    @Transactional(readOnly = true)
    public PageResponseDto<AuditLogDto> getUserAuditLogs(String userId, Pageable pageable) {
        Page<LoginAudit> audits = auditRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
        
        List<AuditLogDto> auditDtos = audits.getContent().stream()
            .map(userMapper::toAuditLogDto)
            .collect(Collectors.toList());
        
        logger.info("Retrieved {} audit logs for user: {}", audits.getTotalElements(), userId);
        return userMapper.toPageResponseDto(audits, auditDtos);
    }
    
    /**
     * Get recent login attempts for a user
     */
    @Transactional(readOnly = true)
    public List<AuditLogDto> getRecentLogins(String userId, int limit) {
        Pageable pageable = Pageable.ofSize(limit);
        Page<LoginAudit> audits = auditRepository.findRecentSuccessfulLogins(userId, pageable);
        
        return audits.getContent().stream()
            .map(userMapper::toAuditLogDto)
            .collect(Collectors.toList());
    }
    
    /**
     * Check for suspicious login activity
     */
    @Transactional(readOnly = true)
    public boolean hasSuspiciousActivity(String userId) {
        LocalDateTime since = LocalDateTime.now().minusHours(24);
        long failedAttempts = auditRepository.countFailedLoginAttempts(userId, since);
        
        boolean suspicious = failedAttempts >= 5;
        if (suspicious) {
            logger.warn("Suspicious activity detected for user: {} with {} failed attempts in 24h", 
                userId, failedAttempts);
        }
        
        return suspicious;
    }
    
    /**
     * Generate a session ID
     */
    private String generateSessionId() {
        return java.util.UUID.randomUUID().toString();
    }
}
