import React from 'react'
import { Link } from 'react-router-dom'
import { Button } from '../components/ui/button'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '../components/ui/card'
import { Shield, ArrowLeft } from 'lucide-react'

export function ForbiddenPage() {
  return (
    <div className="min-h-screen flex items-center justify-center bg-background">
      <div className="max-w-md w-full text-center space-y-6">
        <div className="space-y-2">
          <Shield className="h-16 w-16 text-destructive mx-auto" />
          <h1 className="text-4xl font-bold text-foreground">403</h1>
          <h2 className="text-2xl font-semibold text-foreground">Access Forbidden</h2>
          <p className="text-muted-foreground">
            You don't have permission to access this resource.
          </p>
        </div>
        
        <Card>
          <CardHeader>
            <CardTitle>Insufficient Permissions</CardTitle>
            <CardDescription>
              This page requires administrative privileges
            </CardDescription>
          </CardHeader>
          <CardContent className="space-y-4">
            <p className="text-sm text-muted-foreground">
              Contact your administrator if you believe you should have access to this page.
            </p>
            <Button asChild className="w-full">
              <Link to="/dashboard">
                Go to Dashboard
              </Link>
            </Button>
            <Button variant="outline" asChild className="w-full">
              <Link to="/profile">
                View Profile
              </Link>
            </Button>
            <Button variant="ghost" onClick={() => window.history.back()}>
              <ArrowLeft className="h-4 w-4 mr-2" />
              Go Back
            </Button>
          </CardContent>
        </Card>
      </div>
    </div>
  )
}
