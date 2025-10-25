#!/bin/bash

# Test script for Cognito authentication flow
echo "Testing Cognito Authentication Flow"
echo "=================================="

# Check if backend is running
echo "1. Checking if backend is running..."
if curl -s http://localhost:8080/api/v1/healthz > /dev/null; then
    echo "   ✓ Backend is running on port 8080"
else
    echo "   ✗ Backend is not running. Please start it with: ./mvnw spring-boot:run"
    exit 1
fi

# Check if frontend is running
echo "2. Checking if frontend is running..."
if curl -s http://localhost:3000 > /dev/null; then
    echo "   ✓ Frontend is running on port 3000"
else
    echo "   ✗ Frontend is not running. Please start it with: npm run dev"
    exit 1
fi

echo ""
echo "3. Testing authentication endpoints..."

# Test Cognito callback endpoint
echo "   Testing /auth/cognito/callback endpoint..."
response=$(curl -s -X POST http://localhost:8080/api/v1/auth/cognito/callback \
  -H "Content-Type: application/json" \
  -d '{"code":"test","redirectUri":"http://localhost:3000/auth/callback"}')

if echo "$response" | grep -q "error"; then
    echo "   ✓ Callback endpoint is working (returns error for invalid code as expected)"
else
    echo "   ✗ Callback endpoint may not be working properly"
fi

echo ""
echo "4. Manual testing steps:"
echo "   1. Open http://localhost:3000 in your browser"
echo "   2. Click 'Sign In with Cognito'"
echo "   3. Login with your Cognito credentials"
echo "   4. You should be redirected back to the dashboard"
echo "   5. You should stay logged in (not redirected back to login)"
echo ""
echo "5. If you still get redirected to login:"
echo "   - Check browser console for errors"
echo "   - Check backend logs for authentication errors"
echo "   - Verify Cognito app client callback URLs are configured correctly"
echo ""
echo "Expected Cognito callback URLs:"
echo "   - http://localhost:3000/auth/callback"
echo "   - http://localhost:8080/auth/callback"
echo ""
echo "Expected Cognito sign-out URLs:"
echo "   - http://localhost:3000/login"
echo "   - http://localhost:8080/login"
