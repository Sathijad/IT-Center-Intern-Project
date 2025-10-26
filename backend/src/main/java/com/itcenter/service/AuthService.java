package com.itcenter.service;

import com.itcenter.dto.*;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.Map;

/**
 * Service for handling authentication operations
 */
@Service
public class AuthService {
    
    @Value("${aws.cognito.user-pool-id}")
    private String userPoolId;
    
    @Value("${aws.cognito.client-id}")
    private String clientId;
    
    @Value("${aws.cognito.client-secret:}")
    private String clientSecret; // empty if not set
    
    private final RestTemplate restTemplate = new RestTemplate();
    
    public LoginResponseDto login(LoginRequestDto request) {
        try {
            // For demo purposes, simulate authentication
            if ("admin@itcenter.com".equals(request.getEmail()) && "password".equals(request.getPassword())) {
                return new LoginResponseDto("demo_jwt_token_" + System.currentTimeMillis(), false, "Login successful");
            } else if ("user@itcenter.com".equals(request.getEmail()) && "password".equals(request.getPassword())) {
                return new LoginResponseDto(null, true, "MFA required");
            } else {
                throw new RuntimeException("Invalid email or password");
            }
        } catch (Exception e) {
            throw new RuntimeException("Login failed: " + e.getMessage());
        }
    }
    
    public RegisterResponseDto register(RegisterRequestDto request) {
        try {
            // For demo purposes, simulate registration
            return new RegisterResponseDto("Registration successful. Please check your email for verification.");
        } catch (Exception e) {
            throw new RuntimeException("Registration failed: " + e.getMessage());
        }
    }
    
    public MfaVerificationResponseDto verifyMfa(MfaVerificationRequestDto request) {
        try {
            // For demo purposes, simulate MFA verification
            if ("123456".equals(request.getCode())) {
                return new MfaVerificationResponseDto("demo_jwt_token_" + System.currentTimeMillis(), "MFA verification successful");
            } else {
                throw new RuntimeException("Invalid MFA code");
            }
        } catch (Exception e) {
            throw new RuntimeException("MFA verification failed: " + e.getMessage());
        }
    }
    
    public ForgotPasswordResponseDto forgotPassword(ForgotPasswordRequestDto request) {
        try {
            // For demo purposes, simulate password reset
            return new ForgotPasswordResponseDto("Password reset instructions sent to your email.");
        } catch (Exception e) {
            throw new RuntimeException("Password reset failed: " + e.getMessage());
        }
    }
    
    public Map<String, Object> handleCognitoCallback(String code, String redirectUri) {
        try {
            // Exchange authorization code for tokens
            String tokenEndpoint = "https://itcenter-auth.auth.ap-southeast-2.amazoncognito.com/oauth2/token";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            
            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("grant_type", "authorization_code");
            body.add("client_id", clientId);
            body.add("client_secret", clientSecret);
            body.add("code", code);
            body.add("redirect_uri", redirectUri);
            
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
            
            ResponseEntity<Map> response = restTemplate.postForEntity(tokenEndpoint, request, Map.class);
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> tokenResponse = response.getBody();
                
                // Return the actual Cognito tokens
                Map<String, Object> result = new HashMap<>();
                result.put("access_token", tokenResponse.get("access_token"));
                result.put("id_token", tokenResponse.get("id_token"));
                result.put("refresh_token", tokenResponse.get("refresh_token"));
                result.put("token_type", tokenResponse.get("token_type"));
                result.put("expires_in", tokenResponse.get("expires_in"));
                
                return result;
            } else {
                throw new RuntimeException("Failed to exchange code for tokens");
            }
        } catch (Exception e) {
            throw new RuntimeException("Cognito callback failed: " + e.getMessage());
        }
    }
}
