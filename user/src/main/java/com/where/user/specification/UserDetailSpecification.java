package com.where.user.specification;


import com.where.user.entity.Role;
import com.where.user.entity.UserDetail;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class UserDetailSpecification {

    public static Specification<UserDetail> hasRole(String role) {
        return (root, query, criteriaBuilder) -> {
            Join<UserDetail, Role> roleJoin = root.join("roles");
            return criteriaBuilder.equal(roleJoin.get("name"), role);
        };
    }

    public static Specification<UserDetail> hasNameContaining(String name) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),"%"+name.toLowerCase()+"%" ));
    }

    public static Specification<UserDetail> hasEmailContaining(String email) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("email")),"%"+email.toLowerCase()+"%" ));
    }
}
