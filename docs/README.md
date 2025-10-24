# IT Center Staff Authentication System

## 📋 Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Quick Start](#quick-start)
- [Development Setup](#development-setup)
- [API Documentation](#api-documentation)
- [Security](#security)
- [Testing](#testing)
- [Deployment](#deployment)
- [Monitoring](#monitoring)
- [Troubleshooting](#troubleshooting)
- [Contributing](#contributing)

## 🎯 Overview

The IT Center Staff Authentication System is a comprehensive solution for managing staff authentication, role-based access control, and audit logging across web and mobile platforms.

### Key Features

- **Multi-Platform Authentication**: Web admin portal (React) and mobile app (Flutter)
- **AWS Cognito Integration**: Secure authentication with hosted UI
- **Role-Based Access Control**: Admin and Staff roles with proper enforcement
- **Multi-Factor Authentication**: TOTP support for enhanced security
- **Comprehensive Audit Logging**: Track all user activities and system events
- **RESTful API**: Spring Boot backend with OpenAPI documentation
- **Security First**: OWASP compliance, encryption, and comprehensive logging

### Technology Stack

- **Backend**: Java 25, Spring Boot 3, PostgreSQL, AWS Cognito
- **Frontend**: React 18, TypeScript, Tailwind CSS, Shadcn UI
- **Mobile**: Flutter 3.35, Dart 3.9, Provider/Riverpod
- **Infrastructure**: Docker, Docker Compose, GitHub Actions
- **Security**: OWASP ZAP, SAST, Dependency scanning

## 🏗️ Architecture

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Flutter App   │    │  React Admin    │    │   Spring Boot   │
│   (Mobile)      │    │   (Web Portal)  │    │   (Backend)     │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         │                       │                       │
         └───────────────────────┼───────────────────────┘
                                 │
                    ┌─────────────────┐
                    │   AWS Cognito   │
                    │   (Auth Service)│
                    └─────────────────┘
                                 │
                    ┌─────────────────┐
                    │   PostgreSQL    │
                    │   (Database)     │
                    └─────────────────┘
```

### Components

1. **Backend API** (`/backend`)
   - Spring Boot 3 application
   - JWT validation with AWS Cognito
   - PostgreSQL database with Flyway migrations
   - RESTful API with OpenAPI documentation

2. **React Admin Portal** (`/frontend`)
   - Modern React 18 application
   - Tailwind CSS + Shadcn UI components
   - Role-based routing and access control
   - Real-time data with React Query

3. **Flutter Mobile App** (`/mobile`)
   - Cross-platform mobile application
   - Provider/Riverpod state management
   - Biometric authentication support
   - Offline-first architecture

## 🚀 Quick Start

### Prerequisites

- Java 25+
- Node.js 22+
- Flutter 3.35+
- PostgreSQL 15+
- Docker & Docker Compose

### Local Development

1. **Clone the repository**:
   ```bash
   git clone https://github.com/Sathijad/IT-Center-Intern-Project.git
   cd IT-Center-Intern-Project
   ```

2. **Start infrastructure services**:
   ```bash
   docker-compose up -d postgres redis
   ```

3. **Backend setup**:
   ```bash
   cd backend
   ./mvnw spring-boot:run
   ```

4. **Frontend setup**:
   ```bash
   cd frontend
   npm install
   npm run dev
   ```

5. **Mobile setup**:
   ```bash
   cd mobile
   flutter pub get
   flutter run
   ```

6. **Access the applications**:
   - Backend API: http://localhost:8080/api/v1
   - Frontend Portal: http://localhost:3000
   - API Documentation: http://localhost:8080/swagger-ui.html

## 🛠️ Development Setup

### Backend Development

1. **Database Setup**:
   ```bash
   # Create database
   createdb itcenter_auth
   
   # Run migrations
   cd backend
   ./mvnw flyway:migrate
   ```

2. **Environment Configuration**:
   ```bash
   # Copy environment template
   cp backend/src/main/resources/application-template.yml \
      backend/src/main/resources/application-local.yml
   
   # Update with your AWS Cognito settings
   ```

3. **Run Tests**:
   ```bash
   ./mvnw test
   ./mvnw jacoco:report
   ```

### Frontend Development

1. **Install Dependencies**:
   ```bash
   cd frontend
   npm install
   ```

2. **Environment Setup**:
   ```bash
   # Create environment file
   cp .env.example .env.local
   
   # Update API URL
   echo "REACT_APP_API_URL=http://localhost:8080/api/v1" > .env.local
   ```

3. **Development Commands**:
   ```bash
   npm run dev          # Start development server
   npm run build        # Build for production
   npm run test         # Run tests
   npm run lint         # Run linting
   npm run type-check   # TypeScript checking
   ```

### Mobile Development

1. **Flutter Setup**:
   ```bash
   cd mobile
   flutter pub get
   ```

2. **Development Commands**:
   ```bash
   flutter run          # Run on connected device
   flutter test         # Run tests
   flutter analyze      # Static analysis
   flutter build apk    # Build Android APK
   flutter build ios    # Build iOS app
   ```

## 📚 API Documentation

### Authentication Endpoints

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/me` | Get current user profile | Yes |
| PATCH | `/me` | Update user profile | Yes |
| GET | `/admin/users` | List users (Admin) | Yes (Admin) |
| GET | `/admin/users/{id}` | Get user details (Admin) | Yes (Admin) |
| PATCH | `/admin/users/{id}/roles` | Update user roles (Admin) | Yes (Admin) |
| GET | `/admin/audit-log` | Get audit logs (Admin) | Yes (Admin) |
| GET | `/healthz` | Health check | No |

### Authentication Flow

1. **Login**: User authenticates via AWS Cognito Hosted UI
2. **JWT Token**: Cognito returns JWT token
3. **API Calls**: Include token in `Authorization: Bearer <token>` header
4. **Token Validation**: Backend validates JWT with Cognito
5. **Role Check**: Backend enforces role-based access control

### Error Handling

All API errors follow this format:
```json
{
  "error": "VALIDATION_ERROR",
  "message": "Display name must be between 2 and 50 characters",
  "timestamp": "2024-01-20T10:30:00Z",
  "traceId": "abc123-def456-ghi789"
}
```

## 🔒 Security

### Authentication & Authorization

- **AWS Cognito**: OIDC integration with hosted UI
- **JWT Tokens**: Secure token-based authentication
- **Role-Based Access Control**: Admin and Staff roles
- **Multi-Factor Authentication**: TOTP support

### Security Measures

- **Input Validation**: Comprehensive validation with Bean Validation
- **SQL Injection Prevention**: JPA/Hibernate with parameterized queries
- **XSS Protection**: Content Security Policy headers
- **CSRF Protection**: Spring Security CSRF tokens
- **Rate Limiting**: API rate limiting (100 req/min per user)
- **Audit Logging**: Complete audit trail for all actions

### Security Scanning

- **OWASP ZAP**: Dynamic Application Security Testing
- **SAST**: Static Application Security Testing
- **Dependency Scanning**: Automated vulnerability scanning
- **Container Scanning**: Docker image security scanning

## 🧪 Testing

### Backend Testing

```bash
# Unit Tests
./mvnw test

# Integration Tests
./mvnw verify

# Coverage Report
./mvnw jacoco:report
```

### Frontend Testing

```bash
# Unit Tests
npm run test

# E2E Tests
npm run e2e

# Coverage Report
npm run test:coverage
```

### Mobile Testing

```bash
# Unit Tests
flutter test

# Integration Tests
flutter drive --target=test_driver/app.dart

# Coverage Report
flutter test --coverage
```

### Security Testing

```bash
# OWASP ZAP Scan
docker run -t owasp/zap2docker-stable zap-baseline.py \
  -t http://localhost:8080/api/v1

# Dependency Check
./mvnw dependency-check:check
```

## 🚀 Deployment

### Environment Setup

1. **Development (DEV)**:
   - Automatic deployment on `develop` branch
   - Full test suite execution
   - Security scanning

2. **Staging (STG)**:
   - Automatic deployment on `main` branch
   - Performance testing
   - Integration testing

3. **Production (PRD)**:
   - Manual deployment approval
   - Canary deployment (10% → 100%)
   - 24-hour monitoring

### Docker Deployment

```bash
# Build images
docker-compose build

# Deploy stack
docker-compose up -d

# Check status
docker-compose ps
docker-compose logs -f
```

### AWS Deployment

```bash
# Deploy backend
aws ecs update-service --cluster itcenter-cluster \
  --service backend-service --force-new-deployment

# Deploy frontend
aws s3 sync frontend/build/ s3://itcenter-frontend-bucket/
```

## 📊 Monitoring

### Application Metrics

- **Response Time**: p95 < 300ms target
- **Error Rate**: < 1% target
- **Availability**: 99.9% SLA
- **Throughput**: Requests per second

### Logging

- **Structured Logging**: JSON format with correlation IDs
- **Log Levels**: DEBUG, INFO, WARN, ERROR
- **Log Aggregation**: CloudWatch Logs
- **Retention**: 90 days

### Alerting

- **Critical Alerts**: Authentication failures, database errors
- **Warning Alerts**: High response times, memory usage
- **Info Alerts**: Deployment notifications, user activities

## 🔧 Troubleshooting

### Common Issues

1. **Database Connection Issues**:
   ```bash
   # Check PostgreSQL status
   docker-compose ps postgres
   
   # Check logs
   docker-compose logs postgres
   ```

2. **Authentication Failures**:
   - Verify AWS Cognito configuration
   - Check JWT token expiration
   - Validate user roles

3. **Frontend Build Issues**:
   ```bash
   # Clear cache
   npm run clean
   
   # Reinstall dependencies
   rm -rf node_modules package-lock.json
   npm install
   ```

4. **Mobile Build Issues**:
   ```bash
   # Clean Flutter cache
   flutter clean
   
   # Get dependencies
   flutter pub get
   ```

### Debug Mode

```bash
# Backend debug
export SPRING_PROFILES_ACTIVE=debug
./mvnw spring-boot:run

# Frontend debug
export REACT_APP_DEBUG=true
npm run dev

# Mobile debug
flutter run --debug
```

## 🤝 Contributing

### Development Workflow

1. **Create Feature Branch**:
   ```bash
   git checkout -b feature/auth-improvements
   ```

2. **Make Changes**:
   - Follow coding standards
   - Add tests for new features
   - Update documentation

3. **Commit Changes**:
   ```bash
   git commit -m "feat(auth): add biometric authentication support"
   ```

4. **Create Pull Request**:
   - Include description and screenshots
   - Reference related issues
   - Ensure all checks pass

### Code Standards

- **Java**: Google Java Style Guide
- **TypeScript**: ESLint + Prettier
- **Dart**: Dart Style Guide
- **Commits**: Conventional Commits format

### Pull Request Template

```markdown
## Description
Brief description of changes

## Type of Change
- [ ] Bug fix
- [ ] New feature
- [ ] Breaking change
- [ ] Documentation update

## Testing
- [ ] Unit tests pass
- [ ] Integration tests pass
- [ ] Manual testing completed

## Security
- [ ] Security scan passed
- [ ] No sensitive data exposed
- [ ] Input validation added

## Screenshots
(if applicable)

## Checklist
- [ ] Code follows style guidelines
- [ ] Self-review completed
- [ ] Documentation updated
- [ ] Tests added/updated
```

## 📞 Support

For issues and questions:

- **Documentation**: Check this README and `/docs` folder
- **Issues**: Create GitHub issue with detailed description
- **Discussions**: Use GitHub Discussions for questions
- **Email**: dev@itcenter.com for urgent issues

## 📄 License

This project is proprietary software developed for IT Center internal use.

---

**Last Updated**: January 2024  
**Version**: 1.0.0  
**Maintainer**: IT Center Development Team
