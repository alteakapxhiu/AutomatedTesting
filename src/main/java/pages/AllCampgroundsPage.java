package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

/**
 * AllCampgroundsPage - Page Object for Campgrounds Listing Page
 *
 * URL: http://localhost:5173/campgrounds
 *
 * This page shows:
 * - List of all campgrounds
 * - Each campground card with name, location, description
 * - Link to view individual campground details
 * - Search/filter options (if available)
 */
public class AllCampgroundsPage extends BasePage {

    // ===== PAGE ELEMENTS =====

    @FindBy(xpath = "//h1[contains(text(),'Campgrounds')]")  // React uses "Campgrounds" heading
    private WebElement pageHeading;

    @FindBy(xpath = "//div[@class='card']")  // React campground cards
    private List<WebElement> campgroundCards;

    @FindBy(xpath = "//button[contains(@class,'btn-outline-light')]")  // React uses a + button
    private WebElement addCampgroundButton;

    @FindBy(xpath = "//input[@type='search' or @placeholder='Search']")
    private WebElement searchBox;

    /**
     * Constructor
     */
    public AllCampgroundsPage(WebDriver driver) {
        super(driver);
    }

    // ===== PAGE ACTIONS =====

    /**
     * Navigate to all campgrounds page
     */
    public void navigateToAllCampgrounds() {
        driver.get(utils.ConfigReader.getBaseUrl() + "/campgrounds");
    }

    /**
     * Click on a campground by its name to view details
     *
     * @param campgroundName - Name of the campground to click
     */
    public void clickCampgroundByName(String campgroundName) {
        WebElement campground = driver.findElement(
            By.xpath("//a[contains(text(),'" + campgroundName + "')] | " +
                     "//h2[contains(text(),'" + campgroundName + "')]/..")
        );
        waitHelper.waitForElementClickable(campground);
        campground.click();
    }

    /**
     * Click "Add Campground" or "New Campground" button
     */
    public void clickAddCampground() {
        waitHelper.waitForElementClickable(addCampgroundButton);
        addCampgroundButton.click();
    }

    /**
     * Search for campgrounds
     */
    public void searchCampgrounds(String searchTerm) {
        if (waitHelper.isElementDisplayed(searchBox)) {
            searchBox.clear();
            searchBox.sendKeys(searchTerm);
        }
    }

    // ===== VERIFICATION METHODS =====

    /**
     * Check if campgrounds page is loaded
     */
    public boolean isCampgroundsPageLoaded() {
        return waitHelper.isElementDisplayed(pageHeading);
    }

    /**
     * Get the number of campgrounds displayed
     */
    public int getCampgroundCount() {
        return campgroundCards.size();
    }

    /**
     * Check if a specific campground is displayed on the page
     *
     * @param campgroundName - Name of campground to check
     * @return true if campground is found, false otherwise
     */
    public boolean isCampgroundDisplayed(String campgroundName) {
        try {
            WebElement campground = driver.findElement(
                By.xpath("//*[contains(text(),'" + campgroundName + "')]")
            );
            return campground.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if "Add Campground" button is visible
     * This button only appears when user is logged in
     */
    public boolean isAddCampgroundButtonVisible() {
        return waitHelper.isElementDisplayed(addCampgroundButton);
    }
}
