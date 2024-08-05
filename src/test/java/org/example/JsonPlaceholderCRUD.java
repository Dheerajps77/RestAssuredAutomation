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
import org.testng.asserts.SoftAssert;
import utils.Log4jFilter;
import utils.LoggingSoftAssert;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class JsonPlaceholderCRUD {

    private static final String BASE_URI = "https://jsonplaceholder.typicode.com";
    private static final String BASE_PATH_POST = "/posts";
    private static final Logger logger = LogManager.getLogger(Log4jFilter.class);
    static final int PathParam_POST_ID = 1;
    private ExtentReports extentReports;
    private ExtentTest extentTest;
    private String reportName;
    LoggingSoftAssert softAssert=new LoggingSoftAssert();
    @BeforeClass
    public void setUp() {
        // Setup RestAssured
        baseURI = BASE_URI;
        // Create a unique report name using current timestamp and class name
        String timestamp = ExtentReportManager.getReportNameWithTimeStamp();
        String className = this.getClass().getSimpleName();
        reportName = String.format("ExtentReport_%s_%s", className, timestamp);

        extentReports = ExtentReportManager.createInstance(reportName,
                "JSON Placeholder API Test Report - " + className,
                "JSON Placeholder API Test Report");
    }

    @BeforeMethod
    public void setUpTest(ITestResult result) {
        // Get the test method name from ITestResult
        String methodName = "HTTP Method : " + result.getMethod().getMethodName();
        // Set up ExtentTest instance before each test method with method name
        Setup.extentTest.set(extentReports.createTest(methodName));
        ExtentReportManager.logJson("Before HTTP Method" + methodName);

    }

    @Test(enabled = true, priority = 0)
    public void testCreatePost(ITestContext iTestContext) {
        String requestBody = "{ \"title\": \"foo\", \"body\": \"bar\", \"userId\": 1 }";
        try {
            Setup.extentTest.set(extentReports.createTest("Create Posts"));
            logger.info("Starting test: Create Post " + iTestContext.getCurrentXmlTest().getName());
            logger.info("Start date of test: Create Post " + iTestContext.getStartDate());
            Response response = given()
                    //.baseUri(BASE_URI)
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
            // System.out.println(response.asPrettyString());
            logger.info("Post created with ID: " + response.jsonPath().get("id"));
            logger.info("Response : " + response.asPrettyString());

            softAssert.assertEqualsWithLogging(response.getStatusCode(), 201, "Status code should be 201");
            softAssert.assertNotNullWithLogging(response.jsonPath().get("id"), "ID should not be null");

            softAssert.assertAll();

            Setup.extentTest.get().log(Status.INFO, "Post created with ID: " + response.jsonPath().get("id"));
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
            Setup.extentTest.set(extentReports.createTest("Read Posts"));
            logger.info("Starting test: Read Post with ID " + PathParam_POST_ID);
            String fullUrl = baseURI + BASE_PATH_POST + "/" + PathParam_POST_ID;
            logger.info("Full URL: " + fullUrl);
            Response response=given()
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
            logger.error("Error during get test: ", e);
            Setup.extentTest.get().log(Status.FAIL, "Error during Read Post test: " + e.getMessage());
            throw e;
        }
    }

    @Test(enabled = true, priority = 2, dependsOnMethods = "testReadPost")
    public void testUpdatePost() {
        int postId = 1;
        Setup.extentTest.set(extentReports.createTest("Update Posts"));
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
            Setup.extentTest.get().log(Status.PASS, "Update Post test passed");
        } catch (Exception e) {
            Setup.extentTest.get().log(Status.FAIL, "Error during Update Post test: " + e.getMessage());
            logger.error("Error during update test: ", e);
            throw e;
        }
    }

    @Test(enabled = true, priority = 3, dependsOnMethods = "testUpdatePost")
    public void testDeletePost() {
        int postId = 1;
        try {
            Setup.extentTest.set(extentReports.createTest("Delete Posts"));
            Response response=given()
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
            Setup.extentTest.get().log(Status.FAIL, "Error during Delete Post test: " + e.getMessage());
            logger.error("Error during delete test: ", e);
            throw e;
        }
    }

    @Test(enabled = true, priority = 4)
    public void testFailingCreatePost(ITestContext iTestContext) {
        String requestBody = "{ \"title\": \"foo\", \"body\": \"bar\" }"; // Missing userId
        try {
            Setup.extentTest.set(extentReports.createTest("Test Failing - Create Posts"));
            logger.info("Starting test: Failing Create Post");
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
            logger.info("Response: " + response.asPrettyString());
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
            Setup.extentTest.set(extentReports.createTest("Test Failing - Read Posts"));
            logger.info("Starting test: Failing Read Post with non-existing ID");
            int nonExistingId = 99999;
            Response response = given()
                    .basePath(BASE_PATH_POST)
                    .contentType(ContentType.JSON)
                    .accept(ContentType.JSON)
                    .pathParam("pathId", nonExistingId)
                    .filter(new Log4jFilter())
                    .when()
                    .get("/{pathId}")
                    .then()
                    .assertThat()
                    .statusCode(404) // Expecting failure
                    .extract().response();
            ExtentReportManager.logJson(response.asPrettyString());
            logger.info("Response: " + response.asPrettyString());
            Setup.extentTest.get().log(Status.PASS, "Failing Read Post test passed");
        } catch (Exception e) {
            logger.error("Error during Failing Read Post test: ", e);
            Setup.extentTest.get().log(Status.FAIL, "Error during Failing Read Post test: " + e.getMessage());
            throw e;
        }
    }

    @Test(enabled = true, priority = 6)
    public void testFailingUpdatePost(ITestContext iTestContext) {
        int nonExistingId = 99999;
        Setup.extentTest.set(extentReports.createTest("Test Failing - Update Posts"));
        String requestBody = "{ \"id\": " + nonExistingId + ", \"title\": \"foo_updated\", \"body\": \"bar_updated\", \"userId\": 1 }";
        logger.info("Starting test: Failing Update Post with non-existing ID");
        try {
            Response response = given()
                    .basePath(BASE_PATH_POST)
                    .contentType(ContentType.JSON)
                    .accept(ContentType.JSON)
                    .pathParam("postId", nonExistingId)
                    .body(requestBody)
                    .filter(new Log4jFilter())
                    .when()
                    .put("/{postId}")
                    .then()
                    .assertThat()
                    .statusCode(404) // Expecting failure
                    .extract().response();
            ExtentReportManager.logJson(response.asPrettyString());
            logger.info("Response: " + response.asPrettyString());
            Setup.extentTest.get().log(Status.PASS, "Failing Update Post test passed");
        } catch (Exception e) {
            logger.error("Error during Failing Update Post test: ", e);
            Setup.extentTest.get().log(Status.FAIL, "Error during Failing Update Post test: " + e.getMessage());
            throw e;
        }
    }


    @Test(enabled = true, priority = 7)
    public void softAssertionTests()
    {
        try {
            Setup.extentTest.set(extentReports.createTest("Soft Assertion tests"));
            String fullUrl = baseURI + BASE_PATH_POST + "/" + PathParam_POST_ID;
            logger.info("Full URL: " + fullUrl);
            Response response = given()
                    .basePath(BASE_PATH_POST)
                    .contentType(ContentType.JSON)
                    .accept(ContentType.JSON)
                    .pathParam("pathId", PathParam_POST_ID)
                    .filter(new Log4jFilter())
                    .when()
                    .get("/{pathId}")
                    .then()
                    .assertThat()
                    .statusCode(200)
                    .extract().response();
            softAssert.assertEquals(response.getStatusCode(), 200, "Status code should be 200");
            softAssert.assertEquals(response.jsonPath().getString("title"), "foo", "Title should be 'foo'");
            softAssert.assertAll();
            Setup.extentTest.get().log(Status.PASS, "Read Post test passed");
        } catch (Exception e) {
            logger.error("Error during Read Post test: ", e);
            Setup.extentTest.get().log(Status.FAIL, "Error during Read Post test: " + e.getMessage());
            throw e;
        }
    }
    // Adding 2 skipped tests

    @Test(priority = 4, dependsOnMethods = "testDeletePost")
    public void testSkippedPost() {
        Setup.extentTest.set(extentReports.createTest("Skipped Post Test"));
        logger.info("This test is intentionally skipped.");
        Setup.extentTest.get().log(Status.SKIP, "This test is intentionally skipped.");
        throw new SkipException("Skipping the test intentionally");
    }

    @Test(priority = 5)
    public void testSkippedPost2() {
        Setup.extentTest.set(extentReports.createTest("Skipped Post Test 2"));
        logger.info("This test is intentionally skipped.");
        Setup.extentTest.get().log(Status.SKIP, "This test is intentionally skipped.");
        throw new SkipException("Skipping the test intentionally");
    }

    @Test(priority = 6)
    public void testSkippedPost3() {
        Setup.extentTest.set(extentReports.createTest("Skipped Post Test 3"));
        logger.info("This test is intentionally skipped.");
        Setup.extentTest.get().log(Status.SKIP, "This test is intentionally skipped.");
        throw new SkipException("Skipping the test intentionally");
    }

    @Test(enabled = true, priority = 7)
    public void softAssertionTestsUsingUtil() {
       // softAssert = new LoggingSoftAssert();
        try {
            Setup.extentTest.set(extentReports.createTest("Soft Assertion using utils"));
            String fullUrl = baseURI + BASE_PATH_POST + "/" + PathParam_POST_ID;
            logger.info("Full URL: " + fullUrl);
            Response response = given()
                    .basePath(BASE_PATH_POST)
                    .contentType(ContentType.JSON)
                    .accept(ContentType.JSON)
                    .pathParam("pathId", PathParam_POST_ID)
                    .filter(new Log4jFilter())
                    .when()
                    .get("/{pathId}")
                    .then()
                    .assertThat()
                    .statusCode(200)
                    .extract().response();
            softAssert.assertEquals(response.getStatusCode(), 201, "Status code should be 200");
            softAssert.assertEquals(response.jsonPath().getString("title"), "foo", "Title should be 'foo'");
            softAssert.assertAll();
            Setup.extentTest.get().log(Status.PASS, "Read Post test passed");
        } catch (Exception e) {
            logger.error("Error during Read Post test: ", e);
            Setup.extentTest.get().log(Status.FAIL, "Error during Read Post test: " + e.getMessage());
            throw e;
        }
    }

    @AfterClass
    public void tearDown() {
        extentReports.flush();
    }

}
