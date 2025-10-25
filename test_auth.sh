#!/bin/bash

# Test script to check if the authentication endpoints are working
echo "Testing authentication endpoints..."

# Test login endpoint
echo "Testing login endpoint..."
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@itcenter.com","password":"password"}' \
  -w "\nHTTP Status: %{http_code}\n" \
  -s

echo -e "\n"

# Test health endpoint
echo "Testing health endpoint..."
curl -X GET http://localhost:8080/api/v1/healthz \
  -w "\nHTTP Status: %{http_code}\n" \
  -s

echo -e "\n"

# Test if server is running
echo "Testing if server is running..."
curl -X GET http://localhost:8080/api/v1/actuator/health \
  -w "\nHTTP Status: %{http_code}\n" \
  -s
