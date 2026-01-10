package utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;

/**
 * DriverManager - Manages WebDriver instances using Factory Pattern
 *
 * Purpose: Creates and manages browser driver instances (Chrome, Firefox, Edge)
 * - Uses ThreadLocal to support parallel test execution
 * - Automatically downloads and configures browser drivers using WebDriverManager
 * - Reads browser configuration from config.properties file
 */
public class DriverManager {
    // ThreadLocal ensures each thread gets its own WebDriver instance (important for parallel execution)
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    /**
     * Returns the WebDriver instance for the current thread
     * If driver doesn't exist, creates a new one
     */
    public static WebDriver getDriver() {
        if (driver.get() == null) {
            driver.set(createDriver());
        }
        return driver.get();
    }

    /**
     * Creates a new WebDriver instance based on browser specified in config.properties
     * Supports: Chrome, Firefox, Edge
     */
    private static WebDriver createDriver() {
        // Read browser type from config.properties (e.g., "chrome", "firefox")
        String browser = ConfigReader.getProperty("browser").toLowerCase();
        WebDriver webDriver;

        switch (browser) {
            case "chrome":
                // WebDriverManager automatically downloads the correct ChromeDriver version
                WebDriverManager.chromedriver().setup();

                // Configure Chrome browser options
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--start-maximized");  // Open browser in full screen
                chromeOptions.addArguments("--disable-notifications");  // Block notification popups
                chromeOptions.addArguments("--remote-allow-origins=*");  // Fix CORS issues
                webDriver = new ChromeDriver(chromeOptions);
                break;

            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.addArguments("--start-maximized");
                webDriver = new FirefoxDriver(firefoxOptions);
                break;

            case "edge":
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.addArguments("--start-maximized");
                webDriver = new EdgeDriver(edgeOptions);
                break;

            default:
                throw new IllegalArgumentException("Browser not supported: " + browser);
        }

        // Set implicit wait - how long to wait for elements to appear before throwing error
        webDriver.manage().timeouts().implicitlyWait(
                Duration.ofSeconds(Long.parseLong(ConfigReader.getProperty("implicit.wait")))
        );

        // Set page load timeout - how long to wait for page to load
        webDriver.manage().timeouts().pageLoadTimeout(
                Duration.ofSeconds(Long.parseLong(ConfigReader.getProperty("page.load.timeout")))
        );

        return webDriver;
    }

    /**
     * Quits the browser and removes the driver from ThreadLocal
     * Should be called in @AfterMethod or @AfterClass
     */
    public static void quitDriver() {
        if (driver.get() != null) {
            driver.get().quit();  // Close browser
            driver.remove();  // Remove from ThreadLocal
        }
    }
}
