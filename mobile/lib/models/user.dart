class User {
  final String userId;
  final String email;
  final String displayName;
  final String locale;
  final DateTime createdAt;
  final DateTime updatedAt;
  final List<String> roles;

  User({
    required this.userId,
    required this.email,
    required this.displayName,
    required this.locale,
    required this.createdAt,
    required this.updatedAt,
    required this.roles,
  });

  factory User.fromJson(Map<String, dynamic> json) {
    return User(
      userId: json['userId'] ?? '',
      email: json['email'] ?? '',
      displayName: json['displayName'] ?? '',
      locale: json['locale'] ?? 'en-US',
      createdAt: DateTime.parse(json['createdAt'] ?? DateTime.now().toIso8601String()),
      updatedAt: DateTime.parse(json['updatedAt'] ?? DateTime.now().toIso8601String()),
      roles: List<String>.from(json['roles'] ?? []),
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'userId': userId,
      'email': email,
      'displayName': displayName,
      'locale': locale,
      'createdAt': createdAt.toIso8601String(),
      'updatedAt': updatedAt.toIso8601String(),
      'roles': roles,
    };
  }

  User copyWith({
    String? userId,
    String? email,
    String? displayName,
    String? locale,
    DateTime? createdAt,
    DateTime? updatedAt,
    List<String>? roles,
  }) {
    return User(
      userId: userId ?? this.userId,
      email: email ?? this.email,
      displayName: displayName ?? this.displayName,
      locale: locale ?? this.locale,
      createdAt: createdAt ?? this.createdAt,
      updatedAt: updatedAt ?? this.updatedAt,
      roles: roles ?? this.roles,
    );
  }

  bool get isAdmin => roles.contains('ADMIN');
  bool get isStaff => roles.contains('STAFF');

  @override
  String toString() {
    return 'User(userId: $userId, email: $email, displayName: $displayName, roles: $roles)';
  }

  @override
  bool operator ==(Object other) {
    if (identical(this, other)) return true;
    return other is User && other.userId == userId;
  }

  @override
  int get hashCode => userId.hashCode;
}
