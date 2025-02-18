package com.where.user.service;

import com.where.user.entity.UserDetail;
import com.where.user.repository.UserRepository;
import com.where.user.specification.UserDetailSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    @Override
    public Page<UserDetail> getUsersByRole(String role, String searchTerm, int page, int size, String sortBy, String sortDirection) {
        try {
            Sort.Direction direction = Sort.Direction.fromString(sortDirection);
            Pageable pageable = PageRequest.of(page,size, Sort.by(direction, sortBy));

            Specification<UserDetail> filterSpec = Specification.where(UserDetailSpecification.hasRole(role));

            filterSpec = filterSpec
                    .and(UserDetailSpecification.hasNameContaining(searchTerm))
                    .or(UserDetailSpecification.hasEmailContaining(searchTerm));
            return userRepository.findAll(filterSpec,pageable);
        } catch (RuntimeException e) {
            throw new RuntimeException("Error getting all users by role", e);
        }

    }
}
