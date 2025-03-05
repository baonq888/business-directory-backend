package com.where.gateway.config;

import com.where.gateway.security.JwtAuthFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    private final JwtAuthFilter jwtAuthenticationFilter;

    public GatewayConfig(JwtAuthFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }


    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth-service", r -> r
                        .path("/api/v1/auth/**")
                        .uri("lb://AUTH-SERVICE"))
                .route("location-service", r -> r
                        .path("/api/v1/location/**")
                        .uri("lb://LOCATION-SERVICE"))
                .route("user-service", r -> r
                        .path("/api/v1/users/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter.apply(new JwtAuthFilter.Config())))
                        .uri("lb://USER-SERVICE"))
                .route("business-service", r -> r
                        .path("/api/v1/business/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter.apply(new JwtAuthFilter.Config())))
                        .uri("lb://BUSINESS-SERVICE"))
                .build();
    }
}
