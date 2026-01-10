package tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.*;

/**
 * CampgroundTests - Tests for Campground CRUD Operations
 *
 * REQUIREMENT: Use asserts to do the verifications
 *
 * Test Scenarios Covered:
 * 1. View all campgrounds
 * 2. View individual campground details
 * 3. Create new campground (requires login)
 * 4. Edit campground (requires author authorization)
 * 5. Delete campground (requires author authorization)
 */
public class CampgroundTests extends BaseTest {

    private String testCampgroundName;

    /**
     * Setup method - runs before each test
     * Logs in a user before each test (since most campground operations require authentication)
     */
    @BeforeMethod
    public void loginBeforeTest() {
        // Login before each test
        LoginPage loginPage = new LoginPage(driver);
        loginPage.navigateToLogin();

        // Use test credentials (update these based on your YelpCamp setup)
        String username = "testuser";  // Update with actual test user
        String password = "Test@123";  // Update with actual password

        loginPage.login(username, password);

        // Wait for login to complete
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Generate unique campground name for testing
        String timestamp = String.valueOf(System.currentTimeMillis());
        testCampgroundName = "Test Campground " + timestamp;
    }

    /**
     * Test 1: View All Campgrounds
     *
     * Steps:
     * 1. Navigate to all campgrounds page
     * 2. Verify page loads successfully
     * 3. Verify campgrounds are displayed (if any exist)
     */
    @Test(priority = 1, description = "Verify user can view all campgrounds")
    public void testViewAllCampgrounds() {
        AllCampgroundsPage campgroundsPage = new AllCampgroundsPage(driver);

        // Navigate to campgrounds page
        campgroundsPage.navigateToAllCampgrounds();

        // Verify page loaded (REQUIREMENT: Use asserts)
        Assert.assertTrue(campgroundsPage.isCampgroundsPageLoaded(),
                "Campgrounds page should be loaded successfully");

        // Verify campgrounds are displayed (if any exist)
        int campgroundCount = campgroundsPage.getCampgroundCount();
        System.out.println("Found " + campgroundCount + " campgrounds");

        // If logged in, "Add Campground" button should be visible
        Assert.assertTrue(campgroundsPage.isAddCampgroundButtonVisible(),
                "Add Campground button should be visible for logged in users");

        System.out.println("View all campgrounds test passed");
    }

    /**
     * Test 2: Create New Campground
     *
     * Pre-requisite: User must be logged in
     *
     * Steps:
     * 1. Navigate to New Campground page
     * 2. Fill in campground details
     * 3. Submit the form
     * 4. Verify campground is created successfully
     * 5. Verify redirect to campground details page
     */
    @Test(priority = 2, description = "Verify user can create a new campground")
    public void testCreateNewCampground() {
        NewCampgroundPage newCampgroundPage = new NewCampgroundPage(driver);

        // Navigate to new campground page
        newCampgroundPage.navigateToNewCampground();

        // Verify page loaded
        Assert.assertTrue(newCampgroundPage.isNewCampgroundPageLoaded(),
                "New Campground page should be loaded");

        // Fill in campground details
        String location = "Test Location, Albania";
        String price = "25.00";
        String description = "This is a test campground created by automated tests";

        // Pass null for image - React uses file upload, not URL
        newCampgroundPage.createCampground(
            testCampgroundName,
            location,
            price,
            description,
            null  // No image upload for this test
        );

        // Verify campground created successfully (REQUIREMENT: Use asserts)
        Assert.assertTrue(newCampgroundPage.isCampgroundCreated(),
                "Campground should be created and redirect to details page");

        // Verify we're on the campground details page
        CampgroundDetailsPage detailsPage = new CampgroundDetailsPage(driver);
        String displayedName = detailsPage.getCampgroundName();

        Assert.assertTrue(displayedName.contains(testCampgroundName) ||
                         displayedName.equalsIgnoreCase(testCampgroundName),
                "Created campground name should match: " + testCampgroundName);

        System.out.println("Create campground test passed: " + testCampgroundName);
    }

    /**
     * Test 3: View Campground Details
     *
     * Steps:
     * 1. Navigate to all campgrounds
     * 2. Click on a campground
     * 3. Verify campground details page loads
     * 4. Verify campground information is displayed
     */
    @Test(priority = 3, description = "Verify user can view campground details")
    public void testViewCampgroundDetails() {
        AllCampgroundsPage campgroundsPage = new AllCampgroundsPage(driver);
        campgroundsPage.navigateToAllCampgrounds();

        // Create a campground first
        NewCampgroundPage newCampgroundPage = new NewCampgroundPage(driver);
        newCampgroundPage.navigateToNewCampground();
        newCampgroundPage.createCampground(
            testCampgroundName,
            "Test Location",
            "30.00",
            "Test description",
            null  // No image upload
        );

        // Now verify details are displayed
        CampgroundDetailsPage detailsPage = new CampgroundDetailsPage(driver);

        // Verify details page loaded (REQUIREMENT: Use asserts)
        Assert.assertTrue(detailsPage.isCampgroundDetailsPageLoaded(),
                "Campground details page should be loaded");

        // Verify campground information is displayed
        String name = detailsPage.getCampgroundName();
        Assert.assertNotNull(name, "Campground name should be displayed");
        Assert.assertFalse(name.isEmpty(), "Campground name should not be empty");

        String location = detailsPage.getCampgroundLocation();
        Assert.assertNotNull(location, "Campground location should be displayed");

        String price = detailsPage.getCampgroundPrice();
        Assert.assertNotNull(price, "Campground price should be displayed");

        System.out.println("View campground details test passed");
    }

