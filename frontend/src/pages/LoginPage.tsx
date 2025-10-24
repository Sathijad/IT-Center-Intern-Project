import React from 'react'
import { Navigate } from 'react-router-dom'
import { useAuth } from '../contexts/AuthContext'

export function LoginPage() {
  const { isAuthenticated } = useAuth()

  if (isAuthenticated) {
    return <Navigate to="/dashboard" replace />
  }

  return (
    <div className="min-h-screen flex items-center justify-center bg-background">
      <div className="max-w-md w-full space-y-8 p-8">
        <div className="text-center">
          <h2 className="text-3xl font-bold text-foreground">
            Welcome to IT Center
          </h2>
          <p className="mt-2 text-muted-foreground">
            Please sign in to access the admin portal
          </p>
        </div>
        
        <div className="mt-8 space-y-6">
          <div className="bg-card border border-border rounded-lg p-6">
            <h3 className="text-lg font-semibold mb-4">Authentication Required</h3>
            <p className="text-muted-foreground mb-4">
              This application uses AWS Cognito for authentication. Please use the hosted login page to sign in.
            </p>
            <button 
              className="w-full bg-primary text-primary-foreground py-2 px-4 rounded-md hover:bg-primary/90 transition-colors"
              onClick={() => {
                // In a real implementation, this would redirect to Cognito Hosted UI
                window.location.href = '/api/v1/auth/login'
              }}
            >
              Sign In with Cognito
            </button>
          </div>
          
          <div className="text-center text-sm text-muted-foreground">
            <p>Don't have an account? Contact your administrator.</p>
          </div>
        </div>
      </div>
    </div>
  )
}
