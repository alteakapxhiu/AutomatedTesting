package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.LoginPage;
import pages.RegisterPage;

/**
 * AuthenticationTests - Tests for User Authentication (Login, Register, Logout)
 *
 * REQUIREMENT: Use asserts to do the verifications
 *
 * Test Scenarios Covered:
 * 1. User Registration with valid credentials
 * 2. User Login with valid credentials
 * 3. User Login with invalid credentials
 * 4. User Logout
 */
public class AuthenticationTests extends BaseTest {

    /**
     * Test 1: Successful User Registration
     *
     * Steps:
     * 1. Navigate to Register page
     * 2. Fill in registration form with valid data
     * 3. Submit the form
     * 4. Verify registration is successful (redirect to campgrounds or login)
     */
    @Test(priority = 1, description = "Verify user can register with valid credentials")
    public void testSuccessfulRegistration() {
        // Initialize page object
        RegisterPage registerPage = new RegisterPage(driver);

        // Navigate to register page
        registerPage.navigateToRegister();

        // Verify register page loaded
        Assert.assertTrue(registerPage.isRegisterPageLoaded(),
                "Register page should be loaded");

        // Generate unique username with timestamp to avoid duplicates
        String timestamp = String.valueOf(System.currentTimeMillis());
        String username = "testuser" + timestamp;
        String email = "testuser" + timestamp + "@test.com";
        String password = "Test@123";

        // Fill and submit registration form
        registerPage.register(username, email, password);

        // Verify registration successful (REQUIREMENT: Use asserts)
        Assert.assertTrue(registerPage.isRegistrationSuccessful(),
                "Registration should be successful and redirect to campgrounds or login page");

        System.out.println("Registration test passed for user: " + username);
    }

    /**
     * Test 2: Successful User Login
     *
     * Pre-requisite: User must be registered first (depends on test 1)
     *
     * Steps:
     * 1. Navigate to Login page
     * 2. Enter valid credentials
     * 3. Click Login
     * 4. Verify user is redirected to campgrounds page
     * 5. Verify user is logged in (Logout button visible)
     */
    @Test(priority = 2, description = "Verify user can login with valid credentials")
    public void testSuccessfulLogin() {
        LoginPage loginPage = new LoginPage(driver);

        // Navigate to login page
        loginPage.navigateToLogin();

        // Verify login page loaded
        Assert.assertTrue(loginPage.isLoginPageLoaded(),
                "Login page should be loaded");

        // Login with credentials
        // NOTE: You may need to use credentials from an existing user
        // or modify this to use the user created in registration test
        String username = "testuser";  // Update with actual username
        String password = "Test@123";  // Update with actual password

        loginPage.login(username, password);

        // Verify login successful (REQUIREMENT: Use asserts)
        Assert.assertTrue(loginPage.isLoginSuccessful(),
                "Login should be successful and redirect to campgrounds page");

        // Verify user is logged in
        Assert.assertTrue(loginPage.isUserLoggedIn(),
                "User should be logged in - Logout button should be visible");

        System.out.println("Login test passed for user: " + username);
    }

    /**
     * Test 3: Login with Invalid Credentials
     *
     * Steps:
     * 1. Navigate to Login page
     * 2. Enter invalid credentials
     * 3. Click Login
     * 4. Verify error message is displayed
     * 5. Verify user is NOT redirected (still on login page)
     */
    @Test(priority = 3, description = "Verify login fails with invalid credentials")
    public void testLoginWithInvalidCredentials() {
        LoginPage loginPage = new LoginPage(driver);

        // Navigate to login page
        loginPage.navigateToLogin();

        // Try to login with invalid credentials
        String invalidUsername = "invaliduser12345";
        String invalidPassword = "wrongpassword";

        loginPage.login(invalidUsername, invalidPassword);

        // Verify error message is displayed (REQUIREMENT: Use asserts)
        // Note: This might fail if YelpCamp doesn't show error message
        // In that case, verify URL still contains "/login"
        boolean hasError = loginPage.isErrorMessageDisplayed() ||
                          driver.getCurrentUrl().contains("/login");

        Assert.assertTrue(hasError,
                "Error message should be displayed or user should remain on login page");

        System.out.println("Invalid login test passed - login correctly rejected");
    }

    /**
     * Test 4: User Logout
     *
     * Pre-requisite: User must be logged in first
     *
     * Steps:
     * 1. Login with valid credentials
     * 2. Click Logout button
     * 3. Verify user is logged out (Login button visible)
     * 4. Verify redirect to home page
     */
    @Test(priority = 4, description = "Verify user can logout successfully")
    public void testLogout() {
        // First, login
        LoginPage loginPage = new LoginPage(driver);
        loginPage.navigateToLogin();

        String username = "testuser";  // Update with actual username
        String password = "Test@123";  // Update with actual password

        loginPage.login(username, password);

        // Verify logged in
        Assert.assertTrue(loginPage.isUserLoggedIn(),
                "User should be logged in before logout test");

        // Now logout
        HomePage homePage = new HomePage(driver);
        homePage.clickLogoutLink();

        // Verify user is logged out (REQUIREMENT: Use asserts)
        Assert.assertTrue(homePage.isUserLoggedOut(),
                "User should be logged out - Login button should be visible");

        // Verify not on a restricted page
        String currentUrl = driver.getCurrentUrl();
        Assert.assertFalse(currentUrl.contains("/campgrounds/new"),
                "User should not be able to access New Campground page after logout");

        System.out.println("Logout test passed");
    }

    /**
     * Test 5: Registration with Empty Fields (Form Validation)
     *
     * Steps:
     * 1. Navigate to Register page
     * 2. Submit form without filling any fields
     * 3. Verify error message or validation message appears
     */
    @Test(priority = 5, description = "Verify registration fails with empty fields")
    public void testRegistrationWithEmptyFields() {
        RegisterPage registerPage = new RegisterPage(driver);

        registerPage.navigateToRegister();

        // Try to register with empty fields
        registerPage.clickRegisterButton();

        // Verify still on register page or error shown
        boolean hasValidation = registerPage.isErrorMessageDisplayed() ||
                               driver.getCurrentUrl().contains("/register");

        Assert.assertTrue(hasValidation,
                "Form validation should prevent registration with empty fields");

        System.out.println("Empty fields validation test passed");
    }
}
