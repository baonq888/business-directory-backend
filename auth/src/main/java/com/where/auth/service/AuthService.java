package com.where.auth.service;

import com.where.auth.dto.request.RegisterRequest;
import com.where.auth.entity.AppUser;
import com.where.auth.entity.Role;


public interface AuthService {
    AppUser saveUser(RegisterRequest request, String roleName);
    AppUser getUser(String email);
    Role saveRole(Role role);
    void addBusinessOwnerRoleToUser(String email);
}
