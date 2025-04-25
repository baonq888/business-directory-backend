package com.where.auth;

import com.where.auth.dto.request.RegisterRequest;
import com.where.auth.service.AuthService;
import com.where.enums.Role;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthApplication.class, args);
	}

	@Bean
	CommandLineRunner run(AuthService authService) {
		return args -> {

			authService.saveUser(
					new RegisterRequest(
							"Admin",
							"admin@example.com",
							"123"
					),
					Role.ADMIN.name());
		};
	}
}
