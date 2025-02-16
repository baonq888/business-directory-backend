package com.where.auth.service;

import com.where.auth.entity.AppUser;
import com.where.auth.entity.Role;

import java.util.List;

public interface AuthService {
    AppUser saveUser(AppUser user);
    AppUser getUser(String username);
    Role saveRole(Role role);
    void addBusinessOwnerRoleToUser(String username);
}
