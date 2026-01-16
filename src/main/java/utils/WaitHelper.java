package utils;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * WaitHelper - Methods for explicit waits to replace Thread.sleep()
 */
public class WaitHelper {
    private WebDriver driver;
    private WebDriverWait wait;
    private WebDriverWait shortWait;

    public WaitHelper(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(
                Long.parseLong(ConfigReader.getProperty("explicit.wait"))
        ));
        this.shortWait = new WebDriverWait(driver, Duration.ofSeconds(3));
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

    public boolean waitForUrlToChange(String currentUrl) {
        try {
            wait.until(driver -> !driver.getCurrentUrl().equals(currentUrl));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void waitForElementInvisible(WebElement element) {
        wait.until(ExpectedConditions.invisibilityOf(element));
    }

    public void waitForPageLoad() {
        wait.until(driver -> ((JavascriptExecutor) driver)
                .executeScript("return document.readyState").equals("complete"));
    }

    public void waitForAjaxComplete() {
        wait.until(driver -> ((JavascriptExecutor) driver)
                .executeScript("return jQuery.active == 0"));
    }

    public boolean waitForElementPresent(By locator) {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void waitForElementAttributeContains(WebElement element, String attribute, String value) {
        wait.until(ExpectedConditions.attributeContains(element, attribute, value));
    }

    public void waitForTextToBePresentInElement(WebElement element, String text) {
        wait.until(ExpectedConditions.textToBePresentInElement(element, text));
    }

    public void waitShort(int milliseconds) {
        try {
            wait.until(driver -> {
                try {
                    Thread.sleep(milliseconds);
                    return true;
                } catch (InterruptedException e) {
                    return false;
                }
            });
        } catch (Exception e) {
            // Ignore
        }
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

    public boolean waitForElementCount(By locator, int expectedCount) {
        try {
            return wait.until(driver -> driver.findElements(locator).size() == expectedCount);
        } catch (Exception e) {
            return false;
        }
    }

    public void waitForStaleElement(WebElement element) {
        try {
            wait.until(ExpectedConditions.stalenessOf(element));
        } catch (Exception e) {
            // Element is already stale or not found
        }
    }
}
