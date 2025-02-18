package com.where.user.security;

import com.where.enums.Role;
import com.where.user.filter.CustomAuthorizationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
    private final CustomAuthorizationFilter customAuthorizationFilter;

    public SecurityConfig(CustomAuthorizationFilter jwtAuthenticationFilter) {
        this.customAuthorizationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/users/{id}").hasAnyRole(Role.USER.name(), Role.BUSINESS_OWNER.name(), Role.ADMIN.name()) // Any authenticated user can get user by ID
                        .requestMatchers(HttpMethod.PUT, "/users/{id}").hasAnyRole(Role.USER.name(), Role.BUSINESS_OWNER.name(), Role.ADMIN.name()) // Any authenticated user can update user
                        .requestMatchers(HttpMethod.PATCH, "/users/{id}").hasAnyRole(Role.USER.name(), Role.BUSINESS_OWNER.name(), Role.ADMIN.name())
                        .requestMatchers("/users/**").hasRole(Role.ADMIN.name()) // Other user endpoints require ADMIN role// Only users with ROLE_ADMIN can access
                        .anyRequest().authenticated()
                )
                .addFilterBefore(customAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
