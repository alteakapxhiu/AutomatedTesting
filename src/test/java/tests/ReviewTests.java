package tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.*;

/**
 * ReviewTests - Tests for Review/Comment Functionality
 *
 * REQUIREMENT: Use asserts to do the verifications
 *
 * Test Scenarios Covered:
 * 1. Add review to campground (requires login)
 * 2. View reviews on campground
 * 3. Verify review form not visible when logged out
 */
public class ReviewTests extends BaseTest {

    private String testCampgroundName;

    /**
     * Setup method - runs before each test
     * Creates a test campground to add reviews to
     */
    @BeforeMethod
    public void setupTestCampground() {
        // Login
        LoginPage loginPage = new LoginPage(driver);
        loginPage.navigateToLogin();

        String username = "testuser";  // Update with actual test user
        String password = "Test@123";  // Update with actual password

        loginPage.login(username, password);

        // Create a test campground
        String timestamp = String.valueOf(System.currentTimeMillis());
        testCampgroundName = "Campground for Reviews " + timestamp;

        NewCampgroundPage newCampgroundPage = new NewCampgroundPage(driver);
        newCampgroundPage.navigateToNewCampground();

        // Wait for page to load
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        newCampgroundPage.createCampground(
            testCampgroundName,
            "Review Test Location",
            "35.00",
            "A campground for testing reviews",
            null  // No image upload
        );

        // Wait for campground to be created and page to load
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test 1: Add Review to Campground
     *
     * Pre-requisite: User must be logged in
     *
     * Steps:
     * 1. Navigate to campground details page
     * 2. Verify review form is visible (user is logged in)
     * 3. Fill in review rating and text
     * 4. Submit review
     * 5. Verify review is added successfully
     */
    @Test(priority = 1, description = "Verify logged in user can add review to campground")
    public void testAddReviewToCampground() {
        // Should already be on campground details page from setup
        CampgroundDetailsPage detailsPage = new CampgroundDetailsPage(driver);

        // Verify review form is visible (REQUIREMENT: Use asserts)
        Assert.assertTrue(detailsPage.isReviewFormVisible(),
                "Review form should be visible for logged in users");

        // Add a review
        String rating = "5";
        String reviewText = "This is an excellent campground! " +
                          "Automated test review created at " +
                          System.currentTimeMillis();

        detailsPage.addReview(rating, reviewText);

        // Wait a moment for review to be added
        try {
            Thread.sleep(2000);  // Note: In real tests, replace with proper wait
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Verify review section is displayed
        Assert.assertTrue(detailsPage.areReviewsDisplayed(),
                "Reviews section should be displayed after adding review");

        System.out.println("Add review test passed");
    }

    /**
     * Test 2: View Reviews on Campground
     *
     * Steps:
     * 1. Add a review (from previous test or setup)
     * 2. Navigate to campground details
     * 3. Verify reviews section is visible
     * 4. Verify reviews are displayed
     */
    @Test(priority = 2, description = "Verify reviews are displayed on campground page")
    public void testViewReviewsOnCampground() {
        // First add a review
        CampgroundDetailsPage detailsPage = new CampgroundDetailsPage(driver);

        String rating = "4";
        String reviewText = "Great place for camping! Test review.";

        detailsPage.addReview(rating, reviewText);

        // Wait for review to appear
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Verify reviews are displayed (REQUIREMENT: Use asserts)
        Assert.assertTrue(detailsPage.areReviewsDisplayed(),
                "Reviews should be displayed on campground details page");

        System.out.println("View reviews test passed");
    }

    /**
     * Test 3: Review Form Not Visible When Logged Out
     *
     * Steps:
     * 1. Get URL of current campground (created in @BeforeMethod)
     * 2. Logout user
     * 3. Navigate to campground details page
     * 4. Verify review form is NOT visible
     */
    @Test(priority = 3, description = "Verify review form is hidden when user is logged out")
    public void testReviewFormNotVisibleWhenLoggedOut() {
        // Should already be on campground details page from @BeforeMethod
        // Get the current URL (campground details page)
        String campgroundUrl = driver.getCurrentUrl();
        System.out.println("Current campground URL: " + campgroundUrl);

        // Logout
        HomePage homePage = new HomePage(driver);
        homePage.clickLogoutLink();

        // Wait a moment for logout to complete
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Navigate back to the campground details page while logged out
        driver.get(campgroundUrl);

        // Wait for page to load
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Verify review form is NOT visible (REQUIREMENT: Use asserts)
        CampgroundDetailsPage detailsPage = new CampgroundDetailsPage(driver);

        Assert.assertFalse(detailsPage.isReviewFormVisible(),
                "Review form should NOT be visible for logged out users");

        System.out.println("Review form hidden when logged out test passed");
    }

    /**
     * Test 4: Add Review with Empty Text (Form Validation)
     *
     * Steps:
     * 1. Navigate to campground details
     * 2. Try to submit review with empty fields
     * 3. Verify validation error or review not added
     */
    @Test(priority = 4, description = "Verify review form validates required fields")
    public void testAddReviewWithEmptyFields() {
        CampgroundDetailsPage detailsPage = new CampgroundDetailsPage(driver);

        // Verify on campground details page
        Assert.assertTrue(detailsPage.isCampgroundDetailsPageLoaded(),
                "Should be on campground details page");

        // Try to add review with empty rating and text
        // This will test form validation
        String currentUrl = driver.getCurrentUrl();

        // Try submitting empty review
        detailsPage.addReview("", "");

        // Wait a moment
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Verify still on same page (form validation prevented submission)
        // Or verify error message appeared
        String newUrl = driver.getCurrentUrl();

        Assert.assertEquals(newUrl, currentUrl,
                "Should remain on same page if review validation fails");

        System.out.println("Empty review validation test passed");
    }
}
