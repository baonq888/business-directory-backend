package com.where.user.service;


import com.where.user.entity.UserDetail;
import org.springframework.data.domain.Page;

public interface UserService {
    Page<UserDetail> getUsersByRole(
                                    String role,
                                    String searchTerm,
                                    int page,
                                    int size,
                                    String sortBy,
                                    String sortDirection
    );
}
