package com.itcenter.repository;

import com.itcenter.entity.AppUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for AppUser entity
 */
@Repository
public interface AppUserRepository extends JpaRepository<AppUser, String> {
    
    /**
     * Find user by email
     */
    Optional<AppUser> findByEmail(String email);
    
    /**
     * Check if user exists by email
     */
    boolean existsByEmail(String email);
    
    /**
     * Search users by display name or email with pagination
     */
    @Query("SELECT u FROM AppUser u WHERE " +
           "LOWER(u.displayName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(u.email) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<AppUser> searchUsers(@Param("query") String query, Pageable pageable);
    
    /**
     * Find users by role name
     */
    @Query("SELECT u FROM AppUser u JOIN u.userRoles ur JOIN ur.role r WHERE r.name = :roleName")
    Page<AppUser> findByRoleName(@Param("roleName") String roleName, Pageable pageable);
    
    /**
     * Count users by role
     */
    @Query("SELECT COUNT(u) FROM AppUser u JOIN u.userRoles ur JOIN ur.role r WHERE r.name = :roleName")
    long countByRoleName(@Param("roleName") String roleName);
}
