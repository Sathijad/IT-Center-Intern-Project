import React from 'react'
import { useAuth } from '../contexts/AuthContext'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '../components/ui/card'
import { Users, FileText, Shield, Activity } from 'lucide-react'

export function DashboardPage() {
  const { user, hasRole } = useAuth()

  const stats = [
    {
      title: 'Total Users',
      value: '1,234',
      description: 'Active staff members',
      icon: Users,
      color: 'text-blue-600'
    },
    {
      title: 'Admin Users',
      value: '12',
      description: 'Users with admin privileges',
      icon: Shield,
      color: 'text-purple-600'
    },
    {
      title: 'Audit Events',
      value: '5,678',
      description: 'Events logged this month',
      icon: FileText,
      color: 'text-green-600'
    },
    {
      title: 'Active Sessions',
      value: '89',
      description: 'Currently logged in users',
      icon: Activity,
      color: 'text-orange-600'
    }
  ]

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-3xl font-bold text-foreground">Dashboard</h1>
        <p className="text-muted-foreground">
          Welcome back, {user?.displayName}! Here's what's happening in your system.
        </p>
      </div>

      {/* Stats Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        {stats.map((stat) => (
          <Card key={stat.title}>
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">
                {stat.title}
              </CardTitle>
              <stat.icon className={`h-4 w-4 ${stat.color}`} />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold">{stat.value}</div>
              <p className="text-xs text-muted-foreground">
                {stat.description}
              </p>
            </CardContent>
          </Card>
        ))}
      </div>

      {/* Recent Activity */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <Card>
          <CardHeader>
            <CardTitle>Recent Activity</CardTitle>
            <CardDescription>
              Latest events in your system
            </CardDescription>
          </CardHeader>
          <CardContent>
            <div className="space-y-4">
              {[
                { user: 'John Doe', action: 'Updated profile', time: '2 minutes ago' },
                { user: 'Jane Smith', action: 'Logged in', time: '5 minutes ago' },
                { user: 'Admin User', action: 'Assigned role to user', time: '10 minutes ago' },
                { user: 'Mike Johnson', action: 'Password reset', time: '15 minutes ago' },
              ].map((activity, index) => (
                <div key={index} className="flex items-center space-x-4">
                  <div className="w-2 h-2 bg-primary rounded-full"></div>
                  <div className="flex-1">
                    <p className="text-sm font-medium">{activity.user}</p>
                    <p className="text-xs text-muted-foreground">{activity.action}</p>
                  </div>
                  <div className="text-xs text-muted-foreground">
                    {activity.time}
                  </div>
                </div>
              ))}
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle>System Status</CardTitle>
            <CardDescription>
              Current system health and performance
            </CardDescription>
          </CardHeader>
          <CardContent>
            <div className="space-y-4">
              <div className="flex items-center justify-between">
                <span className="text-sm font-medium">API Status</span>
                <span className="text-sm text-green-600">Healthy</span>
              </div>
              <div className="flex items-center justify-between">
                <span className="text-sm font-medium">Database</span>
                <span className="text-sm text-green-600">Connected</span>
              </div>
              <div className="flex items-center justify-between">
                <span className="text-sm font-medium">Cognito</span>
                <span className="text-sm text-green-600">Active</span>
              </div>
              <div className="flex items-center justify-between">
                <span className="text-sm font-medium">Response Time</span>
                <span className="text-sm text-muted-foreground">245ms</span>
              </div>
            </div>
          </CardContent>
        </Card>
      </div>

      {/* Quick Actions */}
      {hasRole('ADMIN') && (
        <Card>
          <CardHeader>
            <CardTitle>Quick Actions</CardTitle>
            <CardDescription>
              Common administrative tasks
            </CardDescription>
          </CardHeader>
          <CardContent>
            <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
              <button className="p-4 border border-border rounded-lg hover:bg-muted transition-colors text-left">
                <h3 className="font-medium">Manage Users</h3>
                <p className="text-sm text-muted-foreground">View and edit user accounts</p>
              </button>
              <button className="p-4 border border-border rounded-lg hover:bg-muted transition-colors text-left">
                <h3 className="font-medium">View Audit Logs</h3>
                <p className="text-sm text-muted-foreground">Review system activity</p>
              </button>
              <button className="p-4 border border-border rounded-lg hover:bg-muted transition-colors text-left">
                <h3 className="font-medium">System Settings</h3>
                <p className="text-sm text-muted-foreground">Configure system parameters</p>
              </button>
            </div>
          </CardContent>
        </Card>
      )}
    </div>
  )
}
