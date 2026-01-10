package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * WaitHelper - Metodat për pritje eksplicite (explicit waits)
 */
public class WaitHelper {
    private WebDriver driver;
    private WebDriverWait wait;

    public WaitHelper(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(
                Long.parseLong(ConfigReader.getProperty("explicit.wait"))
        ));
    }

    public void waitForElementVisible(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    public void waitForElementClickable(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    public boolean waitForUrlContains(String urlPart) {
        try {
            wait.until(ExpectedConditions.urlContains(urlPart));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean waitForUrlToBe(String url) {
        try {
            wait.until(ExpectedConditions.urlToBe(url));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void waitForElementInvisible(WebElement element) {
        wait.until(ExpectedConditions.invisibilityOf(element));
    }

    public boolean isElementDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isElementDisplayedWithRetry(WebElement element) {
        try {
            return wait.until(driver -> {
                try {
                    return element.isDisplayed();
                } catch (Exception e) {
                    return false;
                }
            });
        } catch (Exception e) {
            return false;
        }
    }
}
