package utils;

import Reporting.ExtentReportManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ErrorHandling {
        private static final Logger logger = LogManager.getLogger(ErrorHandling.class);

        public void handleError(Exception e) {
            logger.error("An error occurred: ", e);
            ExtentReportManager.logFailureDetails(e.getMessage());
        }
    }