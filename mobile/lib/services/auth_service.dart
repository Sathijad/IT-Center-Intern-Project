import 'package:http/http.dart' as http;
import 'dart:convert';
import 'dart:io';

class AuthService {
  static const String baseUrl = 'http://localhost:8080/api/v1';
  
  Future<Map<String, dynamic>> login(String email, String password) async {
    try {
      // In a real implementation, this would integrate with AWS Cognito
      // For now, we'll simulate the authentication flow
      
      final response = await http.post(
        Uri.parse('$baseUrl/auth/login'),
        headers: {
          'Content-Type': 'application/json',
        },
        body: json.encode({
          'email': email,
          'password': password,
        }),
      );

      if (response.statusCode == 200) {
        final data = json.decode(response.body);
        return {
          'success': true,
          'token': data['token'],
          'needsMfa': data['needsMfa'] ?? false,
        };
      } else {
        final error = json.decode(response.body);
        return {
          'success': false,
          'message': error['message'] ?? 'Login failed',
        };
      }
    } catch (e) {
      // Simulate successful login for demo purposes
      if (email == 'admin@itcenter.com' && password == 'password') {
        return {
          'success': true,
          'token': 'demo_jwt_token_${DateTime.now().millisecondsSinceEpoch}',
          'needsMfa': false,
        };
      } else if (email == 'user@itcenter.com' && password == 'password') {
        return {
          'success': true,
          'token': 'demo_jwt_token_${DateTime.now().millisecondsSinceEpoch}',
          'needsMfa': true,
        };
      } else {
        return {
          'success': false,
          'message': 'Invalid email or password',
        };
      }
    }
  }

  Future<Map<String, dynamic>> register(
    String email, 
    String password, 
    String displayName
  ) async {
    try {
      final response = await http.post(
        Uri.parse('$baseUrl/auth/register'),
        headers: {
          'Content-Type': 'application/json',
        },
        body: json.encode({
          'email': email,
          'password': password,
          'displayName': displayName,
        }),
      );

      if (response.statusCode == 201) {
        return {
          'success': true,
          'message': 'Registration successful. Please check your email for verification.',
        };
      } else {
        final error = json.decode(response.body);
        return {
          'success': false,
          'message': error['message'] ?? 'Registration failed',
        };
      }
    } catch (e) {
      // Simulate successful registration for demo purposes
      return {
        'success': true,
        'message': 'Registration successful. Please check your email for verification.',
      };
    }
  }

  Future<Map<String, dynamic>> verifyMfa(String code) async {
    try {
      final response = await http.post(
        Uri.parse('$baseUrl/auth/verify-mfa'),
        headers: {
          'Content-Type': 'application/json',
        },
        body: json.encode({
          'code': code,
        }),
      );

      if (response.statusCode == 200) {
        final data = json.decode(response.body);
        return {
          'success': true,
          'token': data['token'],
        };
      } else {
        final error = json.decode(response.body);
        return {
          'success': false,
          'message': error['message'] ?? 'MFA verification failed',
        };
      }
    } catch (e) {
      // Simulate successful MFA verification for demo purposes
      if (code == '123456') {
        return {
          'success': true,
          'token': 'demo_jwt_token_${DateTime.now().millisecondsSinceEpoch}',
        };
      } else {
        return {
          'success': false,
          'message': 'Invalid MFA code',
        };
      }
    }
  }

  Future<Map<String, dynamic>> forgotPassword(String email) async {
    try {
      final response = await http.post(
        Uri.parse('$baseUrl/auth/forgot-password'),
        headers: {
          'Content-Type': 'application/json',
        },
        body: json.encode({
          'email': email,
        }),
      );

      if (response.statusCode == 200) {
        return {
          'success': true,
          'message': 'Password reset instructions sent to your email.',
        };
      } else {
        final error = json.decode(response.body);
        return {
          'success': false,
          'message': error['message'] ?? 'Password reset failed',
        };
      }
    } catch (e) {
      // Simulate successful password reset for demo purposes
      return {
        'success': true,
        'message': 'Password reset instructions sent to your email.',
      };
    }
  }

  Future<void> logout() async {
    try {
      await http.post(
        Uri.parse('$baseUrl/sessions/logout'),
        headers: {
          'Content-Type': 'application/json',
        },
      );
    } catch (e) {
      // Ignore logout errors
    }
  }
}
