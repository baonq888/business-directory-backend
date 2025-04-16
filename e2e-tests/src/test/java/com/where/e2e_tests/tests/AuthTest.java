package com.where.e2e_tests.tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.where.auth.entity.AppUser;
import com.where.e2e_tests.utils.EntityContext;
import com.where.e2e_tests.utils.TokenContext;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthTest {

    private static final String BASE_URL = "http://localhost:8080/api/v1/auth";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    static void setUp() {
        RestAssured.baseURI = BASE_URL;
    }

    @Test
    @Order(1)
    void testRegisterUser() throws JsonProcessingException {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("email", "testuser@example.com");
        requestMap.put("password", "123");

        String jsonBody = objectMapper.writeValueAsString(requestMap);

        AppUser user =
                given()
                        .contentType(ContentType.JSON)
                        .body(jsonBody)
                        .when()
                        .post("/register")
                        .then()
                        .statusCode(201)
                        .body("email", equalTo("testuser@example.com"))
                        .extract()
                        .as(AppUser.class);

    }

    @Test
    @Order(2)
    void testLoginUser() throws JsonProcessingException {
        Map<String, String> loginPayload = new HashMap<>();
        loginPayload.put("email", "testuser@example.com");
        loginPayload.put("password", "123");

        String jsonBody = objectMapper.writeValueAsString(loginPayload);

        var response =
                given()
                        .contentType(ContentType.JSON)
                        .body(jsonBody)
                        .when()
                        .post("/login")
                        .then()
                        .statusCode(200)
                        .body("access_token", notNullValue())
                        .body("refresh_token", notNullValue())
                        .extract()
                        .response();

        String accessToken = response.jsonPath().getString("access_token");
        String refreshToken = response.jsonPath().getString("refresh_token");

        TokenContext.put("access_token", accessToken);
        TokenContext.put("refresh_token", refreshToken);
    }
}