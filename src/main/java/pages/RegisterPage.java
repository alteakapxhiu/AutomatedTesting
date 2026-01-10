package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * RegisterPage - Page Object for User Registration Page
 *
 * URL: http://localhost:5173/register
 *
 * This page has:
 * - Username input
 * - Email input
 * - Password input
 * - Register/Sign Up button
 * - Error messages (if registration fails)
 */
public class RegisterPage extends BasePage {

    // ===== PAGE ELEMENTS =====

    @FindBy(id = "username")
    private WebElement usernameField;

    @FindBy(id = "email")
    private WebElement emailField;

    @FindBy(id = "password")
    private WebElement passwordField;

    @FindBy(xpath = "//button[@type='submit' and contains(text(),'Submit')]")  // React uses "Submit" button text
    private WebElement registerButton;

    @FindBy(xpath = "//div[contains(@class,'alert') or contains(@class,'error')]")
    private WebElement errorMessage;

    @FindBy(xpath = "//h5[contains(text(),'Register')]")  // React uses h5, not h1
    private WebElement registerHeading;

    @FindBy(linkText = "Login")
    private WebElement loginLink;

    /**
     * Constructor
     */
    public RegisterPage(WebDriver driver) {
        super(driver);
    }

    // ===== PAGE ACTIONS =====

    /**
     * Navigate to registration page
     */
    public void navigateToRegister() {
        driver.get(utils.ConfigReader.getBaseUrl() + "/register");
    }

    /**
     * Enter username
     */
    public void enterUsername(String username) {
        waitHelper.waitForElementVisible(usernameField);
        usernameField.clear();
        usernameField.sendKeys(username);
    }

    /**
     * Enter email
     */
    public void enterEmail(String email) {
        waitHelper.waitForElementVisible(emailField);
        emailField.clear();
        emailField.sendKeys(email);
    }

    /**
     * Enter password
     */
    public void enterPassword(String password) {
        waitHelper.waitForElementVisible(passwordField);
        passwordField.clear();
        passwordField.sendKeys(password);
    }

    /**
     * Click register button
     * Uses JavaScript click to avoid ElementClickInterceptedException
     */
    public void clickRegisterButton() {
        waitHelper.waitForElementClickable(registerButton);

        // Scroll button into view
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
            "arguments[0].scrollIntoView({block: 'center'});", registerButton
        );

        // Small wait after scroll
        try { Thread.sleep(500); } catch (InterruptedException e) {}

        // Use JavaScript click
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
            "arguments[0].click();", registerButton
        );
    }

    /**
     * Complete registration - fills all fields and submits
     *
     * @param username - Username for new account
     * @param email - Email for new account
     * @param password - Password for new account
     */
    public void register(String username, String email, String password) {
        enterUsername(username);
        enterEmail(email);
        enterPassword(password);
        clickRegisterButton();
    }

    // ===== VERIFICATION METHODS =====

    /**
     * Check if registration was successful
     * After successful registration, should redirect to campgrounds or login
     */
    public boolean isRegistrationSuccessful() {
        // Registration might redirect to /campgrounds or /login
        return waitHelper.waitForUrlContains("/campgrounds") ||
               waitHelper.waitForUrlContains("/login");
    }

    /**
     * Check if error message is displayed
     */
    public boolean isErrorMessageDisplayed() {
        return waitHelper.isElementDisplayed(errorMessage);
    }

    /**
     * Get error message text
     */
    public String getErrorMessage() {
        waitHelper.waitForElementVisible(errorMessage);
        return errorMessage.getText();
    }

    /**
     * Check if register page is loaded
     */
    public boolean isRegisterPageLoaded() {
        return waitHelper.isElementDisplayed(registerHeading);
    }
}
