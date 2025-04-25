package com.where.gateway.security;

import com.where.gateway.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.*;

@Component
public class JwtAuthFilter extends AbstractGatewayFilterFactory<JwtAuthFilter.Config> {
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);
    private static final String BEARER_PREFIX = "Bearer ";
    private static final Map<String, Set<HttpMethod>> PUBLIC_PATHS = new HashMap<>();

    static {
        PUBLIC_PATHS.put("/api/v1/auth/login", Set.of(HttpMethod.values()));
        PUBLIC_PATHS.put("/api/v1/auth/register", Set.of(HttpMethod.values()));
        PUBLIC_PATHS.put("/api/v1/auth/token/refresh/", Set.of(HttpMethod.values()));
        PUBLIC_PATHS.put("/api/v1/business", Set.of(HttpMethod.GET));
        PUBLIC_PATHS.put("/api/v1/business/{id}", Set.of(HttpMethod.GET));
    }

    private final JwtUtil jwtUtil;

    public JwtAuthFilter(JwtUtil jwtUtil) {
        super(Config.class);
        this.jwtUtil = jwtUtil;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            HttpMethod method = request.getMethod();
            String path = request.getURI().getPath();

            // Skip authentication for public endpoints
            if (isPublicPath(path,method)) {
                return chain.filter(exchange);
            }

            try {
                extractAndValidateToken(request);
                return chain.filter(exchange);

            } catch (AuthenticationException e) {
                logger.warn("Authentication failed for path: {}, reason: {}", path, e.getMessage());
                return onError(exchange, e.getMessage(), e.getStatus());
            }
        };
    }

    private boolean isPublicPath(String path, HttpMethod method) {
        for (Map.Entry<String, Set<HttpMethod>> entry : PUBLIC_PATHS.entrySet()) {
            if (path.startsWith(entry.getKey())) {
                return entry.getValue().contains(method);
            }
        }
        return false;
    }

    private String extractAndValidateToken(ServerHttpRequest request) throws AuthenticationException {
        List<String> authHeaders = request.getHeaders().getOrEmpty(HttpHeaders.AUTHORIZATION);

        if (authHeaders.isEmpty()) {
            throw new AuthenticationException("Missing Authorization header", HttpStatus.UNAUTHORIZED);
        }

        String token = authHeaders.get(0);
        if (!token.startsWith(BEARER_PREFIX)) {
            throw new AuthenticationException("Invalid Authorization header format", HttpStatus.UNAUTHORIZED);
        }

        token = token.substring(BEARER_PREFIX.length());

        if (token.isEmpty()) {
            throw new AuthenticationException("Empty JWT token", HttpStatus.UNAUTHORIZED);
        }

        if (!jwtUtil.isTokenValid(token)) {
            if (jwtUtil.isTokenExpired(token)) {
                Date expiryDate = jwtUtil.getTokenExpiration(token);
                logger.info("Token for user {} expires at {}", jwtUtil.extractUsername(token), expiryDate);
                throw new AuthenticationException("JWT token has expired", HttpStatus.UNAUTHORIZED);
            } else {
                throw new AuthenticationException("Invalid JWT token", HttpStatus.UNAUTHORIZED);
            }
        }

        return token;
    }

    private Mono<Void> onError(ServerWebExchange exchange, String error, HttpStatus status) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        response.getHeaders().add("X-Error-Message", error);

        // Add CORS headers if needed
        response.getHeaders().add(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");

        logger.error("Authentication error: {}", error);
        return response.setComplete();
    }

    public static class Config {
        // Configuration properties can be added here
    }

    private static class AuthenticationException extends RuntimeException {
        private final HttpStatus status;

        AuthenticationException(String message, HttpStatus status) {
            super(message);
            this.status = status;
        }

        HttpStatus getStatus() {
            return status;
        }
    }
}