    /**
     * Test 4: Edit Campground (as Author)
     *
     * Pre-requisite: User must be logged in and be the author of the campground
     *
     * Steps:
     * 1. Create a campground
     * 2. Verify Edit button is visible (author authorization)
     * 3. Click Edit button
     * 4. Modify campground details
     * 5. Save changes
     * 6. Verify changes are saved
     */
    @Test(priority = 4, description = "Verify campground author can edit campground")
    public void testEditCampgroundAsAuthor() {
        // First create a campground
        NewCampgroundPage newCampgroundPage = new NewCampgroundPage(driver);
        newCampgroundPage.navigateToNewCampground();
        newCampgroundPage.createCampground(
            testCampgroundName,
            "Original Location",
            "20.00",
            "Original description",
            null  // No image upload
        );

        // Now on campground details page
        CampgroundDetailsPage detailsPage = new CampgroundDetailsPage(driver);

        // Verify Edit button is visible (REQUIREMENT: Use asserts)
        Assert.assertTrue(detailsPage.isEditButtonVisible(),
                "Edit button should be visible for campground author");

        // Click Edit button
        detailsPage.clickEdit();

        // Should be on edit page now (similar to new campground page)
        // Note: You may need to create EditCampgroundPage if it's different from NewCampgroundPage
        // For now, verify URL contains "edit"
        Assert.assertTrue(driver.getCurrentUrl().contains("/edit"),
                "Should navigate to edit page");

        System.out.println("Edit campground test passed");
    }

    /**
     * Test 5: Delete Campground (as Author)
     *
     * Pre-requisite: User must be logged in and be the author of the campground
     *
     * Steps:
     * 1. Create a campground
     * 2. Verify Delete button is visible
     * 3. Click Delete button
     * 4. Verify campground is deleted
     * 5. Verify redirect to campgrounds page
     */
    @Test(priority = 5, description = "Verify campground author can delete campground")
    public void testDeleteCampgroundAsAuthor() {
        // First create a campground
        NewCampgroundPage newCampgroundPage = new NewCampgroundPage(driver);
        newCampgroundPage.navigateToNewCampground();
        newCampgroundPage.createCampground(
            testCampgroundName,
            "Location to delete",
            "15.00",
            "This campground will be deleted",
            null  // No image upload
        );

        // Now on campground details page
        CampgroundDetailsPage detailsPage = new CampgroundDetailsPage(driver);

        // Verify Delete button is visible (REQUIREMENT: Use asserts)
        Assert.assertTrue(detailsPage.isDeleteButtonVisible(),
                "Delete button should be visible for campground author");

        // Click Delete button
        detailsPage.clickDelete();

        // Should redirect to campgrounds page
        AllCampgroundsPage campgroundsPage = new AllCampgroundsPage(driver);
        Assert.assertTrue(campgroundsPage.isCampgroundsPageLoaded(),
                "Should redirect to all campgrounds page after deletion");

        // Verify campground is no longer displayed
        Assert.assertFalse(campgroundsPage.isCampgroundDisplayed(testCampgroundName),
                "Deleted campground should not appear in campgrounds list");

        System.out.println("Delete campground test passed");
    }

    /**
     * Test 6: Create Campground with Missing Fields (Form Validation)
     *
     * Steps:
     * 1. Navigate to New Campground page
     * 2. Submit form with missing required fields
     * 3. Verify validation error appears
     */
    @Test(priority = 6, description = "Verify form validation for required fields")
    public void testCreateCampgroundWithMissingFields() {
        NewCampgroundPage newCampgroundPage = new NewCampgroundPage(driver);
        newCampgroundPage.navigateToNewCampground();

        // Try to submit with only name filled
        newCampgroundPage.enterName("Incomplete Campground");
        newCampgroundPage.clickSubmit();

        // Verify validation error or still on same page
        boolean hasValidation = newCampgroundPage.isErrorMessageDisplayed() ||
                               driver.getCurrentUrl().contains("/new");

        Assert.assertTrue(hasValidation,
                "Form should validate required fields before submission");

        System.out.println("Form validation test passed");
    }
}
