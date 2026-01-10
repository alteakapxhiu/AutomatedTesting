package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * LoginPage - Page Object for Login Page
 *
 * URL: http://localhost:5173/login
 *
 * This page has:
 * - Username/Email input field
 * - Password input field
 * - Login button
 * - Link to Register page
 * - Error message (if login fails)
 */
public class LoginPage extends BasePage {

    // ===== PAGE ELEMENTS =====
    // @FindBy - Selenium annotation to find elements
    // Selenium will automatically find these elements when page is loaded

    @FindBy(id = "username")  // Adjust selector based on actual YelpCamp HTML
    private WebElement usernameField;

    @FindBy(id = "password")
    private WebElement passwordField;

    @FindBy(xpath = "//button[@type='submit' or contains(text(),'Login')]")
    private WebElement loginButton;

    @FindBy(xpath = "//div[contains(@class,'alert') or contains(@class,'error')]")
    private WebElement errorMessage;

    @FindBy(linkText = "Create an account")
    private WebElement createAccountLink;

    @FindBy(xpath = "//h5[contains(text(),'Login')]")  // React uses h5, not h1
    private WebElement loginHeading;

    /**
     * Constructor - Calls parent BasePage constructor
     */
    public LoginPage(WebDriver driver) {
        super(driver);
    }

    // ===== PAGE ACTIONS =====
    // These methods represent actions a user can take on this page

    /**
     * Navigate to login page
     */
    public void navigateToLogin() {
        driver.get(utils.ConfigReader.getBaseUrl() + "/login");
    }

    /**
     * Enter username in username field
     */
    public void enterUsername(String username) {
        waitHelper.waitForElementVisible(usernameField);
        usernameField.clear();  // Clear any existing text
        usernameField.sendKeys(username);
    }

    /**
     * Enter password in password field
     */
    public void enterPassword(String password) {
        waitHelper.waitForElementVisible(passwordField);
        passwordField.clear();
        passwordField.sendKeys(password);
    }

    /**
     * Click login button
     */
    public void clickLoginButton() {
        waitHelper.waitForElementClickable(loginButton);
        loginButton.click();

        // Wait a moment for form submission to process
        try { Thread.sleep(1000); } catch (InterruptedException e) {}
    }

    /**
     * Complete login action - enters credentials and clicks login
     * This is a convenience method that combines multiple actions
     *
     * @param username - Username to login with
     * @param password - Password to login with
     */
    public void login(String username, String password) {
        // Wait for page to be fully loaded
        waitHelper.waitForElementVisible(usernameField);

        // Enter username
        enterUsername(username);

        // Small pause to let React update state
        try { Thread.sleep(500); } catch (InterruptedException e) {}

        // Enter password
        enterPassword(password);

        // Small pause before clicking submit
        try { Thread.sleep(500); } catch (InterruptedException e) {}

        // Click login and wait for navigation
        clickLoginButton();

        // Wait for either success (redirect) or error message
        try { Thread.sleep(2000); } catch (InterruptedException e) {}
    }

    // ===== VERIFICATION METHODS (REQUIREMENT: Use asserts) =====
    // These methods return values that can be used in assertions

    /**
     * Check if login was successful by checking URL
     * After successful login, YelpCamp should redirect to /campgrounds
     */
    public boolean isLoginSuccessful() {
        return waitHelper.waitForUrlContains("/campgrounds");
    }

    /**
     * Check if error message is displayed (for failed login)
     */
    public boolean isErrorMessageDisplayed() {
        return waitHelper.isElementDisplayed(errorMessage);
    }

    /**
     * Get the error message text
     */
    public String getErrorMessage() {
        waitHelper.waitForElementVisible(errorMessage);
        return errorMessage.getText();
    }

    /**
     * Check if login page is loaded
     */
    public boolean isLoginPageLoaded() {
        return waitHelper.isElementDisplayed(loginHeading);
    }
}
