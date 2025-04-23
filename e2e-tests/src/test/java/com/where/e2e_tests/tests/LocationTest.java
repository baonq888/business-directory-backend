package com.where.e2e_tests.tests;

import com.where.e2e_tests.utils.EntityContext;
import io.restassured.RestAssured;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LocationTest {
    private static final String BASE_URL = "http://localhost:8222/api/v1/location";
    private static String countryCode = "US"; // Set country code to US, or dynamically fetch from previous test

    private static String cityName;

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = BASE_URL;
    }

    @Test
    @Order(1)
    void testGetCountries() {
        given()
                .when()
                .get("/countries")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0)); // Ensure we get some countries
    }

    @Test
    @Order(2)
    void testGetCitiesByCountry() {

        cityName = given()
                .when()
                .get("/countries/{countryCode}/cities", countryCode)
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0)) // Ensure we get cities
                .body("[0].name", notNullValue()) // Check that city names exist
                .extract()
                .jsonPath()
                .getString("name[0]"); // Save the first city name for future tests
    }

    @Test
    @Order(3)
    void testGetDistrictsByCity() {
        // Fetch districts by city
        var response = given()
                .when()
                .get("/countries/{countryCode}/cities/{cityName}/districts", LocationTest.countryCode, LocationTest.cityName)
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .body("[0].name", notNullValue()) // Check that district names exist
                .extract()
                .jsonPath();

        String districtName = response.getString("name[0]");
        String cityName =  response.getString("adminName1[0]");
        String countryName = response.getString("countryName[0]");
        EntityContext.put("districtName", districtName);
        EntityContext.put("cityName", cityName);
        EntityContext.put("countryName", countryName);
    }


}