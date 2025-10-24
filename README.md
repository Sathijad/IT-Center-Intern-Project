# IT Center Staff Authentication & Role Management

## Phase 1: Staff Authentication & Role Management
**Dates:** Oct 20–22  
**Environments:** DEV / STG / PRD  
**Repository:** https://github.com/Sathijad/IT-Center-Intern-Project.git

## 🎯 Project Overview

This project implements a comprehensive staff authentication and role management system with secure login, MFA, and role-based access control across mobile and web platforms.

### Key Features
- **AWS Cognito Integration**: OIDC-based authentication with hosted UI
- **JWT Validation**: Spring Boot backend with comprehensive token validation
- **Role-Based Access Control**: Admin and Staff roles with proper enforcement
- **Multi-Platform**: React admin portal and Flutter mobile app
- **Security First**: OWASP compliance, encryption, and comprehensive logging
- **Observability**: CloudWatch integration with metrics and tracing

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

## 📁 Project Structure

```
it-center-project/
├── backend/                 # Spring Boot API
│   ├── src/main/java/
│   ├── src/main/resources/
│   ├── src/test/
│   └── pom.xml
├── frontend/               # React Admin Portal
│   ├── src/
│   ├── public/
│   └── package.json
├── mobile/                 # Flutter Mobile App
│   ├── lib/
│   ├── android/
│   ├── ios/
│   └── pubspec.yaml
├── database/              # Database migrations and seeds
│   ├── migrations/
│   └── seeds/
├── docs/                  # Documentation
│   ├── api/
│   └── deployment/
├── .github/               # GitHub Actions CI/CD
│   └── workflows/
└── docker-compose.yml     # Local development setup
```

## 🚀 Quick Start

### Prerequisites
- Java 25
- Node.js 22+
- Flutter 3.35+
- PostgreSQL 15+
- Docker & Docker Compose

### Local Development Setup

1. **Clone and setup**:
   ```bash
   git clone https://github.com/Sathijad/IT-Center-Intern-Project.git
   cd IT-Center-Intern-Project
   ```

2. **Start infrastructure**:
   ```bash
   docker-compose up -d postgres redis
   ```

3. **Backend**:
   ```bash
   cd backend
   ./mvnw spring-boot:run
   ```

4. **Frontend**:
   ```bash
   cd frontend
   npm install
   npm run dev
   ```

5. **Mobile**:
   ```bash
   cd mobile
   flutter pub get
   flutter run
   ```

## 🔐 Security Features

- **Authentication**: AWS Cognito with OIDC integration
- **Authorization**: Role-based access control (RBAC)
- **Multi-Factor Authentication**: TOTP support
- **Security Scanning**: OWASP ZAP integration
- **Input Validation**: Comprehensive validation with Zod and Bean Validation
- **Encryption**: TLS 1.3 for transit, encryption at rest
- **Audit Logging**: Complete audit trail for all actions

## 📊 Monitoring & Observability

- **Logging**: Structured logging with request tracing
- **Metrics**: Performance and business metrics
- **Tracing**: Distributed tracing with Spring Sleuth
- **Dashboards**: CloudWatch and Grafana integration
- **Alerts**: Automated alerting for critical issues

## 🧪 Testing Strategy

- **Unit Tests**: 80%+ code coverage with JUnit
- **API Tests**: Rest Assured and Postman collections
- **UI Tests**: Selenium for React portal
- **Mobile Tests**: Appium for Flutter app
- **Security Tests**: OWASP ZAP DAST scanning
- **Performance Tests**: k6 load testing

## 📈 KPIs & Success Metrics

- **Performance**: p95 < 300ms response time
- **Reliability**: Auth failure rate < 1%
- **Security**: 100% JWT coverage, No High/Critical ZAP findings
- **Quality**: Lighthouse score ≥ 90, WCAG 2.1 AA compliance

## 🚀 Deployment

The project uses GitHub Actions for CI/CD with automated deployment to DEV, STG, and PRD environments.

### Environments
- **DEV**: Development environment for feature testing
- **STG**: Staging environment for integration testing
- **PRD**: Production environment with canary deployment

## 📚 Documentation

- [API Documentation](./docs/api/README.md)
- [Deployment Guide](./docs/deployment/README.md)
- [Security Guidelines](./docs/security/README.md)
- [Runbook](./docs/runbook/README.md)

## 🤝 Contributing

1. Create feature branch from `main`
2. Follow commit convention: `type(scope): description`
3. Include tests and documentation
4. Submit PR with template completion
5. Ensure all checks pass before merge

## 📄 License

This project is proprietary software developed for IT Center internal use.

## 🆘 Support

For issues and questions, please contact the development team or create an issue in the repository.
