import React, { useState } from 'react'
import { useQuery, useMutation, useQueryClient } from 'react-query'
import { authApi } from '../lib/api'
import { UserManagement, SearchParams, UpdateUserRoles } from '../types/auth'
import { Button } from '../components/ui/button'
import { Input } from '../components/ui/input'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '../components/ui/card'
import { Badge } from '../components/ui/badge'
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
import { Search, Filter, MoreHorizontal, Edit, Shield } from 'lucide-react'
import { format } from 'date-fns'

export function UserManagementPage() {
  const queryClient = useQueryClient()
  const [searchParams, setSearchParams] = useState<SearchParams>({
    page: 0,
    size: 20,
    sort: 'createdAt',
    direction: 'desc'
  })

  const { data: usersData, isLoading, error } = useQuery(
    ['users', searchParams],
    () => authApi.getUsers(searchParams),
    {
      keepPreviousData: true,
    }
  )

  const updateRolesMutation = useMutation(
    ({ userId, roles }: { userId: string, roles: string[] }) => 
      authApi.updateUserRoles(userId, { roles }),
    {
      onSuccess: () => {
        queryClient.invalidateQueries(['users'])
      }
    }
  )

  const handleSearch = (query: string) => {
    setSearchParams(prev => ({ ...prev, query, page: 0 }))
  }

  const handlePageChange = (page: number) => {
    setSearchParams(prev => ({ ...prev, page }))
  }

  const handleSort = (sort: string) => {
    setSearchParams(prev => ({
      ...prev,
      sort,
      direction: prev.sort === sort && prev.direction === 'asc' ? 'desc' : 'asc'
    }))
  }

  const handleRoleUpdate = (userId: string, newRoles: string[]) => {
    updateRolesMutation.mutate({ userId, roles: newRoles })
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
        <p className="text-destructive">Failed to load users</p>
      </div>
    )
  }

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-3xl font-bold text-foreground">User Management</h1>
        <p className="text-muted-foreground">
          Manage user accounts and role assignments.
        </p>
      </div>

      {/* Search and Filters */}
      <Card>
        <CardHeader>
          <CardTitle>Search & Filter</CardTitle>
          <CardDescription>
            Find users by name or email address
          </CardDescription>
        </CardHeader>
        <CardContent>
          <div className="flex space-x-4">
            <div className="flex-1">
              <div className="relative">
                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-muted-foreground" />
                <Input
                  placeholder="Search by name or email..."
                  value={searchParams.query || ''}
                  onChange={(e) => handleSearch(e.target.value)}
                  className="pl-10"
                />
              </div>
            </div>
            <Button variant="outline">
              <Filter className="h-4 w-4 mr-2" />
              Filters
            </Button>
          </div>
        </CardContent>
      </Card>

      {/* Users Table */}
      <Card>
        <CardHeader>
          <CardTitle>Users ({usersData?.totalElements || 0})</CardTitle>
          <CardDescription>
            Manage user accounts and permissions
          </CardDescription>
        </CardHeader>
        <CardContent>
          <div className="rounded-md border">
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead 
                    className="cursor-pointer hover:bg-muted"
                    onClick={() => handleSort('displayName')}
                  >
                    User
                  </TableHead>
                  <TableHead 
                    className="cursor-pointer hover:bg-muted"
                    onClick={() => handleSort('email')}
                  >
                    Email
                  </TableHead>
                  <TableHead>Roles</TableHead>
                  <TableHead 
                    className="cursor-pointer hover:bg-muted"
                    onClick={() => handleSort('createdAt')}
                  >
                    Created
                  </TableHead>
                  <TableHead 
                    className="cursor-pointer hover:bg-muted"
                    onClick={() => handleSort('lastLoginAt')}
                  >
                    Last Login
                  </TableHead>
                  <TableHead>Status</TableHead>
                  <TableHead className="w-[50px]"></TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {usersData?.content?.map((user: UserManagement) => (
                  <TableRow key={user.userId}>
                    <TableCell>
                      <div>
                        <div className="font-medium">{user.displayName}</div>
                        <div className="text-sm text-muted-foreground">
                          {user.userId}
                        </div>
                      </div>
                    </TableCell>
                    <TableCell>{user.email}</TableCell>
                    <TableCell>
                      <div className="flex flex-wrap gap-1">
                        {user.roles.map((role) => (
                          <Badge 
                            key={role} 
                            variant={role === 'ADMIN' ? 'default' : 'secondary'}
                          >
                            {role}
                          </Badge>
                        ))}
                      </div>
                    </TableCell>
                    <TableCell>
                      {format(new Date(user.createdAt), 'MMM dd, yyyy')}
                    </TableCell>
                    <TableCell>
                      {user.lastLoginAt 
                        ? format(new Date(user.lastLoginAt), 'MMM dd, yyyy')
                        : 'Never'
                      }
                    </TableCell>
                    <TableCell>
                      <Badge variant={user.active ? 'default' : 'destructive'}>
                        {user.active ? 'Active' : 'Inactive'}
                      </Badge>
                    </TableCell>
                    <TableCell>
                      <Button variant="ghost" size="sm">
                        <MoreHorizontal className="h-4 w-4" />
                      </Button>
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </div>

          {/* Pagination */}
          {usersData && usersData.totalPages > 1 && (
            <div className="flex items-center justify-between mt-4">
              <div className="text-sm text-muted-foreground">
                Showing {usersData.page * usersData.size + 1} to{' '}
                {Math.min((usersData.page + 1) * usersData.size, usersData.totalElements)} of{' '}
                {usersData.totalElements} results
              </div>
              <div className="flex space-x-2">
                <Button
                  variant="outline"
                  size="sm"
                  disabled={usersData.first}
                  onClick={() => handlePageChange(usersData.page - 1)}
                >
                  Previous
                </Button>
                <Button
                  variant="outline"
                  size="sm"
                  disabled={usersData.last}
                  onClick={() => handlePageChange(usersData.page + 1)}
                >
                  Next
                </Button>
              </div>
            </div>
          )}
        </CardContent>
      </Card>
    </div>
  )
}
