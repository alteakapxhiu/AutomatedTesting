package listeners;

import com.aventstack.extentreports.Status;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import tests.BaseTest;
import utils.ScreenshotUtil;

/**
 * TestListener - Dëgjon ngjarjet e testeve dhe kryen veprime (screenshot në dështim)
 */
public class TestListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        if (BaseTest.getExtentTest() != null) {
            BaseTest.getExtentTest().log(Status.INFO, "Test Started: " + result.getName());
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        if (BaseTest.getExtentTest() != null) {
            BaseTest.getExtentTest().log(Status.PASS, "Test Passed: " + result.getName());
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        if (BaseTest.getDriver() != null) {
            String screenshotPath = ScreenshotUtil.captureScreenshot(
                BaseTest.getDriver(),
                result.getName()
            );

            if (BaseTest.getExtentTest() != null) {
                BaseTest.getExtentTest().log(Status.FAIL, "Test Failed: " + result.getName());
                BaseTest.getExtentTest().log(Status.FAIL, result.getThrowable());

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

    @Override
    public void onTestSkipped(ITestResult result) {
        if (BaseTest.getExtentTest() != null) {
            BaseTest.getExtentTest().log(Status.SKIP, "Test Skipped: " + result.getName());
        }
    }

    @Override
    public void onStart(ITestContext context) {
    }

    @Override
    public void onFinish(ITestContext context) {
    }
}
