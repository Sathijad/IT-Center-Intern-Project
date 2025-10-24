# API Documentation

## Overview

The IT Center Staff Authentication API provides secure authentication and user management services. This API follows RESTful principles and uses JWT tokens for authentication.

## Base URL

- **Development**: `http://localhost:8080/api/v1`
- **Staging**: `https://stg-api.itcenter.com/api/v1`
- **Production**: `https://api.itcenter.com/api/v1`

## Authentication

All API endpoints (except health check) require authentication using JWT tokens from AWS Cognito.

### Headers

```
Authorization: Bearer <your-jwt-token>
Content-Type: application/json
```

### Token Information

- **Issuer**: AWS Cognito User Pool
- **Expiration**: 1 hour
- **Refresh**: Automatic via Cognito
- **Claims**: User ID, roles, email, etc.

## Endpoints

### Health Check

#### GET /healthz

Check if the service is running and healthy.

**Response**:
```json
{
  "status": "UP",
  "timestamp": "2024-01-20T10:30:00Z",
  "service": "IT Center Staff Auth API",
  "version": "1.0.0"
}
```

### User Profile

#### GET /me

Get current user's profile information.

**Headers**: `Authorization: Bearer <token>`

**Response**:
```json
{
  "userId": "us-east-1:12345678-1234-1234-1234-123456789012",
  "email": "john.doe@itcenter.com",
  "displayName": "John Doe",
  "locale": "en-US",
  "createdAt": "2024-01-01T00:00:00Z",
  "updatedAt": "2024-01-20T10:30:00Z",
  "roles": ["STAFF"]
}
```

#### PATCH /me

Update current user's profile.

**Headers**: `Authorization: Bearer <token>`

**Request Body**:
```json
{
  "displayName": "John Smith",
  "locale": "en-GB"
}
```

**Response**: Same as GET /me

### User Management (Admin Only)

#### GET /admin/users

List users with pagination and search.

**Headers**: `Authorization: Bearer <token>` (Admin role required)

**Query Parameters**:
- `query` (optional): Search by name or email
- `page` (optional): Page number (0-based, default: 0)
- `size` (optional): Page size (default: 20)
- `sort` (optional): Sort field (default: createdAt)
- `direction` (optional): Sort direction (asc/desc, default: desc)

**Response**:
```json
{
  "content": [
    {
      "userId": "us-east-1:12345678-1234-1234-1234-123456789012",
      "email": "john.doe@itcenter.com",
      "displayName": "John Doe",
      "locale": "en-US",
      "createdAt": "2024-01-01T00:00:00Z",
      "updatedAt": "2024-01-20T10:30:00Z",
      "lastLoginAt": "2024-01-20T09:15:00Z",
      "roles": ["STAFF"],
      "active": true
    }
  ],
  "page": 0,
  "size": 20,
  "totalElements": 150,
  "totalPages": 8,
  "first": true,
  "last": false,
  "hasNext": true,
  "hasPrevious": false,
  "timestamp": "2024-01-20T10:30:00Z"
}
```

#### GET /admin/users/{userId}

Get specific user details.

**Headers**: `Authorization: Bearer <token>` (Admin role required)

**Response**: Same as user object in GET /admin/users

#### PATCH /admin/users/{userId}/roles

Update user roles.

**Headers**: `Authorization: Bearer <token>` (Admin role required)

**Request Body**:
```json
{
  "roles": ["ADMIN", "STAFF"]
}
```

**Response**: Updated user object

### Audit Logs (Admin Only)

#### GET /admin/audit-log

Get audit logs with filtering.

**Headers**: `Authorization: Bearer <token>` (Admin role required)

**Query Parameters**:
- `user_id` (optional): Filter by user ID
- `event_type` (optional): Filter by event type
- `start_date` (optional): Start date (ISO 8601)
- `end_date` (optional): End date (ISO 8601)
- `page` (optional): Page number (0-based, default: 0)
- `size` (optional): Page size (default: 20)

**Response**:
```json
{
  "content": [
    {
      "id": 12345,
      "userId": "us-east-1:12345678-1234-1234-1234-123456789012",
      "userEmail": "john.doe@itcenter.com",
      "userDisplayName": "John Doe",
      "eventType": "LOGIN",
      "ipAddress": "192.168.1.100",
      "userAgent": "Mozilla/5.0...",
      "success": true,
      "failureReason": null,
      "sessionId": "abc123-def456-ghi789",
      "createdAt": "2024-01-20T10:30:00Z"
    }
  ],
  "page": 0,
  "size": 20,
  "totalElements": 5000,
  "totalPages": 250,
  "first": true,
  "last": false,
  "hasNext": true,
  "hasPrevious": false,
  "timestamp": "2024-01-20T10:30:00Z"
}
```

#### GET /admin/audit-log/user/{userId}

Get audit logs for a specific user.

**Headers**: `Authorization: Bearer <token>` (Admin role required)

**Response**: Same format as GET /admin/audit-log

#### GET /admin/audit-log/recent/{userId}

Get recent successful logins for a user.

**Headers**: `Authorization: Bearer <token>` (Admin role required)

**Query Parameters**:
- `limit` (optional): Number of recent logins (default: 10)

**Response**: Array of audit log objects

## Event Types

The following event types are logged in the audit system:

- `LOGIN`: Successful login
- `LOGOUT`: User logout
- `LOGIN_FAILED`: Failed login attempt
- `MFA_SUCCESS`: Successful MFA verification
- `MFA_FAILED`: Failed MFA verification
- `PASSWORD_RESET`: Password reset request
- `ROLE_ASSIGNED`: Role assigned to user
- `ROLE_REMOVED`: Role removed from user
- `PROFILE_UPDATED`: User profile updated

