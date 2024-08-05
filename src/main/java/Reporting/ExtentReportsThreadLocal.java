package Reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Paths;

public class ExtentReportsThreadLocal {

    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    private static ExtentTest nonParallelTest;
    private static final Logger logger = LogManager.getLogger(ExtentReportsThreadLocal.class);

    public synchronized static ExtentReports getExtentReports() {
        String reportPath = Paths.get("reports", "extent.html").toString();
        if (extent == null) {
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
            sparkReporter.config().setTheme(Theme.STANDARD);
            sparkReporter.config().setDocumentTitle("Automation Report");
            sparkReporter.config().setReportName("Test Report");

            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);
            logger.info("ExtentReports instance created.");
        }
        return extent;
    }

    public static synchronized void setTest(ExtentTest extentTest, boolean isParallel) {
        if (isParallel) {
            test.set(extentTest);
        } else {
            nonParallelTest = extentTest;
        }
        logger.info("Test instance set for parallel execution: {}", isParallel);
    }

    public static synchronized ExtentTest getTest(boolean isParallel) {
        if (isParallel) {
            return test.get();
        } else {
            return nonParallelTest;
        }
    }

    public static synchronized void removeTest(boolean isParallel) {
        if (isParallel) {
            test.remove();
        } else {
            nonParallelTest = null;
        }
        logger.info("Test instance removed for parallel execution: {}", isParallel);
    }
}
