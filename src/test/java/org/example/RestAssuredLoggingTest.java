package org.example;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import utils.LogUtils;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
public class RestAssuredLoggingTest {

    @BeforeSuite()
    public static void setup() {
    RestAssured.baseURI = "https://jsonplaceholder.typicode.com";
}

    @Test(enabled = true)
    public void testGetRequest() {
        LogUtils.info("Starting GET request test");

        RestAssured
                .given()
                .log().all()
                .when()
                .get("/posts/1")
                .then()
                .log().all()
                .statusCode(200);

        LogUtils.info("Finished GET request test");
    }

    @Test(enabled = true)
    public void verifyTheGetRequest() {
        LogUtils.info("Starting GET request test");

        given()
                .log().all()
                .when()
                .get("/posts/1")
                .then()
                .log().all()
                .statusCode(200)
                .body("id", equalTo(1));

        LogUtils.info("Finished GET request test");
    }

    @Test(enabled = false)
    public void testPostRequest() {
        LogUtils.info("Starting POST request test");

        String requestBody = "{ \"title\": \"foo\", \"body\": \"bar\", \"userId\": 1 }";

       Response response=given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .log().all()
                .when()
                .post("/posts")
                .then()
                .log().all()
                .statusCode(201)
               .extract().response();

       response.then().body("title", equalTo("foo"))
                .body("body", equalTo("bar"))
                .body("userId", equalTo(1));

        LogUtils.info("Finished POST request test");
        LogUtils.info("Response : " + response.asPrettyString());
    }
}