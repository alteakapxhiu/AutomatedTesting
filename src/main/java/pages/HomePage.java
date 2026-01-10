package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * HomePage - Page Object for YelpCamp Home/Landing Page
 *
 * URL: http://localhost:5173/
 *
 * This page typically shows:
 * - Welcome message
 * - Navigation to view campgrounds
 * - Links to Login/Register
 */
public class HomePage extends BasePage {

    // ===== PAGE ELEMENTS =====

    @FindBy(xpath = "//h1[contains(text(),'YelpCamp') or contains(text(),'Welcome')]")
    private WebElement welcomeHeading;

    @FindBy(xpath = "//a[contains(text(),'View Campgrounds')]")
    private WebElement viewCampgroundsButton;

    @FindBy(xpath = "//p[contains(text(),'Welcome to YelpCamp')]")
    private WebElement welcomeMessage;

    /**
     * Constructor
     */
    public HomePage(WebDriver driver) {
        super(driver);
    }

    // ===== PAGE ACTIONS =====

    /**
     * Click "View Campgrounds" button to go to all campgrounds page
     */
    public void clickViewCampgrounds() {
        waitHelper.waitForElementClickable(viewCampgroundsButton);
        viewCampgroundsButton.click();
    }

    // ===== VERIFICATION METHODS (REQUIREMENT: Use asserts) =====

    /**
     * Check if home page is loaded by verifying welcome heading is displayed
     * Use this in assertions: Assert.assertTrue(homePage.isHomePageLoaded())
     */
    public boolean isHomePageLoaded() {
        return waitHelper.isElementDisplayed(welcomeHeading);
    }

    /**
     * Get the welcome message text
     */
    public String getWelcomeMessage() {
        waitHelper.waitForElementVisible(welcomeMessage);
        return welcomeMessage.getText();
    }
}
