package com.where.auth.service;

import com.where.auth.entity.AppUser;
import java.util.List;

public interface AuthService {
    AppUser saveUser(AppUser user);
    AppUser getUser(String username);

}
