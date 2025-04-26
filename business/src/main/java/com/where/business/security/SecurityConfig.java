package com.where.business.security;

import com.where.enums.Role;
import com.where.business.filter.CustomAuthorizationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
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
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/business/category/**").hasRole(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.PUT, "/business/{id}").hasAnyRole(Role.BUSINESS_OWNER.name())
                        .requestMatchers(HttpMethod.PATCH, "/business/{id}").hasRole(Role.ADMIN.name())
                        .requestMatchers("/business/review/**").hasRole(Role.USER.name())
                        .anyRequest().authenticated()
                )
                .addFilterBefore(customAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
