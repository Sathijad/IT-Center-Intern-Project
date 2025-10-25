import React, { createContext, useContext, useState, useEffect, ReactNode } from 'react'
import { useQuery, useQueryClient } from 'react-query'
import { api } from '../lib/api'
import { UserProfile } from '../types/auth'
import { getCognitoLogoutUrl } from '../lib/cognito'

interface AuthContextType {
  user: UserProfile | null
  isLoading: boolean
  isAuthenticated: boolean
  hasRole: (role: string) => boolean
  login: (token: string) => void
  logout: () => void
  refreshUser: () => void
}

const AuthContext = createContext<AuthContextType | undefined>(undefined)

interface AuthProviderProps {
  children: ReactNode
}

export function AuthProvider({ children }: AuthProviderProps) {
  const [token, setToken] = useState<string | null>(localStorage.getItem('auth_token'))
  const queryClient = useQueryClient()

  // Query to fetch user profile
  const { data: user, isLoading, error, refetch } = useQuery(
    ['user-profile'],
    () => api.get<UserProfile>('/me').then(res => res.data),
    {
      enabled: !!token,
      retry: 2, // Retry twice
      retryDelay: 1000, // Wait 1 second before retry
      onError: (error: any) => {
        console.error('AuthContext: User profile fetch failed:', error)
        // Only logout on persistent 401 errors
        if (error?.response?.status === 401) {
          console.log('AuthContext: 401 error detected')
        }
      }
    }
  )

  const login = (newToken: string) => {
    setToken(newToken)
    localStorage.setItem('auth_token', newToken)
    queryClient.invalidateQueries(['user-profile'])
  }

  const logout = () => {
    setToken(null)
    localStorage.removeItem('auth_token')
    queryClient.clear()
    
    // Call logout endpoint to revoke token
    api.post('/sessions/logout').catch(() => {
      // Ignore errors on logout
    })
    
    // Redirect to Cognito logout
    const redirectUri = `${window.location.origin}/login`
    const logoutUrl = getCognitoLogoutUrl(redirectUri)
    window.location.href = logoutUrl
  }

  const refreshUser = () => {
    refetch()
  }

  const hasRole = (role: string): boolean => {
    return user?.roles?.includes(role) ?? false
  }

  const isAuthenticated = !!token && (!!user || isLoading)

  const value: AuthContextType = {
    user: user ?? null,
    isLoading,
    isAuthenticated,
    hasRole,
    login,
    logout,
    refreshUser,
  }

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  )
}

export function useAuth(): AuthContextType {
  const context = useContext(AuthContext)
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider')
  }
  return context
}
