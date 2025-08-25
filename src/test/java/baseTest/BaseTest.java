package baseTest;

import api.endpoints.PetEndpoints;
import api.payload.Pet;
import api.utilities.ReportManager;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.lang.reflect.Method;

public class BaseTest {

    protected static ExtentReports extent;
    protected static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    // ✅ Log4j2 logger
    protected static final Logger logger = LogManager.getLogger(BaseTest.class);

    protected static final int DEFAULT_MAX_RETRIES = 3;
    protected static final long DEFAULT_WAIT_MS = 1000;

    @BeforeSuite(alwaysRun = true)
    public void setupSuite() {
        extent = ReportManager.getInstance();
    }

    @BeforeMethod(alwaysRun = true)
    public void setupTest(Method method) {
        ExtentTest extentTest = extent.createTest(method.getName());
        test.set(extentTest);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDownTest(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            logError("Test Failed: " + result.getName(), result.getThrowable());
        } else if (result.getStatus() == ITestResult.SKIP) {
            test.get().skip("Test Skipped: " + result.getThrowable());
        } else {
            test.get().pass("Test Passed");
        }
    }

    @AfterSuite(alwaysRun = true)
    public void tearDownSuite() {
        if (extent != null) {
            extent.flush();
        }
    }

    /**
     * Log helper
     */
    protected void logInfo(String message) {
        logger.info(message);                    // Log to file via Log4j2
        System.out.println("[INFO] " + message); // Optional console output
        if (test.get() != null) {
            test.get().info(message);            // Also send to ExtentReports
        }
    }

    protected void logError(String message, Throwable t) {
        logger.error(message, t);                // Log to file via Log4j2
        if (test.get() != null) {
            test.get().fail(message);            // Log message
            test.get().fail(t);                  // Log throwable separately
        }
    }

    /**
     * Retry helper for GET or DELETE operations
     */
    protected Response retryPetOperation(int petId, String operation, Pet petIfNeeded) throws InterruptedException {
        Response response = null;
        for (int i = 0; i < DEFAULT_MAX_RETRIES; i++) {
            switch (operation) {
                case "GET" -> response = PetEndpoints.getPet(petId);
                case "DELETE" -> response = PetEndpoints.deletePet(petId);
            }

            if (response.getStatusCode() == 200) break;

            if (response.getStatusCode() == 404 && petIfNeeded != null) {
                logInfo("Pet not found. Creating pet for retry: ID " + petId);
                PetEndpoints.createPet(petIfNeeded);
            }

            Thread.sleep(DEFAULT_WAIT_MS);
        }
        return response;
    }
}
