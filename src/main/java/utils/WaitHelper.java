package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * WaitHelper - Provides explicit wait methods (REQUIREMENT: Use wait methods, avoid Thread.sleep)
 *
 * Purpose: Smart waiting for elements and conditions
 * - Explicit waits are more reliable than Thread.sleep
 * - Waits only as long as needed (up to timeout)
 * - Provides clear methods for common wait scenarios
 *
 * Why avoid Thread.sleep?
 * - Thread.sleep(5000) always waits 5 seconds even if element appears in 1 second
 * - Explicit wait stops as soon as condition is met, making tests faster
 */
public class WaitHelper {
    private WebDriver driver;
    private WebDriverWait wait;

    public WaitHelper(WebDriver driver) {
        this.driver = driver;
        // Create WebDriverWait with timeout from config.properties
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(
                Long.parseLong(ConfigReader.getProperty("explicit.wait"))
        ));
    }

    /**
     * Wait until element is visible on the page
     * Use before interacting with elements
     */
    public void waitForElementVisible(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * Wait until element is clickable (visible + enabled)
     * Use before clicking buttons/links
     */
    public void waitForElementClickable(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * Wait until URL contains expected text
     * Useful for verifying navigation (e.g., after login, URL should contain "/campgrounds")
     */
    public boolean waitForUrlContains(String urlPart) {
        try {
            wait.until(ExpectedConditions.urlContains(urlPart));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Wait until URL exactly matches expected URL
     */
    public boolean waitForUrlToBe(String url) {
        try {
            wait.until(ExpectedConditions.urlToBe(url));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Wait until element becomes invisible
     * Useful for waiting for loading spinners to disappear
     */
    public void waitForElementInvisible(WebElement element) {
        wait.until(ExpectedConditions.invisibilityOf(element));
    }

    /**
     * Check if element is displayed without throwing exception
     * Returns true/false instead of throwing error
     */
    public boolean isElementDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
