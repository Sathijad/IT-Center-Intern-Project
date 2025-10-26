import React, { createContext, useContext, useState, useEffect, ReactNode } from 'react';
import { useQuery, useQueryClient } from 'react-query';
import api, { setAuthToken } from '../lib/api';
import { UserProfile } from '../types/auth';
import { getCognitoLogoutUrl } from '../lib/cognito';

interface AuthContextType {
  user: UserProfile | null;
  isLoading: boolean;
  isAuthenticated: boolean;
  hasRole: (role: string) => boolean;
  login: (accessToken: string) => void;
  logout: () => void;
  refreshUser: () => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export function AuthProvider({ children }: { children: ReactNode }) {
  const [token, setToken] = useState<string | null>(localStorage.getItem('auth_token'));
  const queryClient = useQueryClient();

  useEffect(() => { setAuthToken(token); }, [token]);

  const { data: user, isLoading, error, refetch } = useQuery(
    ['user-profile'],
    () => api.get<UserProfile>('/me').then(r => r.data),
    {
      enabled: !!token,
      retry: 2,
      retryDelay: 1000,
      onError: (err: any) => {
        console.error('AuthContext: User profile fetch failed:', err);
        if (err?.response?.status === 401) console.log('AuthContext: 401 error detected');
      },
    }
  );

  const login = (accessToken: string) => {
    setToken(accessToken);
    localStorage.setItem('auth_token', accessToken);
    queryClient.invalidateQueries(['user-profile']);
  };

  const logout = () => {
    setToken(null);
    localStorage.removeItem('auth_token');
    queryClient.clear();

    // optional backend logout (ignore if not implemented)
    api.post('/sessions/logout').catch(() => {});

    const redirectUri = `${window.location.origin}/login`;
    window.location.href = getCognitoLogoutUrl(redirectUri);
  };

  const refreshUser = () => { refetch(); };
  const hasRole = (role: string) => user?.roles?.includes(role) ?? false;
  const isAuthenticated = !!token;

  return (
    <AuthContext.Provider value={{ user: user ?? null, isLoading, isAuthenticated, hasRole, login, logout, refreshUser }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth(): AuthContextType {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error('useAuth must be used within an AuthProvider');
  return ctx;
}
