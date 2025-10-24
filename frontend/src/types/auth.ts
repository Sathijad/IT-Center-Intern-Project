// Auth types
export interface UserProfile {
  userId: string
  email: string
  displayName: string
  locale: string
  createdAt: string
  updatedAt: string
  roles: string[]
}

export interface UpdateUserProfile {
  displayName: string
  locale?: string
}

// User management types
export interface UserManagement {
  userId: string
  email: string
  displayName: string
  locale: string
  createdAt: string
  updatedAt: string
  lastLoginAt?: string
  roles: string[]
  active: boolean
}

export interface UpdateUserRoles {
  roles: string[]
}

// Audit log types
export interface AuditLog {
  id: number
  userId: string
  userEmail: string
  userDisplayName: string
  eventType: string
  ipAddress: string
  userAgent: string
  success: boolean
  failureReason?: string
  sessionId: string
  createdAt: string
}

// Pagination types
export interface PageResponse<T> {
  content: T[]
  page: number
  size: number
  totalElements: number
  totalPages: number
  first: boolean
  last: boolean
  hasNext: boolean
  hasPrevious: boolean
  timestamp: string
}

// API error types
export interface ApiError {
  error: string
  message: string
  timestamp: string
  traceId: string
}

// Form types
export interface LoginForm {
  email: string
  password: string
}

export interface SearchParams {
  query?: string
  page?: number
  size?: number
  sort?: string
  direction?: 'asc' | 'desc'
}

export interface AuditLogParams {
  user_id?: string
  event_type?: string
  start_date?: string
  end_date?: string
  page?: number
  size?: number
}

// Event types
export const EVENT_TYPES = {
  LOGIN: 'LOGIN',
  LOGOUT: 'LOGOUT',
  LOGIN_FAILED: 'LOGIN_FAILED',
  MFA_SUCCESS: 'MFA_SUCCESS',
  MFA_FAILED: 'MFA_FAILED',
  PASSWORD_RESET: 'PASSWORD_RESET',
  ROLE_ASSIGNED: 'ROLE_ASSIGNED',
  ROLE_REMOVED: 'ROLE_REMOVED',
  PROFILE_UPDATED: 'PROFILE_UPDATED',
} as const

export type EventType = typeof EVENT_TYPES[keyof typeof EVENT_TYPES]

// Role types
export const ROLES = {
  ADMIN: 'ADMIN',
  STAFF: 'STAFF',
} as const

export type Role = typeof ROLES[keyof typeof ROLES]
