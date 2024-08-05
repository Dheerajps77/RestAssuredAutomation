package Reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import com.aventstack.extentreports.markuputils.CodeLanguage;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import io.restassured.http.Header;
import io.restassured.response.Response;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class ExtentReportManager {

    public static ExtentReports extentReports;
    //public Setup setup = new Setup();

    public static ExtentReports createInstance(String fileName, String reportName, String documentTitle) {
        String reportPath= System.getProperty("user.dir")+"/target/ExtentReports/" + fileName;
        ExtentSparkReporter extentSparkReporter = new ExtentSparkReporter(reportPath);
        extentSparkReporter.config().setReportName(reportName);
        extentSparkReporter.config().setDocumentTitle(documentTitle);
        extentSparkReporter.config().setTheme(Theme.DARK);
        extentSparkReporter.config().setEncoding("utf-8");
        extentReports = new ExtentReports();
        extentReports.attachReporter(extentSparkReporter);

        // Additional configuration (optional)
        extentReports.setSystemInfo("OS", System.getProperty("os.name"));
        extentReports.setSystemInfo("Java Version", System.getProperty("java.version"));
        return extentReports;
    }

    public static String getReportNameWithTimeStamp() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");
        LocalDateTime localDateTime = LocalDateTime.now();
        String formattedTime = dateTimeFormatter.format(localDateTime);
        String reportName = "TestReport_" + formattedTime + ".html";
        return reportName;
    }

    public static void logRequestResponseDetails(String method, String uri, Response response) {
        Setup.extentTest.get().log(Status.INFO, "Method: " + method);
        Setup.extentTest.get().log(Status.INFO, "URI: " + uri);
        Setup.extentTest.get().log(Status.INFO, "Headers: " + response.getHeaders().toString());
        Setup.extentTest.get().log(Status.INFO, "Content-Type: " + response.getContentType());
        Setup.extentTest.get().log(Status.INFO, "Cookies: " + response.getCookies().toString());
        if (response.getBody() != null) {
            Setup.extentTest.get().log(Status.INFO, "Body: " + response.getBody().asPrettyString());
        }
    }



    public static void logPassDetails(String log) {
        Setup.extentTest.get().pass(MarkupHelper.createLabel(log, ExtentColor.GREEN));
    }

    public static void logFailureDetails(String log) {
        Setup.extentTest.get().fail(MarkupHelper.createLabel(log, ExtentColor.RED));
    }

    public static void logSuccessDetails(String message) {
        Setup.extentTest.get().log(Status.PASS, message);
    }

    public static void logExceptionDetails(String log) {
        Setup.extentTest.get().fail(log);
    }

    public static void logInfoDetails(String log) {
        Setup.extentTest.get().info(MarkupHelper.createLabel(log, ExtentColor.GREY));
    }

    public static void logWarningDetails(String log) {
        Setup.extentTest.get().warning(MarkupHelper.createLabel(log, ExtentColor.YELLOW));
    }

    public static void logJson(String json) {
        Setup.extentTest.get().info(MarkupHelper.createCodeBlock(json, CodeLanguage.JSON));
    }

    private static String formatHeaders(Map<String, ?> headersMap) {
        StringBuilder headersFormatted = new StringBuilder();
        for (Map.Entry<String, ?> entry : headersMap.entrySet()) {
            headersFormatted.append(entry.getKey())
                    .append(": ")
                    .append(entry.getValue())
                    .append("\n");
        }
        return headersFormatted.toString();
    }

    public static void logHeaders(List<Header> headersList) {

        String[][] arrayHeaders = headersList.stream().map(header -> new String[]{header.getName(), header.getValue()})
                .toArray(String[][]::new);
        Setup.extentTest.get().info(MarkupHelper.createTable(arrayHeaders));
    }
}
