import React, { useEffect, useState } from 'react';
import { Navigate, useNavigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import { parseAuthorizationCode, parseError, exchangeCodeForTokens } from '../lib/cognito';
import { setAuthToken } from '../lib/api';

export function AuthCallbackPage() {
  const { isAuthenticated, login } = useAuth();
  const navigate = useNavigate();
  const [isProcessing, setIsProcessing] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const go = async () => {
      try {
        const errParam = parseError();
        if (errParam) throw new Error(`Authentication error: ${errParam}`);
  
        const code = parseAuthorizationCode();
        if (!code) throw new Error('No authorization code received');
  
        const redirectUri = `${window.location.origin}/auth/callback`;
        const tokens = await exchangeCodeForTokens(code, redirectUri);
  
        // Store tokens
        localStorage.setItem('auth_token', tokens.access_token);
        setAuthToken(tokens.access_token); // <<< ensures first API call has Authorization
        localStorage.setItem('id_token', tokens.id_token);
        if (tokens.refresh_token) localStorage.setItem('refresh_token', tokens.refresh_token);
        const expAt = Date.now() + (tokens.expires_in - 5) * 1000;
        localStorage.setItem('token_exp', String(expAt));
  
        // Let context know (if your AuthContext tracks it)
        login(tokens.access_token);
  
        // land on the original page or /dashboard
        const ret = sessionStorage.getItem('return_to') || '/dashboard';
        sessionStorage.removeItem('return_to');
        window.location.replace(ret);
      } catch (e: any) {
        console.error('Authentication callback error:', e);
        setError(e?.message || 'Failed to complete authentication');
        setIsProcessing(false);
      }
    };
    go();
  }, [login, navigate]);

  if (isAuthenticated) return <Navigate to="/dashboard" replace />;

  if (error) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-background">
        <div className="max-w-md w-full space-y-8 p-8 text-center">
          <h2 className="text-2xl font-bold text-red-600 mb-4">Authentication Error</h2>
          <p className="text-muted-foreground mb-6">{error}</p>
          <button onClick={() => navigate('/login')} className="bg-primary text-primary-foreground py-2 px-4 rounded-md hover:bg-primary/90 transition-colors">
            Try Again
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen flex items-center justify-center bg-background">
      <div className="text-center">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary mx-auto mb-4" />
        <h2 className="text-xl font-semibold text-foreground mb-2">Completing Authentication</h2>
        <p className="text-muted-foreground">Please wait while we complete your sign-in...</p>
      </div>
    </div>
  );
}
