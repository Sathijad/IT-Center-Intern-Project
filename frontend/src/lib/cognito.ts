// cognito.ts — SPA PKCE
export const COGNITO_CONFIG = {
  userPoolId: 'ap-southeast-2_hTAYJId8y',
  clientId: '3rdnl5ind8guti89jrbob85r4i', // SPA client WITHOUT secret
  region: 'ap-southeast-2',
  domain: 'itcenter-auth.auth.ap-southeast-2.amazoncognito.com',
};

// PKCE utils
function b64url(ab: ArrayBuffer): string {
  const b = new Uint8Array(ab);
  let s = '';
  for (let i = 0; i < b.length; i++) s += String.fromCharCode(b[i]);
  return btoa(s).replace(/\+/g, '-').replace(/\//g, '_').replace(/=+$/, '');
}
async function sha256(input: string): Promise<string> {
  const data = new TextEncoder().encode(input);
  const hash = await crypto.subtle.digest('SHA-256', data);
  return b64url(hash);
}
function randomString(len = 64): string {
  const a = new Uint8Array(len);
  crypto.getRandomValues(a);
  return Array.from(a, v => ('0' + v.toString(16)).slice(-2)).join('');
}

// Build login URL (authorize + PKCE)
export async function getCognitoLoginUrl(redirectUri: string): Promise<string> {
  const verifier = randomString(64);
  sessionStorage.setItem('pkce_code_verifier', verifier);
  const challenge = await sha256(verifier);

  const params = new URLSearchParams({
    response_type: 'code',
    client_id: COGNITO_CONFIG.clientId,
    redirect_uri: redirectUri,
    scope: 'openid email profile',
    code_challenge_method: 'S256',
    code_challenge: challenge,
  });
  return `https://${COGNITO_CONFIG.domain}/oauth2/authorize?${params.toString()}`;
}

// Logout URL
export function getCognitoLogoutUrl(redirectUri: string): string {
  const params = new URLSearchParams({
    client_id: COGNITO_CONFIG.clientId,
    logout_uri: redirectUri,
  });
  return `https://${COGNITO_CONFIG.domain}/logout?${params.toString()}`;
}

export function parseAuthorizationCode(): string | null {
  return new URLSearchParams(window.location.search).get('code');
}
export function parseError(): string | null {
  return new URLSearchParams(window.location.search).get('error');
}

// Exchange code directly with Cognito
export async function exchangeCodeForTokens(code: string, redirectUri: string) {
  const verifier = sessionStorage.getItem('pkce_code_verifier');
  if (!verifier) throw new Error('Missing PKCE code_verifier');

  const body = new URLSearchParams({
    grant_type: 'authorization_code',
    client_id: COGNITO_CONFIG.clientId,
    code,
    redirect_uri: redirectUri,
    code_verifier: verifier,
  });

  const resp = await fetch(`https://${COGNITO_CONFIG.domain}/oauth2/token`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    body,
  });

  if (!resp.ok) throw new Error('Failed to exchange code for tokens');
  return resp.json() as Promise<{ access_token: string; id_token: string; refresh_token?: string; expires_in: number; token_type: string }>;
}
