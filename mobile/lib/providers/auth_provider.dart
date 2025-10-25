import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'dart:io';

import '../models/user.dart';
import '../services/api_service.dart';
import '../services/auth_service.dart';

class AuthState {
  final User? user;
  final bool isLoading;
  final bool isAuthenticated;
  final bool needsMfa;
  final String? error;

  AuthState({
    this.user,
    this.isLoading = false,
    this.isAuthenticated = false,
    this.needsMfa = false,
    this.error,
  });

  AuthState copyWith({
    User? user,
    bool? isLoading,
    bool? isAuthenticated,
    bool? needsMfa,
    String? error,
  }) {
    return AuthState(
      user: user ?? this.user,
      isLoading: isLoading ?? this.isLoading,
      isAuthenticated: isAuthenticated ?? this.isAuthenticated,
      needsMfa: needsMfa ?? this.needsMfa,
      error: error ?? this.error,
    );
  }
}

class AuthNotifier extends StateNotifier<AuthState> {
  final AuthService _authService = AuthService();
  final FlutterSecureStorage _storage = const FlutterSecureStorage();

  AuthNotifier() : super(AuthState()) {
    _initializeAuth();
  }

  Future<void> _initializeAuth() async {
    state = state.copyWith(isLoading: true);
    try {
      final token = await _storage.read(key: 'auth_token');
      if (token != null) {
        await _validateToken(token);
      }
    } catch (e) {
      state = state.copyWith(error: 'Failed to initialize authentication: $e');
    } finally {
      state = state.copyWith(isLoading: false);
    }
  }

  Future<bool> login(String email, String password) async {
    state = state.copyWith(isLoading: true, error: null);
    
    try {
      final result = await _authService.login(email, password);
      
      if (result['success'] == true) {
        final token = result['token'];
        await _storage.write(key: 'auth_token', value: token);
        
        if (result['needsMfa'] == true) {
          state = state.copyWith(needsMfa: true, isAuthenticated: false);
        } else {
          await _validateToken(token);
        }
        
        return true;
      } else {
        state = state.copyWith(error: result['message'] ?? 'Login failed');
        return false;
      }
    } catch (e) {
      state = state.copyWith(error: 'Login failed: $e');
      return false;
    } finally {
      state = state.copyWith(isLoading: false);
    }
  }

  Future<bool> register(String email, String password, String displayName) async {
    state = state.copyWith(isLoading: true, error: null);
    
    try {
      final result = await _authService.register(email, password, displayName);
      
      if (result['success'] == true) {
        return true;
      } else {
        state = state.copyWith(error: result['message'] ?? 'Registration failed');
        return false;
      }
    } catch (e) {
      state = state.copyWith(error: 'Registration failed: $e');
      return false;
    } finally {
      state = state.copyWith(isLoading: false);
    }
  }

  Future<bool> verifyMfa(String code) async {
    state = state.copyWith(isLoading: true, error: null);
    
    try {
      final result = await _authService.verifyMfa(code);
      
      if (result['success'] == true) {
        final token = result['token'];
        await _storage.write(key: 'auth_token', value: token);
        await _validateToken(token);
        state = state.copyWith(needsMfa: false);
        return true;
      } else {
        state = state.copyWith(error: result['message'] ?? 'MFA verification failed');
        return false;
      }
    } catch (e) {
      state = state.copyWith(error: 'MFA verification failed: $e');
      return false;
    } finally {
      state = state.copyWith(isLoading: false);
    }
  }

  Future<bool> forgotPassword(String email) async {
    state = state.copyWith(isLoading: true, error: null);
    
    try {
      final result = await _authService.forgotPassword(email);
      
      if (result['success'] == true) {
        return true;
      } else {
        state = state.copyWith(error: result['message'] ?? 'Password reset failed');
        return false;
      }
    } catch (e) {
      state = state.copyWith(error: 'Password reset failed: $e');
      return false;
    } finally {
      state = state.copyWith(isLoading: false);
    }
  }

  Future<void> logout() async {
    state = state.copyWith(isLoading: true);
    
    try {
      await _authService.logout();
    } catch (e) {
      // Ignore logout errors
    } finally {
      await _storage.delete(key: 'auth_token');
      state = AuthState(); // Reset to initial state
    }
  }

  Future<void> _validateToken(String token) async {
    try {
      final userData = await ApiService.getUserProfile(token);
      final user = User.fromJson(userData);
      state = state.copyWith(
        user: user,
        isAuthenticated: true,
        needsMfa: false,
        error: null,
      );
    } catch (e) {
      await _storage.delete(key: 'auth_token');
      state = state.copyWith(
        isAuthenticated: false,
        error: 'Token validation failed: $e',
      );
    }
  }

  Future<void> updateProfile(Map<String, dynamic> data) async {
    if (!state.isAuthenticated || state.user == null) return;
    
    state = state.copyWith(isLoading: true, error: null);
    
    try {
      final token = await _storage.read(key: 'auth_token');
      if (token == null) throw Exception('No authentication token');
      
      final updatedUserData = await ApiService.updateUserProfile(token, data);
      final user = User.fromJson(updatedUserData);
      state = state.copyWith(user: user);
    } catch (e) {
      state = state.copyWith(error: 'Profile update failed: $e');
    } finally {
      state = state.copyWith(isLoading: false);
    }
  }
}

// Create the provider
final authProvider = StateNotifierProvider<AuthNotifier, AuthState>((ref) {
  return AuthNotifier();
});
