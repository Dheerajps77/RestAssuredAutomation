package Listeners;

import Reporting.ExtentReportsThreadLocal;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {
    private boolean isParallel;

    @Override
    public void onStart(ITestContext context) {
        // Determine if the tests are running in parallel
        isParallel = context.getSuite().getXmlSuite().getParallel().isParallel();
    }

    @Override
    public void onFinish(ITestContext context) {
        ExtentReportsThreadLocal.getExtentReports().flush();
    }

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest test = ExtentReportsThreadLocal.getExtentReports().createTest(result.getMethod().getMethodName());
        ExtentReportsThreadLocal.setTest(test, isParallel);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentReportsThreadLocal.getTest(isParallel).log(Status.PASS, "Test passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentReportsThreadLocal.getTest(isParallel).log(Status.FAIL, "Test failed: " + result.getThrowable());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentReportsThreadLocal.getTest(isParallel).log(Status.SKIP, "Test skipped: " + result.getThrowable());
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        // Do nothing
    }

    @Override
    public void onTestFailedWithTimeout(ITestResult result) {
        onTestFailure(result);
    }
}