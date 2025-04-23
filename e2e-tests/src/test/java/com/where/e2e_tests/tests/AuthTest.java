package com.where.e2e_tests.tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    private static final String BASE_URL = "http://localhost:8222/api/v1/auth";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = BASE_URL;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

    }

    @Test
    @Order(1)
    void testRegisterUser() throws JsonProcessingException {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("name", "Test User");
        requestMap.put("email", "testuser@example.com");
        requestMap.put("password", "123");

        String jsonBody = objectMapper.writeValueAsString(requestMap);

        var response =
                given()
                        .contentType(ContentType.JSON)
                        .body(jsonBody)
                        .log().all() // Logs the request
                        .when()
                        .post("/register")
                        .then()
                        .log().all() // Logs the response
                        .extract()
                        .response();

        int statusCode = response.getStatusCode();
        String responseBody = response.getBody().asPrettyString();

        System.out.println("Status Code: " + statusCode);
        System.out.println("Response Body: " + responseBody);

        Assertions.assertEquals(201, statusCode, "Registration failed with response: " + responseBody);
    }

    @Test
    @Order(2)
    void testLoginUser() throws JsonProcessingException {
        Map<String, String> users = new HashMap<>();
        users.put("testuser@example.com", "123");
        users.put("admin@example.com", "123");

        for (Map.Entry<String, String> entry : users.entrySet()) {
            String email = entry.getKey();
            String password = entry.getValue();

            Map<String, String> loginPayload = new HashMap<>();
            loginPayload.put("email", email);
            loginPayload.put("password", password);

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

            if (email.equals("admin@example.com")) {
                TokenContext.put("admin_access_token", accessToken);
                TokenContext.put("admin_refresh_token", refreshToken);
            } else {
                TokenContext.put("access_token", accessToken);
                TokenContext.put("refresh_token", refreshToken);
            }
        }
    }
}