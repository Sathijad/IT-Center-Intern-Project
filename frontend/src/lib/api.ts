import axios, { AxiosInstance, AxiosRequestConfig, AxiosResponse } from 'axios'

// Create axios instance
export const api: AxiosInstance = axios.create({
  baseURL: 'http://localhost:8082/api/v1',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
})

// Request interceptor to add auth token
api.interceptors.request.use(
  (config: AxiosRequestConfig) => {
    const token = localStorage.getItem('auth_token')
    if (token && config.headers) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// Response interceptor for error handling
api.interceptors.response.use(
  (response: AxiosResponse) => {
    return response
  },
  (error) => {
    console.error('API Interceptor: Request failed:', error.response?.status, error.response?.data)
    if (error.response?.status === 401) {
      console.log('API Interceptor: 401 error detected, but not redirecting to avoid loops')
      // Temporarily disabled to prevent redirect loops
      // localStorage.removeItem('auth_token')
      // window.location.href = '/login'
    }
    return Promise.reject(error)
  }
)

// API endpoints
export const authApi = {
  // User profile
  getProfile: () => api.get('/me'),
  updateProfile: (data: any) => api.patch('/me', data),
  
  // User management (Admin)
  getUsers: (params?: any) => api.get('/admin/users', { params }),
  getUser: (userId: string) => api.get(`/admin/users/${userId}`),
  updateUserRoles: (userId: string, data: any) => api.patch(`/admin/users/${userId}/roles`, data),
  
  // Audit logs (Admin)
  getAuditLogs: (params?: any) => api.get('/admin/audit-log', { params }),
  getUserAuditLogs: (userId: string, params?: any) => api.get(`/admin/audit-log/user/${userId}`, { params }),
  getRecentLogins: (userId: string, limit = 10) => api.get(`/admin/audit-log/recent/${userId}?limit=${limit}`),
  
  // Health check
  healthCheck: () => api.get('/healthz'),
}

export default api
