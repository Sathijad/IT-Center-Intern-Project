import React from 'react';
import { Navigate, Outlet } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';

interface ProtectedRouteProps {
  children?: React.ReactNode;
  requiredRoles?: string[];
}

export function ProtectedRoute({ children, requiredRoles }: ProtectedRouteProps) {
  const { user, isLoading, hasRole } = useAuth();
  const token = localStorage.getItem('auth_token'); // tolerate first render race

  // No token at all? go to login
  if (!token) return <Navigate to="/login" replace />;

  // We have a token but the profile is still loading → show a tiny spinner (avoid redirect flicker)
  if (isLoading && !user) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="animate-spin rounded-full h-10 w-10 border-b-2 border-primary" />
      </div>
    );
  }

  // Role gating only when user is actually loaded
  if (requiredRoles && user && !requiredRoles.some(r => hasRole(r))) {
    return <Navigate to="/403" replace />;
  }

  return children ? <>{children}</> : <Outlet />;
}
