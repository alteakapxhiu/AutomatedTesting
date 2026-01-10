package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.LoginPage;

/**
 * DemoTest - Quick demo test to WATCH Selenium in action
 *
 * This test runs SLOWER than normal tests so you can SEE what's happening
 * You'll literally watch the browser:
 * - Navigate to pages
 * - Fill in forms
 * - Click buttons
 * - All automatically!
 *
 * Run this test FIRST to see Selenium work before running the full test suite
 */
public class DemoTest extends BaseTest {

    /**
     * Simple login test with pauses so you can WATCH the automation
     *
     * What you'll see:
     * 1. Browser opens to YelpCamp
     * 2. Clicks "Login" link
     * 3. Enters username in the field
     * 4. Enters password in the field
     * 5. Clicks "Login" button
     * 6. Navigates to campgrounds page
     */
    @Test(description = "Demo test - watch Selenium work in slow motion!")
    public void watchSeleniumInAction() throws InterruptedException {
        System.out.println("\n🎬 ===== DEMO TEST STARTING ===== ");
        System.out.println("👀 WATCH THE BROWSER - Selenium is working!\n");

        // Go to home page (already there from BaseTest setup)
        HomePage homePage = new HomePage(driver);

        // Pause so you can SEE the home page loaded
        Thread.sleep(2000);
        System.out.println("✅ Step 1: Opened YelpCamp home page");

        // Click login link - WATCH THE BROWSER CLICK IT!
        homePage.clickLoginLink();
        Thread.sleep(2000);
        System.out.println("✅ Step 2: Clicked 'Login' link");

        // Create login page object
        LoginPage loginPage = new LoginPage(driver);

        // Enter username - WATCH IT TYPE!
        loginPage.enterUsername("testuser");
        Thread.sleep(1000);
        System.out.println("✅ Step 3: Typed username 'testuser'");

        // Enter password - WATCH IT TYPE!
        loginPage.enterPassword("Test@123");
        Thread.sleep(1000);
        System.out.println("✅ Step 4: Typed password");

        // Click login button - WATCH THE CLICK!
        loginPage.clickLoginButton();
        Thread.sleep(2000);
        System.out.println("✅ Step 5: Clicked 'Login' button");

        // Check where we ended up
        String currentUrl = driver.getCurrentUrl();
        System.out.println("✅ Step 6: Current URL is: " + currentUrl);

        // Keep browser open for 3 more seconds so you can see the result
        Thread.sleep(3000);

        System.out.println("\n🎉 DEMO TEST COMPLETED!");
        System.out.println("Did you see the browser work automatically? Cool, right?\n");

        // Browser will close now (from BaseTest teardown)
    }
}
