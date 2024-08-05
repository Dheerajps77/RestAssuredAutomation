package org.example;

import io.restassured.http.ContentType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class RestAPITests {

    private static final Logger logger = LogManager.getLogger(RestAPITests.class);

    @Test(description = "To get the details of user with id 3", priority = 0)
    public void verifyUser() {

        logger.info("Starting GET request test");
        // Given
        given()

                // When
                .when()
                .get("https://reqres.in/api/users/3")

                // Then
                .then()
                .statusCode(200)
                .statusLine("HTTP/1.1 200 OK")

                // To verify user of id 3
                .body("data.email", equalTo("emma.wong@reqres.in"))
                .body("data.first_name", equalTo("Emma"))
                .body("data.last_name", equalTo("Wong"));
        logger.info("Finished GET request test");
    }

    @Test(description = "To create a new user", priority = 1)
    public void createUser() {

        logger.info("Starting POST request test");
        JSONObject data = new JSONObject();

        data.put("name", "RestAPITest");
        data.put("job", "Testing");

        // GIVEN
        given()
                .contentType(ContentType.JSON)
                .body(data.toString())

                // WHEN
                .when()
                .post("https://reqres.in/api/users")

                // THEN
                .then()
                .log().all()
                .statusCode(201)
                .body("name", equalTo("RestAPITest"))
                .body("job", equalTo("Testing"));
        logger.info("Finished POST request test");
    }

}