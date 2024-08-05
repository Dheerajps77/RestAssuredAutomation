package org.example;

import org.testng.annotations.Test;
import utils.TestBaseForJSONPlaceholderCRUD;

import Reporting.ExtentReportManager;
import com.aventstack.extentreports.Status;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.annotations.Test;
import utils.Log4jFilter;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
public class TestBaseJsonPlaceholderCRUDTestClass extends TestBaseForJSONPlaceholderCRUD {
    private static final String BASE_PATH_POST = "/posts";
    static final int PathParam_POST_ID = 1;

    @Test(enabled = true, priority = 0)
    public void testCreatePost(ITestContext iTestContext) {
        String requestBody = "{ \"title\": \"foo\", \"body\": \"bar\", \"userId\": 1 }";
        try {
            extentTest = extentReports.createTest("Create Posts");
            logger.info("Starting test: Create Post " + iTestContext.getCurrentXmlTest().getName());
            Response response = given()
                    .basePath(BASE_PATH_POST)
                    .contentType(ContentType.JSON)
                    .accept(ContentType.JSON)
                    .body(requestBody)
                    .filter(new Log4jFilter())
                    .when()
                    .post()
                    .then()
                    .assertThat()
                    .statusCode(201)
                    .extract().response();
            logger.info("Post created with ID: " + response.jsonPath().get("id"));
            logger.info("Response : " + response.asPrettyString());

            softAssert.assertEqualsWithLogging(response.getStatusCode(), 201, "Status code should be 201");
            softAssert.assertNotNullWithLogging(response.jsonPath().get("id"), "ID should not be null");

            softAssert.assertAll();
            extentTest.log(Status.INFO, "Post created with ID: " + response.jsonPath().get("id"));
            ExtentReportManager.logJson(response.asPrettyString());
            extentTest.log(Status.PASS, "Create Post test passed");
        } catch (Exception e) {
            logAndFailTest("Error during Create Post test", e);
        }
    }

    @Test(enabled = true, priority = 1, dependsOnMethods = "testCreatePost")
    public void testReadPost() {
        try {
            extentTest = extentReports.createTest("Read Posts");
            logger.info("Starting test: Read Post with ID " + PathParam_POST_ID);
            String fullUrl = baseURI     + BASE_PATH_POST + "/" + PathParam_POST_ID;
            logger.info("Full URL: " + fullUrl);
            Response response = given()
                    .basePath(BASE_PATH_POST)
                    .contentType(ContentType.JSON)
                    .accept(ContentType.JSON)
                    .pathParam("pathId", PathParam_POST_ID)
                    .filter(new Log4jFilter())
                    .when()
                    .get("/{pathId}")
                    .then().assertThat()
                    .statusCode(200).extract().response();

            ExtentReportManager.logJson(response.asPrettyString());
            softAssert.assertEqualsWithLogging(response.getStatusCode(), 200, "Status code should be 200");
            softAssert.assertNotNullWithLogging(response.jsonPath().get("title"), "Title should not be null");

            softAssert.assertAll();
            extentTest.log(Status.PASS, "Read Post test passed");
        } catch (Exception e) {
            logAndFailTest("Error during Read Post test", e);
        }
    }

    @Test(enabled = true, priority = 2, dependsOnMethods = "testReadPost")
    public void testUpdatePost() {
        int postId = 1;
        extentTest = extentReports.createTest("Update Posts");
        String requestBody = "{ \"id\": " + postId + ", \"title\": \"foo_updated\", \"body\": \"bar_updated\", \"userId\": 1 }";
        logger.info("Starting test: Update Post with ID " + postId);
        try {
            Response response = given()
                    .basePath(BASE_PATH_POST)
                    .contentType(ContentType.JSON)
                    .accept(ContentType.JSON)
                    .pathParam("postId", postId)
                    .body(requestBody)
                    .filter(new Log4jFilter())
                    .when()
                    .put("/{postId}")
                    .then()
                    .assertThat()
                    .statusCode(200)
                    .extract().response();
            response.then().body("title", equalTo("foo_updated"))
                    .body("body", equalTo("bar_updated"))
                    .body("userId", equalTo(1));
            ExtentReportManager.logJson(response.asPrettyString());
            extentTest.log(Status.PASS, "Update Post test passed");
        } catch (Exception e) {
            logAndFailTest("Error during Update Post test", e);
        }
    }

    @Test(enabled = true, priority = 3, dependsOnMethods = "testUpdatePost")
    public void testDeletePost() {
        int postId = 1;
        try {
            extentTest = extentReports.createTest("Delete Posts");
            Response response = given()
                    .basePath(BASE_PATH_POST)
                    .contentType(ContentType.JSON)
                    .accept(ContentType.JSON)
                    .pathParam("postId", postId)
                    .filter(new Log4jFilter())
                    .when()
                    .delete("/{postId}")
                    .then().assertThat().statusCode(200).extract().response();
            ExtentReportManager.logJson(response.asPrettyString());
            extentTest.log(Status.PASS, "Delete Post test passed");
        } catch (Exception e) {
            logAndFailTest("Error during Delete Post test", e);
        }
    }
}