package tests;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import listeners.ExtentManager;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;
import utils.ConfigReader;
import utils.DriverManager;

// Klasa bazë për të gjitha testet
@Listeners(listeners.TestListener.class)
public class BaseTest {
    protected WebDriver driver;
    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    @BeforeSuite
    public void setupSuite() {
        extent = ExtentManager.getInstance();
    }

    @BeforeMethod
    public void setup(java.lang.reflect.Method method) {
        String testName = method.getName();
        System.out.println("\n[SETUP] Starting setup for: " + testName);
        long setupStartTime = System.currentTimeMillis();

        // For Test 7 and Test 8, don't create a new driver - reuse existing session
        if (testName.equals("testShoppingCart") || testName.equals("testEmptyShoppingCart")) {
            System.out.println("Reusing existing driver session for " + testName);
            // Driver already exists from previous test, just get reference
            driver = DriverManager.getDriver();
        } else {
            // Create new driver for all other tests
            driver = DriverManager.getDriver();

            // Use JavaScript navigation as workaround for Chrome 143 renderer timeout
            String baseUrl = ConfigReader.getBaseUrl();
            org.openqa.selenium.JavascriptExecutor js = (org.openqa.selenium.JavascriptExecutor) driver;

            // Set page load timeout
            driver.manage().timeouts().pageLoadTimeout(java.time.Duration.ofSeconds(20));

            // Use JavaScript to navigate - this bypasses some renderer issues
            js.executeScript("window.location.href = arguments[0];", baseUrl);

            // Wait for page to be ready using JavaScript
            try {
                new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(10))
                    .until(d -> js.executeScript("return document.readyState").equals("complete"));
            } catch (Exception e) {
                // Page load wait timed out, but continue anyway
            }
        }

        ExtentTest test = extent.createTest(this.getClass().getSimpleName());
        extentTest.set(test);

        long setupEndTime = System.currentTimeMillis();
        long setupDuration = setupEndTime - setupStartTime;
        System.out.println("[SETUP] Completed setup for: " + testName + " (took " + setupDuration + "ms)\n");
    }

    @AfterMethod
    public void tearDown(java.lang.reflect.Method method) {
        String testName = method.getName();

        // Don't quit driver after Test 6 or Test 7 to preserve session
        if (testName.equals("testCheckSorting") || testName.equals("testShoppingCart")) {
            System.out.println("Preserving driver session after " + testName);
            return;
        }

        // Quit driver for all other tests
        DriverManager.quitDriver();
    }

    @AfterSuite
    public void tearDownSuite() {
        ExtentManager.flush();
    }

    public static WebDriver getDriver() {
        return DriverManager.getDriver();
    }

    public static ExtentTest getExtentTest() {
        return extentTest.get();
    }
}
