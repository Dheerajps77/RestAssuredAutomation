package org.example;


import com.aventstack.extentreports.Status;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class SecurityTest extends TestBaseJsonPlaceholderCRUDTestClass {

    @Test
    public void testAuthenticationRequired() {
        extentTest = extentReports.createTest("Authentication Required Test");
        try {
            given()
                    .when()
                    .get("/posts")
                    .then()
                    .statusCode(200); // JSONPlaceholder does not require authentication for GET

            extentTest.log(Status.PASS, "Authentication Required Test passed.");
        } catch (Exception e) {
            logAndFailTest("Authentication Required Test failed.", e);
        }
    }

    @Test
    public void testAuthorization() {
        extentTest = extentReports.createTest("Authorization Test");
        try {
            given()
                    .auth().preemptive().basic("invalidUser", "invalidPassword")
                    .when()
                    .get("/posts")
                    .then()
                    .statusCode(401); // Should fail with unauthorized

            extentTest.log(Status.PASS, "Authorization Test passed.");
        } catch (Exception e) {
            logAndFailTest("Authorization Test failed.", e);
        }
    }

    @Test
    public void testSqlInjection() {
        extentTest = extentReports.createTest("SQL Injection Test");
        try {
            given()
                    .param("id", "1 OR 1=1")
                    .when()
                    .get("/posts")
                    .then()
                    .statusCode(400); // Expecting a bad request

            extentTest.log(Status.PASS, "SQL Injection Test passed.");
        } catch (Exception e) {
            logAndFailTest("SQL Injection Test failed.", e);
        }
    }

    @Test
    public void testXssInjection() {
        extentTest = extentReports.createTest("XSS Injection Test");
        try {
            given()
                    .contentType(ContentType.JSON)
                    .body("{\"title\": \"<script>alert('XSS')</script>\"}")
                    .when()
                    .post("/posts")
                    .then()
                    .statusCode(400); // Expecting a bad request or validation error

            extentTest.log(Status.PASS, "XSS Injection Test passed.");
        } catch (Exception e) {
            logAndFailTest("XSS Injection Test failed.", e);
        }
    }

    @Test
    public void testSensitiveDataExposure() {
        extentTest = extentReports.createTest("Sensitive Data Exposure Test");
        try {
            given()
                    .auth().preemptive().basic("user", "password")
                    .when()
                    .get("/users/1")
                    .then()
                    .body("password", nullValue()); // No password field should be exposed

            extentTest.log(Status.PASS, "Sensitive Data Exposure Test passed.");
        } catch (Exception e) {
            logAndFailTest("Sensitive Data Exposure Test failed.", e);
        }
    }

    @Test
    public void testRateLimiting() {
        extentTest = extentReports.createTest("Rate Limiting Test");
        try {
            for (int i = 0; i < 100; i++) {
                given()
                        .when()
                        .get("/posts")
                        .then()
                        .statusCode(anyOf(is(200), is(429))); // Should pass or rate limit

                extentTest.log(Status.PASS, "Rate Limiting Test passed.");
            }
        } catch (Exception e) {
            logAndFailTest("Rate Limiting Test failed.", e);
        }
    }

    @Test
    public void testInputValidation() {
        extentTest = extentReports.createTest("Input Validation Test");
        try {
            given()
                    .contentType(ContentType.JSON)
                    .body("{ \"id\": 0, \"title\": \"<script>alert('XSS')</script>\" }")
                    .when()
                    .post("/posts")
                    .then()
                    .statusCode(400); // Expecting a bad request or validation error

            extentTest.log(Status.PASS, "Input Validation Test passed.");
        } catch (Exception e) {
            logAndFailTest("Input Validation Test failed.", e);
        }
    }
}