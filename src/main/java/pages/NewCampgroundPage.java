package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * NewCampgroundPage - Page Object for Create New Campground Page
 *
 * URL: http://localhost:5173/campgrounds/new
 *
 * This page has form to create a new campground:
 * - Campground name
 * - Location
 * - Price
 * - Description
 * - Image URL
 * - Submit button
 */
public class NewCampgroundPage extends BasePage {

    // ===== PAGE ELEMENTS =====

    @FindBy(xpath = "//input[@type='text' and (contains(@placeholder, 'name') or contains(@placeholder, 'Name') or preceding-sibling::label[contains(text(), 'Name')])] | //input[@id='title'] | //input[@name='title']")
    private WebElement nameField;

    @FindBy(xpath = "//input[@type='text' and (contains(@placeholder, 'location') or contains(@placeholder, 'Location') or preceding-sibling::label[contains(text(), 'Location')])] | //input[@id='location'] | //input[@name='location']")
    private WebElement locationField;

    @FindBy(xpath = "//input[@type='number' and (contains(@placeholder, 'price') or contains(@placeholder, 'Price') or preceding-sibling::label[contains(text(), 'Price')])] | //input[@id='price'] | //input[@name='price']")
    private WebElement priceField;

    @FindBy(xpath = "//textarea | //textarea[@id='description'] | //textarea[@name='description'] | //*[@id='description']")
    private WebElement descriptionField;

    @FindBy(xpath = "//input[@type='file'] | //input[@id='images'] | //input[@name='images']")
    private WebElement imageUrlField;

    @FindBy(xpath = "//button[@type='submit'] | //button[contains(text(),'Submit')] | //input[@type='submit'] | //form//button[last()]")
    private WebElement submitButton;

    @FindBy(xpath = "//h1[contains(text(),'New Campground') or contains(text(),'Add Campground')]")
    private WebElement pageHeading;

    @FindBy(xpath = "//div[contains(@class,'alert') or contains(@class,'error')]")
    private WebElement errorMessage;

    /**
     * Constructor
     */
    public NewCampgroundPage(WebDriver driver) {
        super(driver);
    }

    // ===== PAGE ACTIONS =====

    /**
     * Navigate to new campground page
     */
    public void navigateToNewCampground() {
        driver.get(utils.ConfigReader.getBaseUrl() + "/campgrounds/new");

        // Wait for page to fully load
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Wait for form to be ready
        waitHelper.waitForElementVisible(pageHeading);
    }

    /**
     * Enter campground name
     */
    public void enterName(String name) {
        try {
            waitHelper.waitForElementVisible(nameField);
            System.out.println("Name field found, entering: " + name);

            // Scroll field into view first
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center', behavior: 'smooth'});",
                nameField
            );

            try { Thread.sleep(500); } catch (InterruptedException e) {}

            // Click to focus and clear
            nameField.click();
            nameField.clear();

            try { Thread.sleep(300); } catch (InterruptedException e) {}

            // Use sendKeys which triggers proper React events
            nameField.sendKeys(name);

            try { Thread.sleep(500); } catch (InterruptedException e) {}

            // Verify the value was set
            String actualValue = nameField.getAttribute("value");
            System.out.println("Name field value after entry: " + actualValue);

            // Trigger blur event for React form validation
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                "arguments[0].dispatchEvent(new Event('blur', { bubbles: true }));",
                nameField
            );

