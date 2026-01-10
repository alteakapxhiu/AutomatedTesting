package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * CampgroundDetailsPage - Page Object for Individual Campground Details Page
 *
 * URL: http://localhost:5173/campgrounds/:id
 *
 * This page shows:
 * - Campground details (name, location, price, description, image)
 * - Reviews section
 * - Form to add new review
 * - Edit/Delete buttons (only for campground author)
 */
public class CampgroundDetailsPage extends BasePage {

    // ===== CAMPGROUND DETAILS ELEMENTS =====
    // Based on ShowCamp.jsx React component

    @FindBy(xpath = "//h5[contains(@class,'card-title')] | //h1 | //h2[contains(@class,'card-title')] | //*[contains(@class,'campground') and contains(@class,'title')] | //div[@class='card']//h5 | //div[contains(@class,'card')]//h1")
    private WebElement campgroundName;

    @FindBy(xpath = "//li[contains(@class,'list-group-item') and contains(@class,'text-muted')] | //li[contains(@class,'list-group-item')][1] | //*[contains(text(),'Location') or contains(text(),'location')]/following-sibling::* | //p[contains(@class,'location')]")
    private WebElement campgroundLocation;

    @FindBy(xpath = "//li[contains(@class,'list-group-item') and contains(text(),'/night')] | //li[contains(@class,'list-group-item') and contains(text(),'$')] | //*[contains(text(),'Price') or contains(text(),'price')]/following-sibling::* | //span[contains(@class,'price')]")
    private WebElement campgroundPrice;

    @FindBy(xpath = "//p[contains(@class,'card-text')] | //div[contains(@class,'description')] | //p[contains(@class,'description')]")
    private WebElement campgroundDescription;

    @FindBy(xpath = "//img[contains(@class,'carousel-img')] | //img[contains(@class,'campground')] | //div[contains(@class,'carousel')]//img")
    private WebElement campgroundImage;

    // ===== ACTION BUTTONS =====
    // React ShowCamp.jsx line 227-229: Only visible if current user is the author
    @FindBy(xpath = "//button[contains(@class,'btn') and (contains(text(),'Edit') or contains(text(),'edit'))] | //a[contains(@class,'btn') and (contains(text(),'Edit') or contains(text(),'edit'))]")
    private WebElement editButton;

    // React ShowCamp.jsx line 225: Only visible if current user is the author
    @FindBy(xpath = "//button[contains(@class,'btn') and (.//i[contains(@class,'trash')] or contains(text(),'Delete') or contains(text(),'delete'))] | //a[contains(@class,'btn') and (contains(text(),'Delete') or contains(text(),'delete'))]")
    private WebElement deleteButton;

    // ===== REVIEW ELEMENTS =====
    // React ShowCamp.jsx line 255-328: Only visible when user is logged in
    // Radio buttons have id="rate1" through "rate5" (ShowCamp.jsx lines 271-317)

    @FindBy(xpath = "//textarea[@id='content'] | //textarea[contains(@placeholder,'review') or contains(@placeholder,'Review') or contains(@placeholder,'comment')] | //textarea[@name='content'] | //textarea[contains(@class,'review')]")
    private WebElement reviewTextField;

    @FindBy(xpath = "//button[@type='submit' and contains(@class,'btn')] | //button[contains(text(),'Submit') and contains(@class,'btn-primary')] | //form[contains(@class,'review')]//button[@type='submit']")
    private WebElement submitReviewButton;

    @FindBy(xpath = "//div[contains(@class,'review') or contains(@class,'comment')]")
    private WebElement reviewsSection;

    @FindBy(xpath = "//h3[contains(text(),'Reviews')]")
    private WebElement reviewsHeading;

    /**
     * Constructor
     */
    public CampgroundDetailsPage(WebDriver driver) {
        super(driver);
    }

    // ===== PAGE ACTIONS =====

    /**
     * Click Edit button to edit campground
     * Only visible if current user is the campground author
     */
    public void clickEdit() {
        waitHelper.waitForElementVisible(editButton);

        // Scroll into view
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
            "arguments[0].scrollIntoView({block: 'center'});", editButton
        );

        try { Thread.sleep(500); } catch (InterruptedException e) {}

