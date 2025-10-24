package com.itcenter.repository;

import com.itcenter.entity.LoginAudit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

/**
 * Repository interface for LoginAudit entity
 */
@Repository
public interface LoginAuditRepository extends JpaRepository<LoginAudit, Long> {
    
    /**
     * Find audit logs by user ID with pagination
     */
    Page<LoginAudit> findByUserIdOrderByCreatedAtDesc(String userId, Pageable pageable);
    
    /**
     * Find audit logs by event type with pagination
     */
    Page<LoginAudit> findByEventTypeOrderByCreatedAtDesc(String eventType, Pageable pageable);
    
    /**
     * Find audit logs by user ID and event type with pagination
     */
    Page<LoginAudit> findByUserIdAndEventTypeOrderByCreatedAtDesc(
        String userId, String eventType, Pageable pageable);
    
    /**
     * Find audit logs within date range with pagination
     */
    @Query("SELECT la FROM LoginAudit la WHERE " +
           "la.createdAt >= :startDate AND la.createdAt <= :endDate " +
           "ORDER BY la.createdAt DESC")
    Page<LoginAudit> findByDateRange(
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate,
        Pageable pageable);
    
    /**
     * Find audit logs by user ID within date range with pagination
     */
    @Query("SELECT la FROM LoginAudit la WHERE " +
           "la.user.userId = :userId AND " +
           "la.createdAt >= :startDate AND la.createdAt <= :endDate " +
           "ORDER BY la.createdAt DESC")
    Page<LoginAudit> findByUserIdAndDateRange(
        @Param("userId") String userId,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate,
        Pageable pageable);
    
    /**
     * Count failed login attempts for a user in the last 24 hours
     */
    @Query("SELECT COUNT(la) FROM LoginAudit la WHERE " +
           "la.user.userId = :userId AND " +
           "la.eventType = 'LOGIN_FAILED' AND " +
           "la.createdAt >= :since")
    long countFailedLoginAttempts(@Param("userId") String userId, @Param("since") LocalDateTime since);
    
    /**
     * Find recent successful logins for a user
     */
    @Query("SELECT la FROM LoginAudit la WHERE " +
           "la.user.userId = :userId AND " +
           "la.eventType = 'LOGIN' AND " +
           "la.success = true " +
           "ORDER BY la.createdAt DESC")
    Page<LoginAudit> findRecentSuccessfulLogins(@Param("userId") String userId, Pageable pageable);
}
