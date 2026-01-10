package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * RegisterPage - Tealium E-commerce Registration Page
 */
public class RegisterPage extends BasePage {

    @FindBy(id = "firstname")
    private WebElement firstNameField;

    @FindBy(id = "middlename")
    private WebElement middleNameField;

    @FindBy(id = "lastname")
    private WebElement lastNameField;

    @FindBy(id = "email_address")
    private WebElement emailField;

    @FindBy(id = "password")
    private WebElement passwordField;

    @FindBy(id = "confirmation")
    private WebElement confirmPasswordField;

    @FindBy(id = "is_subscribed")
    private WebElement newsletterCheckbox;

    @FindBy(xpath = "//button[@title='Register']")
    private WebElement registerButton;

    @FindBy(xpath = "//h1[contains(text(),'Create an Account')]")
    private WebElement pageHeading;

    @FindBy(xpath = "//li[@class='success-msg']//span")
    private WebElement successMessage;

    public RegisterPage(WebDriver driver) {
        super(driver);
    }

    public void enterFirstName(String firstName) {
        waitHelper.waitForElementVisible(firstNameField);
        firstNameField.clear();
        firstNameField.sendKeys(firstName);
    }

    public void enterMiddleName(String middleName) {
        waitHelper.waitForElementVisible(middleNameField);
        middleNameField.clear();
        middleNameField.sendKeys(middleName);
    }

    public void enterLastName(String lastName) {
        waitHelper.waitForElementVisible(lastNameField);
        lastNameField.clear();
        lastNameField.sendKeys(lastName);
    }

    public void enterEmail(String email) {
        waitHelper.waitForElementVisible(emailField);
        emailField.clear();
        emailField.sendKeys(email);
    }

    public void enterPassword(String password) {
        waitHelper.waitForElementVisible(passwordField);
        passwordField.clear();
        passwordField.sendKeys(password);
    }

    public void enterConfirmPassword(String confirmPassword) {
        waitHelper.waitForElementVisible(confirmPasswordField);
        confirmPasswordField.clear();
        confirmPasswordField.sendKeys(confirmPassword);
    }

    public void checkNewsletterSubscription() {
        if (!newsletterCheckbox.isSelected()) {
            newsletterCheckbox.click();
        }
    }

    public void clickRegisterButton() {
        waitHelper.waitForElementClickable(registerButton);
        scrollToElement(registerButton);
        js.executeScript("arguments[0].click();", registerButton);
    }

    public void registerUser(String firstName, String lastName, String email, String password) {
        waitHelper.waitForElementVisible(firstNameField);
        enterFirstName(firstName);
        enterLastName(lastName);
        enterEmail(email);
        enterPassword(password);
        enterConfirmPassword(password);
        clickRegisterButton();
    }

    public boolean isRegisterPageLoaded() {
        return waitHelper.isElementDisplayed(pageHeading);
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    public boolean isSuccessMessageDisplayed() {
        return waitHelper.isElementDisplayed(successMessage);
    }

    public String getSuccessMessage() {
        waitHelper.waitForElementVisible(successMessage);
        return successMessage.getText();
    }

    public void clickLogOut() {
        clickAccountMenu();
        waitHelper.waitForElementVisible(logOutLink);
        waitHelper.waitForElementClickable(logOutLink);
        try {
            logOutLink.click();
        } catch (Exception e) {
            js.executeScript("arguments[0].click();", logOutLink);
        }
    }
}
