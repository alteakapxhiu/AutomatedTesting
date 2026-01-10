package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * LoginPage - Tealium E-commerce Login Page
 */
public class LoginPage extends BasePage {

    @FindBy(id = "email")
    private WebElement emailField;

    @FindBy(id = "pass")
    private WebElement passwordField;

    @FindBy(id = "send2")
    private WebElement loginButton;

    @FindBy(xpath = "//h1[contains(text(),'Customer Login') or contains(text(),'Login')]")
    private WebElement loginHeading;

    @FindBy(xpath = "//li[@class='error-msg']//span")
    private WebElement errorMessage;

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void navigateToLoginPage() {
        driver.get(utils.ConfigReader.getBaseUrl() + "customer/account/login/");
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

    public void clickLoginButton() {
        waitHelper.waitForElementClickable(loginButton);
        scrollToElement(loginButton);
        js.executeScript("arguments[0].click();", loginButton);
    }

    public void login(String email, String password) {
        waitHelper.waitForElementVisible(emailField);
        enterEmail(email);
        enterPassword(password);
        clickLoginButton();
    }

    public boolean isLoginPageLoaded() {
        return waitHelper.isElementDisplayed(loginHeading);
    }

    public boolean isErrorMessageDisplayed() {
        return waitHelper.isElementDisplayed(errorMessage);
    }

    public String getErrorMessage() {
        waitHelper.waitForElementVisible(errorMessage);
        return errorMessage.getText();
    }
}
