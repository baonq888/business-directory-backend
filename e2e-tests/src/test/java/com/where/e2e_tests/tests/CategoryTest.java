package com.where.e2e_tests.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.where.business.entity.Category;
import com.where.e2e_tests.utils.TokenContext;
import com.where.e2e_tests.utils.EntityContext;
import io.restassured.RestAssured;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CategoryTest {

    private static final String BASE_URL = "http://localhost:8222/api/v1/business/category";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = BASE_URL;
    }

    @Test
    @Order(1)
    void createCategory() throws Exception {
        Map<String, Object> request = new HashMap<>();
        request.put("name", "Restaurant");

        var response = given()
                .header("Authorization", "Bearer " + TokenContext.get("admin_access_token"))
                .contentType("application/json")
                .body(objectMapper.writeValueAsString(request))
                .post("/")
                .then()
                .statusCode(201)
                .extract()
                .response();

        Long createdCategoryId = response.jsonPath().getLong("id");

        // Save the created category to the EntityContext
        Category createdCategory = response.as(Category.class);
        EntityContext.put("categoryId", createdCategoryId);
    }

    @Test
    @Order(2)
    void updateCategory() throws Exception {
        // Retrieve the saved category from EntityContext
        Category savedCategory = EntityContext.get("category");

        // Ensure the category was saved in the EntityContext before trying to update
        Assertions.assertNotNull(savedCategory, "Category should be saved in EntityContext");

        Map<String, Object> request = new HashMap<>();
        request.put("name", "Restaurant Updated");

        given()
                .header("Authorization", "Bearer " + TokenContext.get("admin_access_token"))
                .contentType("application/json")
                .body(objectMapper.writeValueAsString(request))
                .put("/" + savedCategory.getId())
                .then()
                .statusCode(200)
                .body("id", equalTo(savedCategory.getId().intValue()))
                .body("name", equalTo("Restaurant Updated"));
    }
}