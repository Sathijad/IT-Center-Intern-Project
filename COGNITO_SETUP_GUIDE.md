# Cognito Authentication Setup Guide

## Issues Fixed

1. **Frontend LoginPage** - Now properly redirects to Cognito Hosted UI instead of backend API
2. **Callback Handling** - Added `/auth/callback` route to handle Cognito authentication responses
3. **Backend Integration** - Added `/auth/cognito/callback` endpoint to exchange authorization codes for tokens
4. **Logout Flow** - Updated logout to properly redirect to Cognito logout page

## Required Cognito Configuration

### 1. App Client Settings

In your AWS Cognito User Pool, configure your app client with these settings:

**Callback URLs:**
```
http://localhost:3000/auth/callback
http://localhost:8080/auth/callback
```

**Sign out URLs:**
```
http://localhost:3000/login
http://localhost:8080/login
```

**Allowed OAuth Flows:**
- Authorization code grant
- Implicit grant

**Allowed OAuth Scopes:**
- openid
- email
- profile

### 2. Domain Configuration

Make sure your Cognito domain is configured as:
```
itcenter-auth.auth.ap-southeast-2.amazoncognito.com
```

## Testing the Authentication Flow

### 1. Start the Services

```bash
# Start backend
cd backend
./mvnw spring-boot:run

# Start frontend (in another terminal)
cd frontend
npm run dev
```

### 2. Test the Flow

1. Navigate to `http://localhost:3000`
2. Click "Sign In with Cognito"
3. You should be redirected to the Cognito hosted login page
4. After successful login, you should be redirected back to `/auth/callback`
5. The callback page will process the authentication and redirect to `/dashboard`

### 3. Expected Behavior

- **Before**: Clicking login redirected to `/api/v1/auth/login` (404 error)
- **After**: Clicking login redirects to Cognito hosted UI
- **Before**: After Cognito login, you saw "This is the default redirect page" error
- **After**: After Cognito login, you're properly redirected back to the app

## Troubleshooting

### If you still see 404 errors:

1. Check that your Cognito app client has the correct callback URLs configured
2. Verify the domain name in `cognito.ts` matches your Cognito domain
3. Ensure the backend is running on port 8080

### If you see "This is the default redirect page":

1. Add the callback URLs to your Cognito app client settings
2. Make sure the URLs match exactly (including protocol and port)

### If authentication fails:

1. Check browser console for errors
2. Verify Cognito client ID and secret in `application.yml`
3. Check backend logs for authentication errors

## Files Modified

- `frontend/src/lib/cognito.ts` - New Cognito utility functions
- `frontend/src/pages/LoginPage.tsx` - Updated to use Cognito hosted UI
- `frontend/src/pages/AuthCallbackPage.tsx` - New callback page
- `frontend/src/App.tsx` - Added callback route
- `frontend/src/contexts/AuthContext.tsx` - Updated logout flow
- `backend/src/main/java/com/itcenter/controller/AuthController.java` - Added callback endpoint
- `backend/src/main/java/com/itcenter/service/AuthService.java` - Added Cognito callback handling
