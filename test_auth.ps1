# Test script to check if the authentication endpoints are working
Write-Host "Testing authentication endpoints..." -ForegroundColor Green

# Test login endpoint
Write-Host "Testing login endpoint..." -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "http://localhost:8080/api/v1/auth/login" -Method POST -Headers @{"Content-Type"="application/json"} -Body '{"email":"admin@itcenter.com","password":"password"}' -ErrorAction Stop
    Write-Host "Login Response: $($response.Content)" -ForegroundColor Green
    Write-Host "HTTP Status: $($response.StatusCode)" -ForegroundColor Green
} catch {
    Write-Host "Login Error: $($_.Exception.Message)" -ForegroundColor Red
    Write-Host "HTTP Status: $($_.Exception.Response.StatusCode)" -ForegroundColor Red
}

Write-Host ""

# Test health endpoint
Write-Host "Testing health endpoint..." -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "http://localhost:8080/api/v1/healthz" -Method GET -ErrorAction Stop
    Write-Host "Health Response: $($response.Content)" -ForegroundColor Green
    Write-Host "HTTP Status: $($response.StatusCode)" -ForegroundColor Green
} catch {
    Write-Host "Health Error: $($_.Exception.Message)" -ForegroundColor Red
    Write-Host "HTTP Status: $($_.Exception.Response.StatusCode)" -ForegroundColor Red
}

Write-Host ""

# Test if server is running
Write-Host "Testing if server is running..." -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "http://localhost:8080/api/v1/actuator/health" -Method GET -ErrorAction Stop
    Write-Host "Actuator Response: $($response.Content)" -ForegroundColor Green
    Write-Host "HTTP Status: $($response.StatusCode)" -ForegroundColor Green
} catch {
    Write-Host "Actuator Error: $($_.Exception.Message)" -ForegroundColor Red
    Write-Host "HTTP Status: $($_.Exception.Response.StatusCode)" -ForegroundColor Red
}
