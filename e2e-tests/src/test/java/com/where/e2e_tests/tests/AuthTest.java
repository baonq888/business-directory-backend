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

        // Store the user for later use
        EntityContext.put(AppUser.class, user);
    }

    @Test
    @Order(2)
    void testAddRoleToUser() {
        AppUser user = EntityContext.get(AppUser.class);

        given()
                .contentType(ContentType.JSON)
                .body("{ \"username\": \"" + user.getEmail() + "\" }")
                .when()
                .post("/user/role/add")
                .then()
                .statusCode(200)
                .body(equalTo("Add role successfully"));
    }


}