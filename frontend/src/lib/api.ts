import axios, { AxiosInstance, AxiosResponse, AxiosError } from 'axios';
import type { InternalAxiosRequestConfig } from 'axios';

export const api: AxiosInstance = axios.create({
  baseURL: 'http://localhost:8082/api/v1',
  timeout: 10000,
  headers: { 'Content-Type': 'application/json' },
});

// expose a setter so AuthContext can sync axios with current token
export function setAuthToken(token: string | null) {
  if (token) api.defaults.headers.common['Authorization'] = `Bearer ${token}`;
  else delete api.defaults.headers.common['Authorization'];
}

api.interceptors.request.use(
  (config: InternalAxiosRequestConfig) => {
    const token = localStorage.getItem('auth_token');
    if (token) {
      const h = config.headers as any;
      if (h?.set) h.set('Authorization', `Bearer ${token}`);
      else config.headers = { ...(config.headers as any), Authorization: `Bearer ${token}` };
    }
    return config;
  },
  (error: AxiosError) => Promise.reject(error)
);

api.interceptors.response.use(
  (r: AxiosResponse) => r,
  (e: AxiosError) => {
    console.error('API Interceptor: Request failed:', e.response?.status, e.response?.data);
    if (e.response?.status === 401) console.log('API Interceptor: 401 error detected');
    return Promise.reject(e);
  }
);

export const authApi = {
  getProfile: () => api.get('/me'),
  updateProfile: (data: any) => api.patch('/me', data),
  getUsers: (params?: any) => api.get('/admin/users', { params }),
  getUser: (userId: string) => api.get(`/admin/users/${userId}`),
  updateUserRoles: (userId: string, data: any) => api.patch(`/admin/users/${userId}/roles`, data),
  getAuditLogs: (params?: any) => api.get('/admin/audit-log', { params }),
  getUserAuditLogs: (userId: string, params?: any) => api.get(`/admin/audit-log/user/${userId}`, { params }),
  getRecentLogins: (userId: string, limit = 10) => api.get(`/admin/audit-log/recent/${userId}?limit=${limit}`),
  healthCheck: () => api.get('/healthz'),
};

export default api;
