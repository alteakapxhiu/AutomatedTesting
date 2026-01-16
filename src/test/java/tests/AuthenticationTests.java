package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.LoginPage;
import pages.RegisterPage;

/**
 * AccountTests - Tests 1 and 2 for Tealium E-commerce Application
 */
public class AuthenticationTests extends BaseTest {

    private static String testEmail;
    private static String testPassword;

    /**
     * Test 1: Create an Account
     * 1. Navigate to: https://ecommerce.tealiumdemo.com/
     * 2. Click Account and then Register button.
     * 3. Check title of the open page.
     * 4. Fill in form fields.
     * 5. Click Register button.
     * 6. Check successful message is displayed on the screen.
     * 7. Click on Account and Log Out.
     */
    @Test(priority = 1, description = "Test 1: Create an Account")
    public void testCreateAccount() {
        System.out.println("\nTEST 1 STARTED: Create an Account");
        System.out.println("Description: Register a new user account\n");

        HomePage homePage = new HomePage(driver);
        RegisterPage registerPage = new RegisterPage(driver);

        // Step 1: Navigate to homepage
        homePage.navigateToHomePage();
        Assert.assertTrue(homePage.isHomePageLoaded(), "Home page should be loaded");

        // Step 2: Click Account and Register
        homePage.clickRegister();

        // Step 3: Check title of the page
        Assert.assertTrue(registerPage.isRegisterPageLoaded(), "Register page should be loaded");
        String pageTitle = registerPage.getPageTitle();
        Assert.assertTrue(pageTitle.contains("Create") || pageTitle.contains("Account"),
                "Page title should contain 'Create' or 'Account'");

        // Step 4: Fill in form fields
        String timestamp = String.valueOf(System.currentTimeMillis());
        String firstName = "Test";
        String lastName = "User" + timestamp;
        testEmail = "testuser" + timestamp + "@test.com";
        testPassword = "Test@1234";

        registerPage.registerUser(firstName, lastName, testEmail, testPassword);

        // Step 5 & 6: Check successful message is displayed
        Assert.assertTrue(registerPage.isSuccessMessageDisplayed(),
                "Success message should be displayed after registration");

        // Step 7: Click on Account and Log Out
        registerPage.clickLogOut();

        System.out.println("TEST 1 COMPLETED SUCCESSFULLY\n");
    }

    /**
     * Test 2: Sign In
     * 1. Navigate to: https://ecommerce.tealiumdemo.com/
     * 2. Click on Account then Sign in.
     * 3. Login with existing credentials.
     * 4. Check your username is displayed on right corner of the page.
     * 5. Click on Account and Log Out.
     *
     * NOTE: This test uses pre-existing credentials. Make sure to create an account first
     * or update the credentials below.
     */
    @Test(priority = 2, description = "Test 2: Sign In")
    public void testSignIn() {
        System.out.println("\nTEST 2 STARTED: Sign In");
        System.out.println("Description: Login with existing credentials\n");

        HomePage homePage = new HomePage(driver);
        LoginPage loginPage = new LoginPage(driver);

        // Use credentials from Test 1 if available, otherwise use config properties
        String emailToUse = (testEmail != null && !testEmail.isEmpty()) ? testEmail : utils.ConfigReader.getTestEmail();
        String passwordToUse = (testPassword != null && !testPassword.isEmpty()) ? testPassword : utils.ConfigReader.getTestPassword();

        // Step 1: Navigate to homepage
        homePage.navigateToHomePage();
        Assert.assertTrue(homePage.isHomePageLoaded(), "Home page should be loaded");

        // Step 2: Click on Account then Sign in
        homePage.clickSignIn();
        Assert.assertTrue(loginPage.isLoginPageLoaded(), "Login page should be loaded");

        // Step 3: Login with credentials
        loginPage.login(emailToUse, passwordToUse);

        // Step 4: Check username is displayed
        Assert.assertTrue(homePage.isUserLoggedIn(), "User should be logged in");
        String welcomeMsg = homePage.getWelcomeMessageText();
        Assert.assertTrue(welcomeMsg.contains("WELCOME") || welcomeMsg.toLowerCase().contains("hello"),
                "Welcome message should be displayed with username");

        // Step 5: Click on Account and Log Out
        homePage.clickLogOut();

        System.out.println("TEST 2 COMPLETED SUCCESSFULLY\n");
    }
}
