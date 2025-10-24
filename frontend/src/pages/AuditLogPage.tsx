import React, { useState } from 'react'
import { useQuery } from 'react-query'
import { authApi } from '../lib/api'
import { AuditLog, AuditLogParams, EVENT_TYPES } from '../types/auth'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '../components/ui/card'
import { Badge } from '../components/ui/badge'
import { Button } from '../components/ui/button'
import { Input } from '../components/ui/input'
import { 
  Table, 
  TableBody, 
  TableCell, 
  TableHead, 
  TableHeader, 
  TableRow 
} from '../components/ui/table'
import { 
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '../components/ui/select'
import { Search, Filter, Calendar, User, Activity } from 'lucide-react'
import { format } from 'date-fns'

export function AuditLogPage() {
  const [searchParams, setSearchParams] = useState<AuditLogParams>({
    page: 0,
    size: 20
  })

  const { data: auditData, isLoading, error } = useQuery(
    ['audit-logs', searchParams],
    () => authApi.getAuditLogs(searchParams),
    {
      keepPreviousData: true,
    }
  )

  const handleFilterChange = (key: keyof AuditLogParams, value: string) => {
    setSearchParams(prev => ({
      ...prev,
      [key]: value || undefined,
      page: 0
    }))
  }

  const handlePageChange = (page: number) => {
    setSearchParams(prev => ({ ...prev, page }))
  }

  const getEventTypeColor = (eventType: string) => {
    switch (eventType) {
      case 'LOGIN':
        return 'bg-green-100 text-green-800'
      case 'LOGOUT':
        return 'bg-blue-100 text-blue-800'
      case 'LOGIN_FAILED':
        return 'bg-red-100 text-red-800'
      case 'MFA_SUCCESS':
        return 'bg-green-100 text-green-800'
      case 'MFA_FAILED':
        return 'bg-red-100 text-red-800'
      case 'PASSWORD_RESET':
        return 'bg-yellow-100 text-yellow-800'
      case 'ROLE_ASSIGNED':
        return 'bg-purple-100 text-purple-800'
      case 'ROLE_REMOVED':
        return 'bg-orange-100 text-orange-800'
      case 'PROFILE_UPDATED':
        return 'bg-indigo-100 text-indigo-800'
      default:
        return 'bg-gray-100 text-gray-800'
    }
  }

  if (isLoading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary"></div>
      </div>
    )
  }

  if (error) {
    return (
      <div className="text-center py-8">
        <p className="text-destructive">Failed to load audit logs</p>
      </div>
    )
  }

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-3xl font-bold text-foreground">Audit Log</h1>
        <p className="text-muted-foreground">
          Monitor system activity and user actions.
        </p>
      </div>

      {/* Filters */}
      <Card>
        <CardHeader>
          <CardTitle>Filters</CardTitle>
          <CardDescription>
            Filter audit logs by user, event type, and date range
          </CardDescription>
        </CardHeader>
        <CardContent>
          <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
            <div>
              <label className="text-sm font-medium mb-2 block">User ID</label>
              <Input
                placeholder="Filter by user ID..."
                value={searchParams.user_id || ''}
                onChange={(e) => handleFilterChange('user_id', e.target.value)}
              />
            </div>
            
            <div>
              <label className="text-sm font-medium mb-2 block">Event Type</label>
              <Select
                value={searchParams.event_type || ''}
                onValueChange={(value) => handleFilterChange('event_type', value)}
              >
                <SelectTrigger>
                  <SelectValue placeholder="All event types" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="">All event types</SelectItem>
                  {Object.values(EVENT_TYPES).map((eventType) => (
                    <SelectItem key={eventType} value={eventType}>
                      {eventType.replace('_', ' ')}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
            </div>

            <div>
              <label className="text-sm font-medium mb-2 block">Start Date</label>
              <Input
                type="datetime-local"
                value={searchParams.start_date || ''}
                onChange={(e) => handleFilterChange('start_date', e.target.value)}
              />
            </div>

            <div>
              <label className="text-sm font-medium mb-2 block">End Date</label>
              <Input
                type="datetime-local"
                value={searchParams.end_date || ''}
                onChange={(e) => handleFilterChange('end_date', e.target.value)}
              />
            </div>
          </div>
        </CardContent>
      </Card>

      {/* Audit Logs Table */}
      <Card>
        <CardHeader>
          <CardTitle>Audit Events ({auditData?.totalElements || 0})</CardTitle>
          <CardDescription>
            System activity and user action logs
          </CardDescription>
        </CardHeader>
        <CardContent>
          <div className="rounded-md border">
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead>Timestamp</TableHead>
                  <TableHead>User</TableHead>
                  <TableHead>Event Type</TableHead>
                  <TableHead>IP Address</TableHead>
                  <TableHead>Success</TableHead>
                  <TableHead>Details</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {auditData?.content?.map((log: AuditLog) => (
                  <TableRow key={log.id}>
                    <TableCell>
                      <div className="text-sm">
                        {format(new Date(log.createdAt), 'MMM dd, yyyy')}
                      </div>
                      <div className="text-xs text-muted-foreground">
                        {format(new Date(log.createdAt), 'HH:mm:ss')}
                      </div>
                    </TableCell>
                    <TableCell>
                      <div>
                        <div className="font-medium">{log.userDisplayName}</div>
                        <div className="text-sm text-muted-foreground">
                          {log.userEmail}
                        </div>
                      </div>
                    </TableCell>
                    <TableCell>
                      <Badge className={getEventTypeColor(log.eventType)}>
                        {log.eventType.replace('_', ' ')}
                      </Badge>
                    </TableCell>
                    <TableCell>
                      <code className="text-xs bg-muted px-2 py-1 rounded">
                        {log.ipAddress}
                      </code>
                    </TableCell>
                    <TableCell>
                      <Badge variant={log.success ? 'default' : 'destructive'}>
                        {log.success ? 'Success' : 'Failed'}
                      </Badge>
                    </TableCell>
                    <TableCell>
                      {log.failureReason && (
                        <div className="text-sm text-destructive">
                          {log.failureReason}
                        </div>
                      )}
                      {log.userAgent && (
                        <div className="text-xs text-muted-foreground truncate max-w-xs">
                          {log.userAgent}
                        </div>
                      )}
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </div>

          {/* Pagination */}
          {auditData && auditData.totalPages > 1 && (
            <div className="flex items-center justify-between mt-4">
              <div className="text-sm text-muted-foreground">
                Showing {auditData.page * auditData.size + 1} to{' '}
                {Math.min((auditData.page + 1) * auditData.size, auditData.totalElements)} of{' '}
                {auditData.totalElements} results
              </div>
              <div className="flex space-x-2">
                <Button
                  variant="outline"
                  size="sm"
                  disabled={auditData.first}
                  onClick={() => handlePageChange(auditData.page - 1)}
                >
                  Previous
                </Button>
                <Button
                  variant="outline"
                  size="sm"
                  disabled={auditData.last}
                  onClick={() => handlePageChange(auditData.page + 1)}
                >
                  Next
                </Button>
              </div>
            </div>
          )}
        </CardContent>
      </Card>

      {/* Summary Stats */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Total Events</CardTitle>
            <Activity className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{auditData?.totalElements || 0}</div>
            <p className="text-xs text-muted-foreground">
              All audit events
            </p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Failed Events</CardTitle>
            <Activity className="h-4 w-4 text-destructive" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">
              {auditData?.content?.filter(log => !log.success).length || 0}
            </div>
            <p className="text-xs text-muted-foreground">
              Failed authentication attempts
            </p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Unique Users</CardTitle>
            <User className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">
              {new Set(auditData?.content?.map(log => log.userId)).size || 0}
            </div>
            <p className="text-xs text-muted-foreground">
              Users with activity
            </p>
          </CardContent>
        </Card>
      </div>
    </div>
  )
}
