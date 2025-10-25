// Cognito configuration and utilities
export const COGNITO_CONFIG = {
  userPoolId: 'ap-southeast-2_UW7Y1ro4I',
  clientId: '42tvdcjmmun7jkir6p317efgsh',
  region: 'ap-southeast-2',
  domain: 'itcenter-auth.auth.ap-southeast-2.amazoncognito.com',
}

// Generate Cognito hosted UI URL
export function getCognitoLoginUrl(redirectUri: string): string {
  const params = new URLSearchParams({
    response_type: 'code',
    client_id: COGNITO_CONFIG.clientId,
    redirect_uri: redirectUri,
    scope: 'openid email profile',
  })

  return `https://${COGNITO_CONFIG.domain}/login?${params.toString()}`
}

// Generate Cognito logout URL
export function getCognitoLogoutUrl(redirectUri: string): string {
  const params = new URLSearchParams({
    client_id: COGNITO_CONFIG.clientId,
    logout_uri: redirectUri,
  })

  return `https://${COGNITO_CONFIG.domain}/logout?${params.toString()}`
}

// Parse authorization code from URL
export function parseAuthorizationCode(): string | null {
  const urlParams = new URLSearchParams(window.location.search)
  return urlParams.get('code')
}

// Parse error from URL
export function parseError(): string | null {
  const urlParams = new URLSearchParams(window.location.search)
  return urlParams.get('error')
}

// Exchange authorization code for tokens
export async function exchangeCodeForTokens(code: string, redirectUri: string): Promise<any> {
  const response = await fetch('/api/v1/auth/cognito/callback', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({
      code,
      redirectUri,
    }),
  })

  if (!response.ok) {
    throw new Error('Failed to exchange code for tokens')
  }

  return response.json()
}
