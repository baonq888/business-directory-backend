package com.where.user.repository;

import com.where.user.entity.UserDetail;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserDetail,Long>, JpaSpecificationExecutor<UserDetail> {
    Optional<UserDetail> findByEmail(String email);


}
