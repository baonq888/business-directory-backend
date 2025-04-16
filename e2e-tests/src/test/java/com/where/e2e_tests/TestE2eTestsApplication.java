package com.where.e2e_tests;

import org.springframework.boot.SpringApplication;

public class TestE2eTestsApplication {

	public static void main(String[] args) {
		SpringApplication.from(E2eTestsApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