## Error Handling

All API errors follow a consistent format:

```json
{
  "error": "VALIDATION_ERROR",
  "message": "Display name must be between 2 and 50 characters",
  "timestamp": "2024-01-20T10:30:00Z",
  "traceId": "abc123-def456-ghi789"
}
```

### HTTP Status Codes

- `200 OK`: Request successful
- `201 Created`: Resource created successfully
- `400 Bad Request`: Invalid request data
- `401 Unauthorized`: Authentication required
- `403 Forbidden`: Insufficient permissions
- `404 Not Found`: Resource not found
- `409 Conflict`: Resource conflict
- `422 Unprocessable Entity`: Validation error
- `500 Internal Server Error`: Server error

### Common Error Types

- `VALIDATION_ERROR`: Input validation failed
- `AUTHENTICATION_ERROR`: Authentication failed
- `AUTHORIZATION_ERROR`: Insufficient permissions
- `NOT_FOUND_ERROR`: Resource not found
- `CONFLICT_ERROR`: Resource conflict
- `INTERNAL_ERROR`: Internal server error

## Rate Limiting

API requests are rate limited to prevent abuse:

- **Per User**: 100 requests per minute
- **Per IP**: 1000 requests per hour
- **Burst**: 20 requests per second

Rate limit headers are included in responses:

```
X-RateLimit-Limit: 100
X-RateLimit-Remaining: 95
X-RateLimit-Reset: 1642680000
```

## Pagination

List endpoints support pagination with the following parameters:

- `page`: Page number (0-based)
- `size`: Number of items per page
- `sort`: Field to sort by
- `direction`: Sort direction (asc/desc)

Response includes pagination metadata:

```json
{
  "content": [...],
  "page": 0,
  "size": 20,
  "totalElements": 150,
  "totalPages": 8,
  "first": true,
  "last": false,
  "hasNext": true,
  "hasPrevious": false
}
```

## Examples

### Complete Authentication Flow

1. **Login via Cognito Hosted UI**:
   ```
   GET https://your-domain.auth.us-east-1.amazoncognito.com/login?
     client_id=your-client-id&
     response_type=code&
     scope=openid+email+profile&
     redirect_uri=https://your-app.com/callback
   ```

2. **Exchange code for tokens**:
   ```bash
   curl -X POST https://your-domain.auth.us-east-1.amazoncognito.com/oauth2/token \
     -H "Content-Type: application/x-www-form-urlencoded" \
     -d "grant_type=authorization_code&code=your-code&client_id=your-client-id&redirect_uri=https://your-app.com/callback"
   ```

3. **Use JWT token for API calls**:
   ```bash
   curl -X GET http://localhost:8080/api/v1/me \
     -H "Authorization: Bearer your-jwt-token"
   ```

### User Management Example

```bash
# Get all users
curl -X GET "http://localhost:8080/api/v1/admin/users?page=0&size=10" \
  -H "Authorization: Bearer admin-token"

# Search users
curl -X GET "http://localhost:8080/api/v1/admin/users?query=john" \
  -H "Authorization: Bearer admin-token"

# Update user roles
curl -X PATCH "http://localhost:8080/api/v1/admin/users/user-id/roles" \
  -H "Authorization: Bearer admin-token" \
  -H "Content-Type: application/json" \
  -d '{"roles": ["ADMIN"]}'
```

### Audit Log Example

```bash
# Get recent audit logs
curl -X GET "http://localhost:8080/api/v1/admin/audit-log?page=0&size=20" \
  -H "Authorization: Bearer admin-token"

# Filter by event type
curl -X GET "http://localhost:8080/api/v1/admin/audit-log?event_type=LOGIN" \
  -H "Authorization: Bearer admin-token"

# Filter by date range
curl -X GET "http://localhost:8080/api/v1/admin/audit-log?start_date=2024-01-01T00:00:00Z&end_date=2024-01-31T23:59:59Z" \
  -H "Authorization: Bearer admin-token"
```

## SDK Examples

### JavaScript/TypeScript

```typescript
import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080/api/v1',
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add auth token
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('auth_token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Get user profile
const getUserProfile = async () => {
  const response = await api.get('/me');
  return response.data;
};

// Update user profile
const updateUserProfile = async (data: any) => {
  const response = await api.patch('/me', data);
  return response.data;
};
```

### Java

```java
@Service
public class UserService {
    
    @Autowired
    private RestTemplate restTemplate;
    
    public UserProfile getUserProfile(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        
        HttpEntity<?> entity = new HttpEntity<>(headers);
        
        ResponseEntity<UserProfile> response = restTemplate.exchange(
            "http://localhost:8080/api/v1/me",
            HttpMethod.GET,
            entity,
            UserProfile.class
        );
        
        return response.getBody();
    }
}
```

### Flutter/Dart

```dart
import 'package:http/http.dart' as http;
import 'dart:convert';

class ApiService {
  static const String baseUrl = 'http://localhost:8080/api/v1';
  
  static Future<Map<String, dynamic>> getUserProfile(String token) async {
    final response = await http.get(
      Uri.parse('$baseUrl/me'),
      headers: {
        'Authorization': 'Bearer $token',
        'Content-Type': 'application/json',
      },
    );
    
    if (response.statusCode == 200) {
      return json.decode(response.body);
    } else {
      throw Exception('Failed to get user profile');
    }
  }
}
```

---

**Last Updated**: January 2024  
**API Version**: 1.0.0  
**OpenAPI Spec**: Available at `/swagger-ui.html`
