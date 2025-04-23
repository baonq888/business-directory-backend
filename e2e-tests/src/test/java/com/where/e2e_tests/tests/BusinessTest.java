package com.where.e2e_tests.tests;

import com.where.business.dto.request.BusinessCreateRequest;
import com.where.business.entity.Business;
import com.where.e2e_tests.utils.EntityContext;
import com.where.e2e_tests.utils.TokenContext;
import io.restassured.RestAssured;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BusinessTest {

    private static final String BASE_URL = "http://localhost:8222/api/v1/business";
    private static String businessId;

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = BASE_URL;
    }

    @Test
    @Order(1)
    void testHealthCheck() {
        given()
                .header("Authorization", "Bearer " + TokenContext.get("access_token"))
                .when()
                .get("/health")
                .then()
                .statusCode(200)
                .body(equalTo("OK"));
    }

    @Test
    @Order(2)
    void testCreateBusiness() {
        BusinessCreateRequest request = getBusinessCreateRequest();

        Business createdBusiness = given()
                .header("Authorization", "Bearer " + TokenContext.get("access_token"))
                .contentType("application/json")
                .body(request)
                .when()
                .post("/")
                .then()
                .statusCode(201)
                .body("name", equalTo("Test Business"))
                .extract().as(Business.class);

        businessId = createdBusiness.getId().toString();
    }

    private static @NotNull BusinessCreateRequest getBusinessCreateRequest() {
        Long categoryId = EntityContext.get("categoryId");
        String cityName = EntityContext.get("cityName");
        String districtName = EntityContext.get("districtId");
        String countryName = EntityContext.get("countryName");

        BusinessCreateRequest request = new BusinessCreateRequest();
        request.setName("Test Business");
        request.setDescription("A test business description.");
        request.setCategoryId(categoryId);
        request.setCityName(cityName);
        request.setDistrictName(districtName);
        request.setCountryName(countryName);
        request.setPhone("123-456-7890");
        request.setEmail("test@business.com");
        request.setWebsite("https://testbusiness.com");
        return request;
    }

    @Test
    @Order(3)
    void testUpdateBusinessStatus() {
        String newStatus = "APPROVED"; // Example status

        given()
                .header("Authorization", "Bearer " + TokenContext.get("access_token"))
                .param("status", newStatus)
                .when()
                .patch("/{id}/status", businessId)
                .then()
                .statusCode(200)
                .body("status", equalTo(newStatus));
    }
}
