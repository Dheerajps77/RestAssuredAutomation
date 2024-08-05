package utils;
import Reporting.ExtentReportManager;
import Reporting.Setup;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import io.restassured.RestAssured;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import utils.LoggingSoftAssert;

public class TestBaseForJSONPlaceholderCRUD {
    protected static final Logger logger = LogManager.getLogger(TestBaseForJSONPlaceholderCRUD.class);
    protected ExtentReports extentReports;
    protected ExtentTest extentTest;
    protected LoggingSoftAssert softAssert;

    @BeforeClass
    public void setUpClass() {
        // Setup RestAssured base URI
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";

        // Create a unique report name using current timestamp and class name
        String reportName = ExtentReportManager.getReportNameWithTimeStamp();
        String className = this.getClass().getSimpleName();

        extentReports = ExtentReportManager.createInstance(reportName,
                "JSON Placeholder API Test Report - " + className,
                "JSON Placeholder API Test Report");

        softAssert = new LoggingSoftAssert();
    }

    @BeforeMethod
    public void setUpMethod(ITestResult result) {
        // Get the test method name from ITestResult
        String methodName = result.getMethod().getMethodName();
        // Set up ExtentTest instance before each test method with method name
        extentTest = extentReports.createTest(methodName);
        Setup.extentTest.set(extentTest);
    }

    @AfterMethod
    public void tearDownMethod(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            extentTest.log(Status.FAIL, "Test Failed: " + result.getThrowable());
        } else if (result.getStatus() == ITestResult.SKIP) {
            extentTest.log(Status.SKIP, "Test Skipped: " + result.getThrowable());
        } else {
            extentTest.log(Status.PASS, "Test Passed");
        }
        extentReports.flush();
    }

    @AfterClass
    public void tearDownClass() {
        extentReports.flush();
    }

    protected void logAndFailTest(String message, Exception e) {
        logger.error(message, e);
        Setup.extentTest.get().log(Status.FAIL, message + ": " + e.getMessage());
        throw new SkipException(message, e);
    }
}