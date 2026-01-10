package listeners;

import com.aventstack.extentreports.Status;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import tests.BaseTest;
import utils.ScreenshotUtil;

/**
 * TestListener - Listens to test events and performs actions (REQUIREMENT: Screenshot on failure)
 *
 * Purpose: Hooks into TestNG lifecycle events
 * - Automatically captures screenshot when test fails
 * - Logs test results to ExtentReports
 * - Prints test status to console
 *
 * How it works: TestNG calls these methods at different points:
 * - onTestStart: Before each @Test method
 * - onTestSuccess: After test passes
 * - onTestFailure: After test fails (THIS IS WHERE WE CAPTURE SCREENSHOT)
 * - onTestSkipped: When test is skipped
 */
public class TestListener implements ITestListener {

    /**
     * Called before each test starts
     */
    @Override
    public void onTestStart(ITestResult result) {
        System.out.println("Test Started: " + result.getName());
        if (BaseTest.getExtentTest() != null) {
            BaseTest.getExtentTest().log(Status.INFO, "Test Started: " + result.getName());
        }
    }

    /**
     * Called when test passes
     */
    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("Test Passed: " + result.getName());
        if (BaseTest.getExtentTest() != null) {
            BaseTest.getExtentTest().log(Status.PASS, "Test Passed: " + result.getName());
        }
    }

    /**
     * Called when test fails - CAPTURES SCREENSHOT HERE
     * This fulfills the requirement: "Configure Screenshot Capture on failure"
     */
    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println("Test Failed: " + result.getName());

        // Take screenshot of the failure
        if (BaseTest.getDriver() != null) {
            String screenshotPath = ScreenshotUtil.captureScreenshot(
                BaseTest.getDriver(),
                result.getName()  // Use test name as screenshot name
            );

            // Add failure info and screenshot to ExtentReport
            if (BaseTest.getExtentTest() != null) {
                BaseTest.getExtentTest().log(Status.FAIL, "Test Failed: " + result.getName());
                BaseTest.getExtentTest().log(Status.FAIL, result.getThrowable());  // Log error message

                // Attach screenshot to report
                if (screenshotPath != null) {
                    try {
                        BaseTest.getExtentTest().addScreenCaptureFromPath(
                            System.getProperty("user.dir") + "/" + screenshotPath
                        );
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * Called when test is skipped
     */
    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println("Test Skipped: " + result.getName());
        if (BaseTest.getExtentTest() != null) {
            BaseTest.getExtentTest().log(Status.SKIP, "Test Skipped: " + result.getName());
        }
    }

    /**
     * Called before entire test suite starts
     */
    @Override
    public void onStart(ITestContext context) {
        System.out.println("Test Suite Started: " + context.getName());
    }

    /**
     * Called after entire test suite finishes
     */
    @Override
    public void onFinish(ITestContext context) {
        System.out.println("Test Suite Finished: " + context.getName());
    }
}