            try { Thread.sleep(300); } catch (InterruptedException e) {}
        } catch (Exception e) {
            System.out.println("Error entering name: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Enter location
     */
    public void enterLocation(String location) {
        waitHelper.waitForElementVisible(locationField);

        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
            "arguments[0].scrollIntoView({block: 'center', behavior: 'smooth'});",
            locationField
        );

        try { Thread.sleep(500); } catch (InterruptedException e) {}

        locationField.click();
        locationField.clear();

        try { Thread.sleep(300); } catch (InterruptedException e) {}

        locationField.sendKeys(location);

        try { Thread.sleep(500); } catch (InterruptedException e) {}

        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
            "arguments[0].dispatchEvent(new Event('blur', { bubbles: true }));",
            locationField
        );

        try { Thread.sleep(300); } catch (InterruptedException e) {}
    }

    /**
     * Enter price
     */
    public void enterPrice(String price) {
        waitHelper.waitForElementVisible(priceField);

        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
            "arguments[0].scrollIntoView({block: 'center', behavior: 'smooth'});",
            priceField
        );

        try { Thread.sleep(500); } catch (InterruptedException e) {}

        priceField.click();
        priceField.clear();

        try { Thread.sleep(300); } catch (InterruptedException e) {}

        priceField.sendKeys(price);

        try { Thread.sleep(500); } catch (InterruptedException e) {}

        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
            "arguments[0].dispatchEvent(new Event('blur', { bubbles: true }));",
            priceField
        );

        try { Thread.sleep(300); } catch (InterruptedException e) {}
    }

    /**
     * Enter description
     */
    public void enterDescription(String description) {
        try {
            waitHelper.waitForElementVisible(descriptionField);
            System.out.println("Description field found, entering: " + description);

            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center', behavior: 'smooth'});",
                descriptionField
            );

            try { Thread.sleep(500); } catch (InterruptedException e) {}

            descriptionField.click();
            descriptionField.clear();

            try { Thread.sleep(300); } catch (InterruptedException e) {}

            descriptionField.sendKeys(description);

            try { Thread.sleep(500); } catch (InterruptedException e) {}

            // Verify the value was set
            String actualValue = descriptionField.getAttribute("value");
            System.out.println("Description field value after entry: " + actualValue);

            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                "arguments[0].dispatchEvent(new Event('blur', { bubbles: true }));",
                descriptionField
            );

            try { Thread.sleep(300); } catch (InterruptedException e) {}
        } catch (Exception e) {
            System.out.println("Error entering description: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Upload image file (React uses file input, not URL input)
     * NOTE: For file upload, you'd need the file path on your computer
     * Example: "C:\\Users\\USER\\Downloads\\campground.jpg"
     */
    public void uploadImage(String filePath) {
        waitHelper.waitForElementVisible(imageUrlField);
        imageUrlField.sendKeys(filePath);  // For file input, use sendKeys with file path
    }

    /**
     * Click submit button
     * Uses JavaScript click to avoid ElementClickInterceptedException
     */
    public void clickSubmit() {
        try {
            waitHelper.waitForElementClickable(submitButton);

            // Log button details for debugging
            String buttonText = (String) ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                "return arguments[0].textContent;", submitButton
            );
            System.out.println("Found submit button with text: " + buttonText);

            // Scroll button into view
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center', behavior: 'smooth'});", submitButton
            );

            // Small wait after scroll
            try { Thread.sleep(500); } catch (InterruptedException e) {}

            // Check if button is enabled
            Boolean isEnabled = (Boolean) ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                "return !arguments[0].disabled;", submitButton
            );
            System.out.println("Submit button enabled: " + isEnabled);

            // Use JavaScript click to avoid ElementClickInterceptedException
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                "arguments[0].click();", submitButton
            );

            System.out.println("Submit button clicked");

            // Wait for form submission to process
            try { Thread.sleep(2000); } catch (InterruptedException e) {}
        } catch (Exception e) {
            System.out.println("Error clicking submit button: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Create new campground - fills all fields and submits
     *
     * @param name - Campground name (goes in "title" field)
     * @param location - Campground location
     * @param price - Price per night
     * @param description - Description of campground
     * @param imagePath - File path to image (optional, can be null to skip image upload)
     */
    public void createCampground(String name, String location, String price,
                                 String description, String imagePath) {
        enterName(name);
        enterLocation(location);
        enterPrice(price);
        enterDescription(description);
        if (imagePath != null && !imagePath.isEmpty()) {
            uploadImage(imagePath);
        }
        clickSubmit();
    }

    // ===== VERIFICATION METHODS =====

    /**
     * Check if new campground page is loaded
     */
    public boolean isNewCampgroundPageLoaded() {
        return waitHelper.isElementDisplayed(pageHeading);
    }

    /**
     * Check if campground was created successfully
     * After creation, should redirect to campground details or all campgrounds
     */
    public boolean isCampgroundCreated() {
        // Wait longer for redirect to happen
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Check if URL changed from /new to a details page
        String currentUrl = driver.getCurrentUrl();
        System.out.println("Current URL after submit: " + currentUrl);

        // Should redirect to a campground details page (contains ID) or show error
        return currentUrl.contains("/campgrounds/") && !currentUrl.contains("/new");
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
}
