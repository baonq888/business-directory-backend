package com.where.user.repository;

import com.where.user.entity.UserDetail;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserDetail, UUID>, JpaSpecificationExecutor<UserDetail> {
    Optional<UserDetail> findByEmail(String email);


}
