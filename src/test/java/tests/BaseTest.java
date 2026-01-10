package tests;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import listeners.ExtentManager;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;
import utils.ConfigReader;
import utils.DriverManager;

/**
 * BaseTest - Parent class for all test classes (REQUIREMENT: Use JUnit/TestNG)
 *
 * Purpose: Contains setup and teardown methods for all tests
 * - Initializes WebDriver before each test
 * - Quits WebDriver after each test
 * - Sets up ExtentReports for reporting
 * - All test classes will extend this base class
 *
 * TestNG Annotations:
 * @BeforeClass - Runs once before all tests in this class
 * @AfterClass - Runs once after all tests in this class
 * @BeforeMethod - Runs before each @Test method
 * @AfterMethod - Runs after each @Test method
 */
@Listeners(listeners.TestListener.class)  // Attach TestListener for screenshot capture on failure
public class BaseTest {
    protected WebDriver driver;
    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    /**
     * Runs once before all tests in the class
     * Initializes ExtentReports
     */
    @BeforeSuite
    public void setupSuite() {
        extent = ExtentManager.getInstance();
    }

    /**
     * Runs before each test method
     * - Creates WebDriver instance
     * - Opens YelpCamp application
     * - Creates test entry in ExtentReports
     */
    @BeforeMethod
    public void setup() {
        // Get WebDriver from DriverManager
        driver = DriverManager.getDriver();

        // Navigate to YelpCamp application (URL from config.properties)
        driver.get(ConfigReader.getBaseUrl());

        // Create test entry in ExtentReports
        ExtentTest test = extent.createTest(this.getClass().getSimpleName());
        extentTest.set(test);
    }

    /**
     * Runs after each test method
     * Closes browser and cleans up WebDriver
     */
    @AfterMethod
    public void tearDown() {
        // Close browser
        DriverManager.quitDriver();
    }

    /**
     * Runs once after all tests in the class
     * Writes ExtentReports to disk
     */
    @AfterSuite
    public void tearDownSuite() {
        ExtentManager.flush();  // Write report to file
    }

    // ===== HELPER METHODS FOR LISTENERS =====
    // These allow TestListener to access driver and ExtentTest

    /**
     * Returns the WebDriver instance for current thread
     * Used by TestListener to capture screenshots
     */
    public static WebDriver getDriver() {
        return DriverManager.getDriver();
    }

    /**
     * Returns the ExtentTest instance for current thread
     * Used by TestListener to log test results
     */
    public static ExtentTest getExtentTest() {
        return extentTest.get();
    }
}
