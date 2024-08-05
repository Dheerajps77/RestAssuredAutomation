package utils;

import Reporting.ExtentReportManager;
import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class Log4jFilter implements Filter {

    //private static final Logger logger = LogManager.getLogger(Log4jFilter.class);
    private static final Logger requestResponseLogger = LogManager.getLogger("requestResponseLogger");

    @Override
    public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec, FilterContext ctx) {
        // Log request details
        String method = requestSpec.getMethod();
        String uri = requestSpec.getURI();
        requestResponseLogger.info("========== REQUEST ==============");
        requestResponseLogger.info("Method: " + requestSpec.getMethod());
        requestResponseLogger.info("URI: " + requestSpec.getURI());
        requestResponseLogger.info("Headers: " + requestSpec.getHeaders());
        requestResponseLogger.info("Content-Type: " + requestSpec.getContentType());
        requestResponseLogger.info("Cookies: " + requestSpec.getCookies());
        if (!requestSpec.getQueryParams().isEmpty()) {
            requestResponseLogger.info("Query Params: " + requestSpec.getQueryParams());
        }
        if (!requestSpec.getPathParams().isEmpty()) {
            requestResponseLogger.info("Path Params: " + requestSpec.getPathParams());
        }
        if (requestSpec.getBody() != null) {
            requestResponseLogger.info("Body: " + requestSpec.getBody());
        }

        // Proceed with the request
        Response response = ctx.next(requestSpec, responseSpec);

        // Log response details
        requestResponseLogger.info("========== RESPONSE ==============");
        requestResponseLogger.info("Status Code: " + response.getStatusCode());
        requestResponseLogger.info("Status Line: " + response.getStatusLine());
        requestResponseLogger.info("Headers: " + response.getHeaders());
        requestResponseLogger.info("Cookies: " + response.getCookies());
        if (response.getBody() != null) {
            requestResponseLogger.info("Body: " + response.getBody().asPrettyString());
        }

        // Log request and response details in Extent Report
        ExtentReportManager.logRequestResponseDetails(method, uri, response);

        return response;
    }
}

