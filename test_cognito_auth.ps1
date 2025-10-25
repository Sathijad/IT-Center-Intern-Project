Write-Host "Testing Cognito Authentication Flow" -ForegroundColor Green
Write-Host "==================================" -ForegroundColor Green

Write-Host "1. Checking if backend is running..." -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "http://localhost:8080/api/v1/healthz" -UseBasicParsing -TimeoutSec 5
    Write-Host "   Backend is running on port 8080" -ForegroundColor Green
} catch {
    Write-Host "   Backend is not running. Please start it with: ./mvnw spring-boot:run" -ForegroundColor Red
    exit 1
}

Write-Host "2. Checking if frontend is running..." -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "http://localhost:3000" -UseBasicParsing -TimeoutSec 5
    Write-Host "   Frontend is running on port 3000" -ForegroundColor Green
} catch {
    Write-Host "   Frontend is not running. Please start it with: npm run dev" -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "Manual testing steps:" -ForegroundColor Cyan
Write-Host "1. Open http://localhost:3000 in your browser" -ForegroundColor White
Write-Host "2. Click Sign In with Cognito" -ForegroundColor White
Write-Host "3. Login with your Cognito credentials" -ForegroundColor White
Write-Host "4. You should be redirected back to the dashboard" -ForegroundColor White
Write-Host "5. You should stay logged in (not redirected back to login)" -ForegroundColor White
Write-Host ""
Write-Host "Expected Cognito callback URLs:" -ForegroundColor Cyan
Write-Host "- http://localhost:3000/auth/callback" -ForegroundColor White
Write-Host "- http://localhost:8080/auth/callback" -ForegroundColor White
Write-Host ""
Write-Host "Expected Cognito sign-out URLs:" -ForegroundColor Cyan
Write-Host "- http://localhost:3000/login" -ForegroundColor White
Write-Host "- http://localhost:8080/login" -ForegroundColor White