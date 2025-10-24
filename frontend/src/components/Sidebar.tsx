import React from 'react'
import { Link, useLocation } from 'react-router-dom'
import { 
  LayoutDashboard, 
  Users, 
  FileText, 
  User, 
  Settings,
  Shield
} from 'lucide-react'
import { useAuth } from '../contexts/AuthContext'
import { cn } from '../lib/utils'

const navigation = [
  {
    name: 'Dashboard',
    href: '/dashboard',
    icon: LayoutDashboard,
    roles: ['ADMIN', 'STAFF']
  },
  {
    name: 'Profile',
    href: '/profile',
    icon: User,
    roles: ['ADMIN', 'STAFF']
  },
  {
    name: 'User Management',
    href: '/admin/users',
    icon: Users,
    roles: ['ADMIN']
  },
  {
    name: 'Audit Log',
    href: '/admin/audit',
    icon: FileText,
    roles: ['ADMIN']
  },
]

export function Sidebar() {
  const location = useLocation()
  const { hasRole } = useAuth()

  const filteredNavigation = navigation.filter(item => 
    item.roles.some(role => hasRole(role))
  )

  return (
    <div className="w-64 bg-card border-r border-border">
      <div className="p-6">
        <div className="flex items-center space-x-2">
          <Shield className="h-8 w-8 text-primary" />
          <h1 className="text-xl font-bold">IT Center</h1>
        </div>
        <p className="text-sm text-muted-foreground mt-1">Admin Portal</p>
      </div>
      
      <nav className="px-4 pb-4">
        <ul className="space-y-2">
          {filteredNavigation.map((item) => {
            const isActive = location.pathname === item.href
            return (
              <li key={item.name}>
                <Link
                  to={item.href}
                  className={cn(
                    'flex items-center space-x-3 px-3 py-2 rounded-md text-sm font-medium transition-colors',
                    isActive
                      ? 'bg-primary text-primary-foreground'
                      : 'text-muted-foreground hover:text-foreground hover:bg-muted'
                  )}
                >
                  <item.icon className="h-4 w-4" />
                  <span>{item.name}</span>
                </Link>
              </li>
            )
          })}
        </ul>
      </nav>
    </div>
  )
}
