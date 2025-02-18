package com.where.auth.service;

import com.where.auth.entity.AppUser;
import com.where.auth.entity.Role;


public interface AuthService {
    AppUser saveUser(AppUser user);
    AppUser getUser(String email);
    Role saveRole(Role role);
    void addBusinessOwnerRoleToUser(String email);
}
