package org.example;

import Reporting.ExtentReportManager;
import Reporting.Setup;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.Log4jFilter;
import utils.LoggingSoftAssert;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class JsonPlaceholderCRUDWithLogginMessage {

    private static final String BASE_URI = "https://jsonplaceholder.typicode.com";
    private static final String BASE_PATH_POST = "/posts";
    private static final Logger logger = LogManager.getLogger(JsonPlaceholderCRUD.class);
    static final int PathParam_POST_ID = 1;
    private ExtentReports extentReports;
    private ExtentTest extentTest;
    private String reportName;
    private LoggingSoftAssert softAssert = new LoggingSoftAssert();

    @BeforeClass
    public void setUp() {
        baseURI = BASE_URI;
        String timestamp = ExtentReportManager.getReportNameWithTimeStamp();
        String className = this.getClass().getSimpleName();
        reportName = String.format("ExtentReport_%s_%s", className, timestamp);

        extentReports = ExtentReportManager.createInstance(reportName,
                "JSON Placeholder API Test Report - " + className,
                "JSON Placeholder API Test Report");
    }

    @BeforeMethod
    public void setUpTest(ITestResult result) {
        String methodName = "HTTP Method : " + result.getMethod().getMethodName();
        Setup.extentTest.set(extentReports.createTest(methodName));
        logger.info("Before HTTP Method: {}", methodName);
        ExtentReportManager.logJson("Before HTTP Method: " + methodName);
    }

    @Test(enabled = true, priority = 0)
    public void testCreatePost(ITestContext iTestContext) {
        String requestBody = "{ \"title\": \"foo\", \"body\": \"bar\", \"userId\": 1 }";
        try {
            String testName = "Create Posts";
            Setup.extentTest.set(extentReports.createTest(testName));
            logger.info("Starting test: {} {}", testName, iTestContext.getCurrentXmlTest().getName());
            logger.info("Start date of test: {}", iTestContext.getStartDate());
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

            String postId = String.valueOf(response.jsonPath().get("id"));
            logger.info("Post created with ID: {}", postId);
            logger.info("Response: {}", response.asPrettyString());

            softAssert.assertEqualsWithLogging(response.getStatusCode(), 201, "Status code should be 201");
            softAssert.assertNotNullWithLogging(response.jsonPath().get("id"), "ID should not be null");
            softAssert.assertAll();

            Setup.extentTest.get().log(Status.INFO, "Post created with ID: " + postId);
            ExtentReportManager.logJson(response.asPrettyString());
            Setup.extentTest.get().log(Status.PASS, "Create Post test passed");
        } catch (Exception e) {
            logger.error("Error during Create Post test: ", e);
            Setup.extentTest.get().log(Status.FAIL, "Error during Create Post test: " + e.getMessage());
            throw e;
        }
    }

    @Test(enabled = true, priority = 1, dependsOnMethods = "testCreatePost")
    public void testReadPost() {
        try {
            String testName = "Read Posts";
            Setup.extentTest.set(extentReports.createTest(testName));
            logger.info("Starting test: {} with ID {}", testName, PathParam_POST_ID);
            String fullUrl = baseURI + BASE_PATH_POST + "/" + PathParam_POST_ID;
            logger.info("Full URL: {}", fullUrl);
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

            Setup.extentTest.get().log(Status.PASS, "Read Post test passed");
        } catch (Exception e) {
            logger.error("Error during Read Post test: ", e);
            Setup.extentTest.get().log(Status.FAIL, "Error during Read Post test: " + e.getMessage());
            throw e;
        }
    }

    @Test(enabled = true, priority = 2, dependsOnMethods = "testReadPost")
    public void testUpdatePost() {
        int postId = 1;
        String requestBody = "{ \"id\": " + postId + ", \"title\": \"foo_updated\", \"body\": \"bar_updated\", \"userId\": 1 }";
        try {
            String testName = "Update Posts";
            Setup.extentTest.set(extentReports.createTest(testName));
            logger.info("Starting test: {} with ID {}", testName, postId);
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
            Setup.extentTest.get().log(Status.PASS, "Update Post test passed");
        } catch (Exception e) {
            logger.error("Error during Update Post test: ", e);
            Setup.extentTest.get().log(Status.FAIL, "Error during Update Post test: " + e.getMessage());
            throw e;
        }
    }

    @Test(enabled = true, priority = 3, dependsOnMethods = "testUpdatePost")
    public void testDeletePost() {
        int postId = 1;
        try {
            String testName = "Delete Posts";
            Setup.extentTest.set(extentReports.createTest(testName));
            logger.info("Starting test: {} with ID {}", testName, postId);
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
            Setup.extentTest.get().log(Status.PASS, "Delete Post test passed");
        } catch (Exception e) {
            logger.error("Error during Delete Post test: ", e);
            Setup.extentTest.get().log(Status.FAIL, "Error during Delete Post test: " + e.getMessage());
            throw e;
        }
    }

    @Test(enabled = true, priority = 4)
    public void testFailingCreatePost(ITestContext iTestContext) {
        String requestBody = "{ \"title\": \"foo\", \"body\": \"bar\" }"; // Missing userId
        try {
            String testName = "Test Failing - Create Posts";
            Setup.extentTest.set(extentReports.createTest(testName));
            logger.info("Starting test: {}", testName);
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
                    .statusCode(400) // Expecting failure
                    .extract().response();

            ExtentReportManager.logJson(response.asPrettyString());
            logger.info("Response: {}", response.asPrettyString());
            Setup.extentTest.get().log(Status.PASS, "Failing Create Post test passed");
        } catch (Exception e) {
            logger.error("Error during Failing Create Post test: ", e);
            Setup.extentTest.get().log(Status.FAIL, "Error during Failing Create Post test: " + e.getMessage());
            throw e;
        }
    }

    @Test(enabled = true, priority = 5)
    public void testFailingReadPost(ITestContext iTestContext) {
        try {
            String testName = "Test Failing - Read Posts";
            Setup.extentTest.set(extentReports.createTest(testName));
            logger.info("Starting test: {}", testName);
            Response response = given()
                    .basePath(BASE_PATH_POST)
                    .contentType(ContentType.JSON)
                    .accept(ContentType.JSON)
                    .pathParam("pathId", 9999) // Non-existent post ID
                    .filter(new Log4jFilter())
                    .when()
                    .get("/{pathId}")
                    .then()
                    .assertThat()
                    .statusCode(404) // Expecting failure
                    .extract().response();

            ExtentReportManager.logJson(response.asPrettyString());
            logger.info("Response: {}", response.asPrettyString());
            Setup.extentTest.get().log(Status.PASS, "Failing Read Post test passed");
        } catch (Exception e) {
            logger.error("Error during Failing Read Post test: ", e);
            Setup.extentTest.get().log(Status.FAIL, "Error during Failing Read Post test: " + e.getMessage());
            throw e;
        }
    }

    @AfterClass
    public void tearDown() {
        extentReports.flush();
    }
}
