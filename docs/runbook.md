# IT Center Staff Authentication System - Runbook

## 🚨 Emergency Procedures

### System Down
1. **Check Health Endpoints**:
   ```bash
   curl http://localhost:8080/api/v1/healthz
   curl http://localhost:3000/health
   ```

2. **Check Service Status**:
   ```bash
   docker-compose ps
   docker-compose logs --tail=100
   ```

3. **Restart Services**:
   ```bash
   docker-compose restart
   ```

### Database Issues
1. **Check PostgreSQL**:
   ```bash
   docker-compose exec postgres psql -U itcenter -d itcenter_auth
   ```

2. **Check Connections**:
   ```sql
   SELECT * FROM pg_stat_activity;
   ```

3. **Restart Database**:
   ```bash
   docker-compose restart postgres
   ```

### Authentication Failures
1. **Check Cognito Status**:
   - AWS Console → Cognito → User Pools
   - Check service health

2. **Verify JWT Tokens**:
   ```bash
   # Decode JWT token
   echo "YOUR_JWT_TOKEN" | base64 -d
   ```

3. **Check Token Expiration**:
   - Tokens expire after 1 hour
   - Refresh tokens expire after 30 days

## 🔧 Maintenance Procedures

### Daily Checks
- [ ] Check application health endpoints
- [ ] Review error logs for anomalies
- [ ] Monitor authentication success rates
- [ ] Check database performance metrics

### Weekly Tasks
- [ ] Review audit logs for suspicious activity
- [ ] Update security patches
- [ ] Backup database
- [ ] Performance analysis

### Monthly Tasks
- [ ] Security vulnerability scan
- [ ] Update dependencies
- [ ] Review user access permissions
- [ ] Capacity planning review

## 📊 Monitoring & Alerts

### Key Metrics
- **Response Time**: Target < 300ms p95
- **Error Rate**: Target < 1%
- **Authentication Success Rate**: Target > 99%
- **Database Connections**: Monitor pool usage

### Alert Thresholds
- **Critical**: Response time > 1s, Error rate > 5%
- **Warning**: Response time > 500ms, Error rate > 2%
- **Info**: Deployment notifications, User activities

### Log Analysis
```bash
# Check recent errors
docker-compose logs backend | grep ERROR | tail -20

# Check authentication logs
docker-compose logs backend | grep "LOGIN" | tail -10

# Check performance
docker-compose logs backend | grep "slow query" | tail -5
```

## 🔐 Security Procedures

### User Management
1. **Create New User**:
   - Add to AWS Cognito User Pool
   - Assign appropriate role
   - Send welcome email

2. **Deactivate User**:
   - Disable in Cognito
   - Revoke all sessions
   - Update audit log

3. **Role Changes**:
   - Update user roles in database
   - Log role change event
   - Notify user of changes

### Security Incidents
1. **Suspicious Activity**:
   - Review audit logs
   - Check IP addresses
   - Temporarily disable account if needed

2. **Data Breach**:
   - Immediately disable affected accounts
   - Rotate all secrets and tokens
   - Notify security team
   - Document incident

## 🚀 Deployment Procedures

### Pre-Deployment Checklist
- [ ] All tests passing
- [ ] Security scan completed
- [ ] Database migrations tested
- [ ] Rollback plan prepared
- [ ] Monitoring alerts configured

### Deployment Steps
1. **Backup Database**:
   ```bash
   pg_dump -h localhost -U itcenter itcenter_auth > backup_$(date +%Y%m%d_%H%M%S).sql
   ```

2. **Deploy Backend**:
   ```bash
   docker-compose pull backend
   docker-compose up -d backend
   ```

3. **Deploy Frontend**:
   ```bash
   docker-compose pull frontend
   docker-compose up -d frontend
   ```

4. **Verify Deployment**:
   ```bash
   curl http://localhost:8080/api/v1/healthz
   curl http://localhost:3000
   ```

