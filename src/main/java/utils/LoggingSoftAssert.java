package utils;

import Reporting.ExtentReportManager;
import org.testng.asserts.IAssert;
import org.testng.asserts.SoftAssert;

public class LoggingSoftAssert extends SoftAssert {

    @Override
    public void onAssertSuccess(IAssert<?> assertCommand) {
        ExtentReportManager.logSuccessDetails(assertCommand.getMessage());
        super.onAssertSuccess(assertCommand);
    }

    @Override
    public void onAssertFailure(IAssert<?> assertCommand, AssertionError ex) {
        ExtentReportManager.logFailureDetails(ex.getMessage());
        super.onAssertFailure(assertCommand, ex);
    }

    public void assertEqualsWithLogging(Object actual, Object expected, String message) {
        try {
            assertEquals(actual, expected, message);
            onAssertSuccess(new IAssert<Object>() {
                @Override
                public Object getActual() {
                    return actual;
                }

                @Override
                public Object getExpected() {
                    return expected;
                }

                @Override
                public String getMessage() {
                    return message;
                }

                @Override
                public void doAssert() {
                    // No-op for logging
                }
            });
        } catch (AssertionError ex) {
            onAssertFailure(new IAssert<Object>() {
                @Override
                public Object getActual() {
                    return actual;
                }

                @Override
                public Object getExpected() {
                    return expected;
                }

                @Override
                public String getMessage() {
                    return message;
                }

                @Override
                public void doAssert() {
                    // No-op for logging
                }
            }, ex);
            throw ex;
        }
    }

    // Add more custom assertion methods as needed

    public void assertTrueWithLogging(boolean condition, String message) {
        try {
            assertTrue(condition, message);
            onAssertSuccess(new IAssert<Boolean>() {
                @Override
                public Boolean getActual() {
                    return condition;
                }

                @Override
                public Boolean getExpected() {
                    return true;
                }

                @Override
                public String getMessage() {
                    return message;
                }

                @Override
                public void doAssert() {
                    // No-op for logging
                }
            });
        } catch (AssertionError ex) {
            onAssertFailure(new IAssert<Boolean>() {
                @Override
                public Boolean getActual() {
                    return condition;
                }

                @Override
                public Boolean getExpected() {
                    return true;
                }

                @Override
                public String getMessage() {
                    return message;
                }

                @Override
                public void doAssert() {
                    // No-op for logging
                }
            }, ex);
            throw ex;
        }
    }

    public void assertNotNullWithLogging(Object object, String message) {
        try {
            assertNotNull(object, message);
            onAssertSuccess(new IAssert<Object>() {
                @Override
                public Object getActual() {
                    return object;
                }

                @Override
                public Object getExpected() {
                    return null;
                }

                @Override
                public String getMessage() {
                    return message;
                }

                @Override
                public void doAssert() {
                    // No-op for logging
                }
            });
        } catch (AssertionError ex) {
            onAssertFailure(new IAssert<Object>() {
                @Override
                public Object getActual() {
                    return object;
                }

                @Override
                public Object getExpected() {
                    return null;
                }

                @Override
                public String getMessage() {
                    return message;
                }

                @Override
                public void doAssert() {
                    // No-op for logging
                }
            }, ex);
            throw ex;
        }
    }
}
