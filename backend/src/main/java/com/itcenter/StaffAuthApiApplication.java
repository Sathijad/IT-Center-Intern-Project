package com.itcenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Main Spring Boot application class for IT Center Staff Authentication API
 * 
 * Features:
 * - JWT-based authentication with AWS Cognito
 * - Role-based access control (RBAC)
 * - User management and audit logging
 * - RESTful API with OpenAPI documentation
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableAsync
public class StaffAuthApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(StaffAuthApiApplication.class, args);
    }
}
