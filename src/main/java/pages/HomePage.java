package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * HomePage - Tealium E-commerce Home Page
 */
public class HomePage extends BasePage {

    @FindBy(xpath = "//h1")
    private WebElement mainHeading;

    @FindBy(className = "logo")
    private WebElement logo;

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public void navigateToHomePage() {
        driver.get(utils.ConfigReader.getBaseUrl());
    }

    public boolean isHomePageLoaded() {
        return waitHelper.isElementDisplayed(logo);
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    // Methods inherited from BasePage:
    // - clickRegister()
    // - clickSignIn()
    // - clickLogOut()
    // - hoverOverWomenAndClickViewAll()
    // - hoverOverMenAndClickViewAll()
    // - hoverOverSaleAndClickViewAll()
    // - clickMyWishList()
    // - clickShoppingCart()
    // - isUserLoggedIn()
    // - getWelcomeMessageText()
    // - getAccountMenuText()
}
