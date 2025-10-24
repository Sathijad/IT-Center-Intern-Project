import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'dart:io';

import '../models/user.dart';
import '../services/api_service.dart';
import '../services/auth_service.dart';

class AuthProvider extends ChangeNotifier {
  final AuthService _authService = AuthService();
  final FlutterSecureStorage _storage = const FlutterSecureStorage();
  
  User? _user;
  bool _isLoading = false;
  bool _isAuthenticated = false;
  bool _needsMfa = false;
  String? _error;

  User? get user => _user;
  bool get isLoading => _isLoading;
  bool get isAuthenticated => _isAuthenticated;
  bool get needsMfa => _needsMfa;
  String? get error => _error;

  AuthProvider() {
    _initializeAuth();
  }

  Future<void> _initializeAuth() async {
    _setLoading(true);
    try {
      final token = await _storage.read(key: 'auth_token');
      if (token != null) {
        await _validateToken(token);
      }
    } catch (e) {
      _setError('Failed to initialize authentication: $e');
    } finally {
      _setLoading(false);
    }
  }

  Future<bool> login(String email, String password) async {
    _setLoading(true);
    _clearError();
    
    try {
      final result = await _authService.login(email, password);
      
      if (result['success'] == true) {
        final token = result['token'];
        await _storage.write(key: 'auth_token', value: token);
        
        if (result['needsMfa'] == true) {
          _needsMfa = true;
          _isAuthenticated = false;
        } else {
          await _validateToken(token);
        }
        
        return true;
      } else {
        _setError(result['message'] ?? 'Login failed');
        return false;
      }
    } catch (e) {
      _setError('Login failed: $e');
      return false;
    } finally {
      _setLoading(false);
    }
  }

  Future<bool> register(String email, String password, String displayName) async {
    _setLoading(true);
    _clearError();
    
    try {
      final result = await _authService.register(email, password, displayName);
      
      if (result['success'] == true) {
        return true;
      } else {
        _setError(result['message'] ?? 'Registration failed');
        return false;
      }
    } catch (e) {
      _setError('Registration failed: $e');
      return false;
    } finally {
      _setLoading(false);
    }
  }

  Future<bool> verifyMfa(String code) async {
    _setLoading(true);
    _clearError();
    
    try {
      final result = await _authService.verifyMfa(code);
      
      if (result['success'] == true) {
        final token = result['token'];
        await _storage.write(key: 'auth_token', value: token);
        await _validateToken(token);
        _needsMfa = false;
        return true;
      } else {
        _setError(result['message'] ?? 'MFA verification failed');
        return false;
      }
    } catch (e) {
      _setError('MFA verification failed: $e');
      return false;
    } finally {
      _setLoading(false);
    }
  }

  Future<bool> forgotPassword(String email) async {
    _setLoading(true);
    _clearError();
    
    try {
      final result = await _authService.forgotPassword(email);
      
      if (result['success'] == true) {
        return true;
      } else {
        _setError(result['message'] ?? 'Password reset failed');
        return false;
      }
    } catch (e) {
      _setError('Password reset failed: $e');
      return false;
    } finally {
      _setLoading(false);
    }
  }

  Future<void> logout() async {
    _setLoading(true);
    
    try {
      await _authService.logout();
    } catch (e) {
      // Ignore logout errors
    } finally {
      await _storage.delete(key: 'auth_token');
      _user = null;
      _isAuthenticated = false;
      _needsMfa = false;
      _clearError();
      _setLoading(false);
    }
  }

  Future<void> _validateToken(String token) async {
    try {
      final userData = await ApiService.getUserProfile(token);
      _user = User.fromJson(userData);
      _isAuthenticated = true;
      _needsMfa = false;
    } catch (e) {
      await _storage.delete(key: 'auth_token');
      _isAuthenticated = false;
      _setError('Token validation failed: $e');
    }
  }

  Future<void> updateProfile(Map<String, dynamic> data) async {
    if (!_isAuthenticated || _user == null) return;
    
    _setLoading(true);
    _clearError();
    
    try {
      final token = await _storage.read(key: 'auth_token');
      if (token == null) throw Exception('No authentication token');
      
      final updatedUserData = await ApiService.updateUserProfile(token, data);
      _user = User.fromJson(updatedUserData);
      notifyListeners();
    } catch (e) {
      _setError('Profile update failed: $e');
    } finally {
      _setLoading(false);
    }
  }

  void _setLoading(bool loading) {
    _isLoading = loading;
    notifyListeners();
  }

  void _setError(String error) {
    _error = error;
    notifyListeners();
  }

  void _clearError() {
    _error = null;
    notifyListeners();
  }
}
