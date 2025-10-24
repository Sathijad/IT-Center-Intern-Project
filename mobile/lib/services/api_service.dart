import 'package:http/http.dart' as http;
import 'dart:convert';
import 'dart:io';

class ApiService {
  static const String baseUrl = 'http://localhost:8080/api/v1';
  
  static Future<Map<String, dynamic>> getUserProfile(String token) async {
    final response = await http.get(
      Uri.parse('$baseUrl/me'),
      headers: {
        'Authorization': 'Bearer $token',
        'Content-Type': 'application/json',
      },
    );

    if (response.statusCode == 200) {
      return json.decode(response.body);
    } else {
      throw Exception('Failed to get user profile: ${response.statusCode}');
    }
  }

  static Future<Map<String, dynamic>> updateUserProfile(
    String token, 
    Map<String, dynamic> data
  ) async {
    final response = await http.patch(
      Uri.parse('$baseUrl/me'),
      headers: {
        'Authorization': 'Bearer $token',
        'Content-Type': 'application/json',
      },
      body: json.encode(data),
    );

    if (response.statusCode == 200) {
      return json.decode(response.body);
    } else {
      throw Exception('Failed to update user profile: ${response.statusCode}');
    }
  }

  static Future<List<Map<String, dynamic>>> getUsers(
    String token, {
    String? query,
    int page = 0,
    int size = 20,
  }) async {
    final queryParams = <String, String>{
      'page': page.toString(),
      'size': size.toString(),
    };
    
    if (query != null && query.isNotEmpty) {
      queryParams['query'] = query;
    }

    final uri = Uri.parse('$baseUrl/admin/users').replace(
      queryParameters: queryParams,
    );

    final response = await http.get(
      uri,
      headers: {
        'Authorization': 'Bearer $token',
        'Content-Type': 'application/json',
      },
    );

    if (response.statusCode == 200) {
      final data = json.decode(response.body);
      return List<Map<String, dynamic>>.from(data['content'] ?? []);
    } else {
      throw Exception('Failed to get users: ${response.statusCode}');
    }
  }

  static Future<List<Map<String, dynamic>>> getAuditLogs(
    String token, {
    String? userId,
    String? eventType,
    DateTime? startDate,
    DateTime? endDate,
    int page = 0,
    int size = 20,
  }) async {
    final queryParams = <String, String>{
      'page': page.toString(),
      'size': size.toString(),
    };
    
    if (userId != null && userId.isNotEmpty) {
      queryParams['user_id'] = userId;
    }
    
    if (eventType != null && eventType.isNotEmpty) {
      queryParams['event_type'] = eventType;
    }
    
    if (startDate != null) {
      queryParams['start_date'] = startDate.toIso8601String();
    }
    
    if (endDate != null) {
      queryParams['end_date'] = endDate.toIso8601String();
    }

    final uri = Uri.parse('$baseUrl/admin/audit-log').replace(
      queryParameters: queryParams,
    );

    final response = await http.get(
      uri,
      headers: {
        'Authorization': 'Bearer $token',
        'Content-Type': 'application/json',
      },
    );

    if (response.statusCode == 200) {
      final data = json.decode(response.body);
      return List<Map<String, dynamic>>.from(data['content'] ?? []);
    } else {
      throw Exception('Failed to get audit logs: ${response.statusCode}');
    }
  }

  static Future<Map<String, dynamic>> updateUserRoles(
    String token,
    String userId,
    List<String> roles,
  ) async {
    final response = await http.patch(
      Uri.parse('$baseUrl/admin/users/$userId/roles'),
      headers: {
        'Authorization': 'Bearer $token',
        'Content-Type': 'application/json',
      },
      body: json.encode({'roles': roles}),
    );

    if (response.statusCode == 200) {
      return json.decode(response.body);
    } else {
      throw Exception('Failed to update user roles: ${response.statusCode}');
    }
  }

  static Future<Map<String, dynamic>> healthCheck() async {
    final response = await http.get(
      Uri.parse('$baseUrl/healthz'),
      headers: {
        'Content-Type': 'application/json',
      },
    );

    if (response.statusCode == 200) {
      return json.decode(response.body);
    } else {
      throw Exception('Health check failed: ${response.statusCode}');
    }
  }
}