        // Use JavaScript click
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
            "arguments[0].click();", editButton
        );
    }

    /**
     * Click Delete button to delete campground
     * Only visible if current user is the campground author
     * Handles the confirmation dialog (ShowCamp.jsx line 118)
     */
    public void clickDelete() {
        waitHelper.waitForElementVisible(deleteButton);

        // Scroll into view
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
            "arguments[0].scrollIntoView({block: 'center'});", deleteButton
        );

        try { Thread.sleep(500); } catch (InterruptedException e) {}

        // Use JavaScript click
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
            "arguments[0].click();", deleteButton
        );

        // Handle the confirmation dialog
        try {
            Thread.sleep(500);  // Wait for alert to appear
            driver.switchTo().alert().accept();  // Click OK on confirmation dialog
        } catch (Exception e) {
            // Alert might not appear if delete fails
        }
    }

    /**
     * Add a review to the campground
     *
     * @param rating - Rating value 1-5 (React uses radio buttons)
     * @param reviewText - Review text/comment
     */
    public void addReview(String rating, String reviewText) {
        // Wait for review form to be visible
        try {
            waitHelper.waitForElementVisible(reviewTextField);
        } catch (Exception e) {
            // Review form might not be visible (user not logged in)
            return;
        }

        // Scroll to review form first
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
            "arguments[0].scrollIntoView({block: 'center'});", reviewTextField
        );

        try { Thread.sleep(500); } catch (InterruptedException e) {}

        // Click the rating radio button using JavaScript (they're hidden inputs)
        // React uses id="rate1" through "rate5" but they're hidden
        // We need to click them with JavaScript or click their labels
        if (rating != null && !rating.isEmpty()) {
            String ratingId = "rate" + rating;

            try {
                // Try finding the label for the rating using multiple strategies
                WebElement ratingLabel = null;
                try {
                    ratingLabel = driver.findElement(
                        org.openqa.selenium.By.xpath("//label[@for='" + ratingId + "']")
                    );
                } catch (Exception ex1) {
                    // Try alternative selectors for star rating
                    try {
                        ratingLabel = driver.findElement(
                            org.openqa.selenium.By.xpath("//input[@id='" + ratingId + "']/following-sibling::label | //input[@id='" + ratingId + "']/preceding-sibling::label")
                        );
                    } catch (Exception ex2) {
                        // Try finding by value
                        ratingLabel = driver.findElement(
                            org.openqa.selenium.By.xpath("//input[@value='" + rating + "']/following-sibling::label | //input[@value='" + rating + "']/preceding-sibling::label")
                        );
                    }
                }

                if (ratingLabel != null) {
                    // Use JavaScript to click the label (more reliable)
                    ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                        "arguments[0].click();", ratingLabel
                    );
                }
            } catch (Exception e) {
                // Fallback: click the radio button directly with JavaScript
                try {
                    WebElement ratingRadio = driver.findElement(org.openqa.selenium.By.id(ratingId));
                    ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                        "arguments[0].checked = true; arguments[0].click(); arguments[0].dispatchEvent(new Event('change', { bubbles: true }));",
                        ratingRadio
                    );
                } catch (Exception ex) {
                    // Last fallback: try by value
                    try {
                        WebElement ratingRadio = driver.findElement(
                            org.openqa.selenium.By.xpath("//input[@type='radio' and @value='" + rating + "']")
                        );
                        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                            "arguments[0].checked = true; arguments[0].click(); arguments[0].dispatchEvent(new Event('change', { bubbles: true }));",
                            ratingRadio
                        );
                    } catch (Exception finalEx) {
                        System.out.println("Could not set rating: " + finalEx.getMessage());
                    }
                }
            }
        }

        try { Thread.sleep(500); } catch (InterruptedException e) {}

        // Enter review text using JavaScript for React compatibility
        waitHelper.waitForElementVisible(reviewTextField);

        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
            "arguments[0].value = ''; arguments[0].dispatchEvent(new Event('input', { bubbles: true }));",
            reviewTextField
        );

        reviewTextField.sendKeys(reviewText);

        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
            "arguments[0].dispatchEvent(new Event('blur', { bubbles: true }));",
            reviewTextField
        );

        try { Thread.sleep(500); } catch (InterruptedException e) {}

        // Submit review - scroll and use JavaScript click
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
            "arguments[0].scrollIntoView({block: 'center'});", submitReviewButton
        );

        try { Thread.sleep(500); } catch (InterruptedException e) {}

        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
            "arguments[0].click();", submitReviewButton
        );

        try { Thread.sleep(1000); } catch (InterruptedException e) {}
    }

    // ===== VERIFICATION METHODS =====

    /**
     * Get campground name displayed on the page
     */
    public String getCampgroundName() {
        waitHelper.waitForElementVisible(campgroundName);
        return campgroundName.getText();
    }

    /**
     * Get campground location
     */
    public String getCampgroundLocation() {
        waitHelper.waitForElementVisible(campgroundLocation);
        return campgroundLocation.getText();
    }

    /**
     * Get campground price
     */
    public String getCampgroundPrice() {
        waitHelper.waitForElementVisible(campgroundPrice);
        return campgroundPrice.getText();
    }

    /**
     * Get campground description
     */
    public String getCampgroundDescription() {
        waitHelper.waitForElementVisible(campgroundDescription);
        return campgroundDescription.getText();
    }

    /**
     * Check if Edit button is visible
     * Edit button only appears for campground author
     */
    public boolean isEditButtonVisible() {
        return waitHelper.isElementDisplayed(editButton);
    }

    /**
     * Check if Delete button is visible
     * Delete button only appears for campground author
     */
    public boolean isDeleteButtonVisible() {
        return waitHelper.isElementDisplayed(deleteButton);
    }

    /**
     * Check if review form is visible
     * Review form only appears when user is logged in
     */
    public boolean isReviewFormVisible() {
        return waitHelper.isElementDisplayed(reviewTextField);
    }

    /**
     * Check if reviews section is displayed
     */
    public boolean areReviewsDisplayed() {
        return waitHelper.isElementDisplayed(reviewsSection);
    }

    /**
     * Check if campground details page is loaded
     */
    public boolean isCampgroundDetailsPageLoaded() {
        return waitHelper.isElementDisplayed(campgroundName);
    }
}