### Rollback Procedures
1. **Stop New Services**:
   ```bash
   docker-compose stop backend frontend
   ```

2. **Restore Previous Version**:
   ```bash
   docker-compose up -d backend:previous-version
   docker-compose up -d frontend:previous-version
   ```

3. **Restore Database** (if needed):
   ```bash
   psql -h localhost -U itcenter itcenter_auth < backup_file.sql
   ```

## 🐛 Troubleshooting Guide

### Common Issues

#### 1. Authentication Not Working
**Symptoms**: Users cannot log in, JWT validation fails
**Causes**: 
- Cognito configuration issues
- Token expiration
- Network connectivity

**Solutions**:
```bash
# Check Cognito configuration
aws cognito-idp describe-user-pool --user-pool-id YOUR_POOL_ID

# Verify JWT token
jwt decode YOUR_TOKEN

# Check network connectivity
curl -I https://cognito-idp.us-east-1.amazonaws.com
```

#### 2. Database Connection Issues
**Symptoms**: API returns 500 errors, database timeout
**Causes**:
- Database server down
- Connection pool exhausted
- Network issues

**Solutions**:
```bash
# Check database status
docker-compose exec postgres pg_isready

# Check connections
docker-compose exec postgres psql -U itcenter -c "SELECT count(*) FROM pg_stat_activity;"

# Restart database
docker-compose restart postgres
```

#### 3. Frontend Build Failures
**Symptoms**: Frontend not loading, build errors
**Causes**:
- Node.js version mismatch
- Dependency conflicts
- Environment variables missing

**Solutions**:
```bash
# Clear cache
npm run clean

# Reinstall dependencies
rm -rf node_modules package-lock.json
npm install

# Check environment variables
cat .env.local
```

#### 4. Mobile App Issues
**Symptoms**: App crashes, authentication fails
**Causes**:
- Flutter version mismatch
- API endpoint issues
- Device compatibility

**Solutions**:
```bash
# Clean Flutter cache
flutter clean

# Get dependencies
flutter pub get

# Check API connectivity
flutter run --debug
```

### Performance Issues

#### High Response Times
1. **Check Database Performance**:
   ```sql
   SELECT query, mean_time, calls 
   FROM pg_stat_statements 
   ORDER BY mean_time DESC 
   LIMIT 10;
   ```

2. **Check Application Logs**:
   ```bash
   docker-compose logs backend | grep "slow query"
   ```

3. **Monitor Resource Usage**:
   ```bash
   docker stats
   ```

#### Memory Issues
1. **Check Memory Usage**:
   ```bash
   docker-compose exec backend jps -v
   ```

2. **Check Garbage Collection**:
   ```bash
   docker-compose logs backend | grep "GC"
   ```

3. **Restart Services**:
   ```bash
   docker-compose restart backend
   ```

## 📞 Escalation Procedures

### Level 1 Support
- Basic troubleshooting
- User account issues
- Password resets
- General questions

### Level 2 Support
- Application issues
- Database problems
- Performance issues
- Security concerns

### Level 3 Support
- Critical system failures
- Security incidents
- Data breaches
- Infrastructure issues

### Emergency Contacts
- **On-Call Engineer**: +1-XXX-XXX-XXXX
- **Security Team**: security@itcenter.com
- **Management**: management@itcenter.com

## 📋 Checklists

### Daily Health Check
- [ ] Application health endpoints responding
- [ ] Database connectivity verified
- [ ] Authentication service operational
- [ ] Error logs reviewed
- [ ] Performance metrics within normal range

### Weekly Maintenance
- [ ] Security patches applied
- [ ] Database backup completed
- [ ] Audit logs reviewed
- [ ] User access permissions verified
- [ ] Performance analysis completed

### Monthly Review
- [ ] Security vulnerability scan
- [ ] Dependency updates
- [ ] Capacity planning review
- [ ] Disaster recovery test
- [ ] Documentation updates

---

**Last Updated**: January 2024  
**Version**: 1.0.0  
**Next Review**: February 2024
