package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utils.WaitHelper;

/**
 * BasePage - Parent class for all page objects (REQUIREMENT: Page Object Model)
 *
 * Purpose: Contains common elements and methods shared by all pages
 * - Navigation bar elements (Login, Register, Logout links)
 * - Common utility methods
 * - All page classes will extend this base class
 *
 * Page Object Model Benefits:
 * - Separates test logic from page structure
 * - If UI changes, you only update the page class, not all tests
 * - Reusable methods across tests
 */
public class BasePage {
    protected WebDriver driver;
    protected WaitHelper waitHelper;

    // ===== NAVIGATION BAR ELEMENTS =====
    // These elements appear on every page in YelpCamp

    @FindBy(linkText = "Login")
    protected WebElement loginLink;

    @FindBy(linkText = "Sign Up")  // React uses "Sign Up" not "Register"
    protected WebElement registerLink;

    @FindBy(xpath = "//button[contains(text(),'Logout')]")  // Logout is a BUTTON not a link
    protected WebElement logoutLink;

    @FindBy(linkText = "Home")  // React uses linkText "Home"
    protected WebElement homeLink;

    @FindBy(linkText = "All Camps")  // React uses "All Camps" not "All Campgrounds"
    protected WebElement allCampgroundsLink;

    @FindBy(linkText = "New Camp")  // React uses "New Camp" not "New Campground"
    protected WebElement newCampgroundLink;

    /**
     * Constructor - Initializes WebDriver and PageFactory
     * PageFactory.initElements() automatically finds and initializes @FindBy elements
     */
    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.waitHelper = new WaitHelper(driver);
        PageFactory.initElements(driver, this);  // Initialize all @FindBy elements
    }

    // ===== NAVIGATION METHODS =====
    // These methods are available to all page objects

    /**
     * Click on Login link in navigation bar
     */
    public void clickLoginLink() {
        waitHelper.waitForElementClickable(loginLink);
        loginLink.click();
    }

    /**
     * Click on Register link in navigation bar
     */
    public void clickRegisterLink() {
        waitHelper.waitForElementClickable(registerLink);
        registerLink.click();
    }

    /**
     * Click on Logout link (only visible when logged in)
     * Uses JavaScript click to avoid flash message blocking the button
     */
    public void clickLogoutLink() {
        // Wait for logout button to be present
        waitHelper.waitForElementVisible(logoutLink);

        // Wait for any flash messages to disappear (they might block the button)
        try { Thread.sleep(2000); } catch (InterruptedException e) {}

        // Use JavaScript click to avoid ElementClickInterceptedException from flash messages
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
            "arguments[0].click();", logoutLink
        );

        // Wait for logout to process
        try { Thread.sleep(1000); } catch (InterruptedException e) {}
    }

    /**
     * Click on Home/YelpCamp logo
     */
    public void clickHomeLink() {
        waitHelper.waitForElementClickable(homeLink);
        homeLink.click();
    }

    /**
     * Click on "All Campgrounds" link
     */
    public void clickAllCampgroundsLink() {
        waitHelper.waitForElementClickable(allCampgroundsLink);
        allCampgroundsLink.click();
    }

    /**
     * Click on "New Campground" link (only visible when logged in)
     */
    public void clickNewCampgroundLink() {
        waitHelper.waitForElementClickable(newCampgroundLink);
        newCampgroundLink.click();
    }

    // ===== VERIFICATION METHODS =====

    /**
     * Check if user is logged in
     * User is logged in if Logout link is visible
     */
    public boolean isUserLoggedIn() {
        return waitHelper.isElementDisplayed(logoutLink);
    }

    /**
     * Check if user is logged out
     * User is logged out if Login link is visible
     */
    public boolean isUserLoggedOut() {
        return waitHelper.isElementDisplayed(loginLink);
    }

    /**
     * Get current page URL
     */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    /**
     * Get current page title
     */
    public String getPageTitle() {
        return driver.getTitle();
    }
}
