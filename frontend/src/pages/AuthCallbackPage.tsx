import React, { useEffect, useState } from 'react'
import { Navigate, useNavigate } from 'react-router-dom'
import { useAuth } from '../contexts/AuthContext'
import { parseAuthorizationCode, parseError, exchangeCodeForTokens } from '../lib/cognito'

export function AuthCallbackPage() {
  const { isAuthenticated, login } = useAuth()
  const navigate = useNavigate()
  const [isProcessing, setIsProcessing] = useState(true)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    const processCallback = async () => {
      try {
        // Check for error in URL
        const errorParam = parseError()
        if (errorParam) {
          setError(`Authentication error: ${errorParam}`)
          setIsProcessing(false)
          return
        }

        // Get authorization code
        const code = parseAuthorizationCode()
        if (!code) {
          setError('No authorization code received')
          setIsProcessing(false)
          return
        }

        // Exchange code for tokens
        const redirectUri = `${window.location.origin}/auth/callback`
        const tokenData = await exchangeCodeForTokens(code, redirectUri)
        
        // Store token and redirect - use id_token for authentication
        login(tokenData.id_token)
        navigate('/dashboard', { replace: true })
      } catch (err) {
        console.error('Authentication callback error:', err)
        setError('Failed to complete authentication')
        setIsProcessing(false)
      }
    }

    processCallback()
  }, [login, navigate])

  if (isAuthenticated) {
    return <Navigate to="/dashboard" replace />
  }

  if (error) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-background">
        <div className="max-w-md w-full space-y-8 p-8">
          <div className="text-center">
            <h2 className="text-2xl font-bold text-red-600 mb-4">
              Authentication Error
            </h2>
            <p className="text-muted-foreground mb-6">{error}</p>
            <button
              onClick={() => navigate('/login')}
              className="bg-primary text-primary-foreground py-2 px-4 rounded-md hover:bg-primary/90 transition-colors"
            >
              Try Again
            </button>
          </div>
        </div>
      </div>
    )
  }

  return (
    <div className="min-h-screen flex items-center justify-center bg-background">
      <div className="text-center">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary mx-auto mb-4"></div>
        <h2 className="text-xl font-semibold text-foreground mb-2">
          Completing Authentication
        </h2>
        <p className="text-muted-foreground">
          Please wait while we complete your sign-in...
        </p>
      </div>
    </div>
  )
}
