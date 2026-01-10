package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utils.WaitHelper;

/**
 * BasePage - Base class for all pages (Page Object Model)
 */
public class BasePage {
    protected WebDriver driver;
    protected WaitHelper waitHelper;
    protected Actions actions;
    protected JavascriptExecutor js;

    // Account menu elements
    @FindBy(xpath = "//a[@class='skip-link skip-account']")
    protected WebElement accountMenu;

    @FindBy(xpath = "//a[@title='Register']")
    protected WebElement registerLink;

    @FindBy(xpath = "//a[@title='Log In']")
    protected WebElement signInLink;

    @FindBy(xpath = "//a[@title='Log Out']")
    protected WebElement logOutLink;

    @FindBy(xpath = "//a[@title='My Wishlist']")
    protected WebElement myWishListLink;

    // Top navigation menu
    @FindBy(xpath = "//a[@class='level0 has-children' and contains(text(),'Women')]")
    protected WebElement womenMenu;

    @FindBy(xpath = "//a[@class='level0 has-children' and contains(text(),'Men')]")
    protected WebElement menMenu;

    @FindBy(xpath = "//a[@class='level0 has-children' and contains(text(),'Sale')]")
    protected WebElement saleMenu;

    // View All menu options
    @FindBy(xpath = "//li[contains(@class,'level1') and contains(@class,'view-all')]//a[contains(text(),'View All Women')]")
    protected WebElement viewAllWomenLink;

    @FindBy(xpath = "//li[contains(@class,'level1') and contains(@class,'view-all')]//a[contains(text(),'View All Men')]")
    protected WebElement viewAllMenLink;

    @FindBy(xpath = "//li[contains(@class,'level1') and contains(@class,'view-all')]//a[contains(text(),'View All Sale')]")
    protected WebElement viewAllSaleLink;

    // Shopping cart (handles both empty cart with 'no-count' and cart with items)
    @FindBy(xpath = "//a[contains(@class,'skip-link') and contains(@class,'skip-cart')]")
    protected WebElement shoppingCartLink;

    // Username display
    @FindBy(xpath = "//p[@class='welcome-msg']")
    protected WebElement welcomeMessage;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.waitHelper = new WaitHelper(driver);
        this.actions = new Actions(driver);
        this.js = (JavascriptExecutor) driver;
        PageFactory.initElements(driver, this);
    }

    public void clickAccountMenu() {
        waitHelper.waitForElementClickable(accountMenu);
        accountMenu.click();
        // Wait a moment for dropdown to appear
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void clickRegister() {
        clickAccountMenu();
        waitHelper.waitForElementVisible(registerLink);
        waitHelper.waitForElementClickable(registerLink);
        registerLink.click();
    }

    public void clickSignIn() {
        clickAccountMenu();
        waitHelper.waitForElementVisible(signInLink);
        waitHelper.waitForElementClickable(signInLink);
        signInLink.click();
    }

    public void clickLogOut() {
        clickAccountMenu();
        waitHelper.waitForElementVisible(logOutLink);
        waitHelper.waitForElementClickable(logOutLink);
        logOutLink.click();
    }

    public void clickMyWishList() {
        clickAccountMenu();

        // Try to find wishlist link dynamically
        try {
            WebElement wishlistLink = driver.findElement(
                By.xpath("//a[@title='My Wishlist' or contains(@href,'wishlist') or contains(text(),'Wishlist')]"));

            waitHelper.waitForElementClickable(wishlistLink);
            try {
                wishlistLink.click();
            } catch (Exception e) {
                js.executeScript("arguments[0].click();", wishlistLink);
            }
        } catch (Exception e) {
            System.out.println("Error clicking My Wishlist: " + e.getMessage());
            // Fallback: try the FindBy element
            try {
                waitHelper.waitForElementClickable(myWishListLink);
                js.executeScript("arguments[0].click();", myWishListLink);
            } catch (Exception ex) {
                System.out.println("Could not click wishlist link");
            }
        }

        // Wait for wishlist page to load
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void hoverOverWomenAndClickViewAll() {
        waitHelper.waitForElementVisible(womenMenu);
        scrollToElement(womenMenu);

        // Perform hover action
        actions.moveToElement(womenMenu).perform();

        // Wait for submenu to appear after hover
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Find the View All link dynamically after hover
        try {
            WebElement viewAllLink = driver.findElement(
                By.xpath("//nav[@id='nav']//a[contains(text(),'View All Women')]"));

            // Use JavaScript click for better reliability
            js.executeScript("arguments[0].click();", viewAllLink);
        } catch (Exception e) {
            // Fallback: Try to click the main Women link
            js.executeScript("arguments[0].click();", womenMenu);
        }

        // Wait for page to load
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void hoverOverMenAndClickViewAll() {
        waitHelper.waitForElementVisible(menMenu);
        scrollToElement(menMenu);

        // Perform hover action
        actions.moveToElement(menMenu).perform();

        // Wait for submenu to appear after hover
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Find the View All link dynamically after hover
        try {
            WebElement viewAllLink = driver.findElement(
                By.xpath("//nav[@id='nav']//a[contains(text(),'View All Men')]"));

            // Use JavaScript click for better reliability
            js.executeScript("arguments[0].click();", viewAllLink);
        } catch (Exception e) {
            // Fallback: Try to click the main Men link
            js.executeScript("arguments[0].click();", menMenu);
        }

        // Wait for page to load
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void hoverOverSaleAndClickViewAll() {
        waitHelper.waitForElementVisible(saleMenu);
        scrollToElement(saleMenu);

        // Perform hover action
        actions.moveToElement(saleMenu).perform();

        // Wait for submenu to appear after hover
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Find the View All link dynamically after hover
        try {
            WebElement viewAllLink = driver.findElement(
                By.xpath("//nav[@id='nav']//a[contains(text(),'View All Sale')]"));

            // Use JavaScript click for better reliability
            js.executeScript("arguments[0].click();", viewAllLink);
        } catch (Exception e) {
            // Fallback: Try to click the main Sale link
            js.executeScript("arguments[0].click();", saleMenu);
        }

        // Wait for page to load
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void clickShoppingCart() {
        try {
            // First, click the shopping cart dropdown to open it
            WebElement cartLink = driver.findElement(
                By.xpath("//a[contains(@class,'skip-link') and contains(@class,'skip-cart')]"));

            waitHelper.waitForElementClickable(cartLink);
            System.out.println("Clicking shopping cart dropdown");

            try {
                cartLink.click();
            } catch (Exception e) {
                // Use JavaScript click as fallback
                System.out.println("Using JavaScript to click shopping cart");
                js.executeScript("arguments[0].click();", cartLink);
            }

            // Wait for dropdown to open
            Thread.sleep(1000);

            // Now click "View Cart" or "Go to Cart" link inside the dropdown
            try {
                WebElement viewCartLink = driver.findElement(
                    By.xpath("//a[contains(text(),'View Cart') or contains(text(),'Go to Cart') or contains(@href,'/checkout/cart')]"));

                System.out.println("Clicking View Cart link");
                waitHelper.waitForElementClickable(viewCartLink);
                js.executeScript("arguments[0].click();", viewCartLink);
            } catch (Exception e) {
                System.out.println("Could not find View Cart link, trying direct navigation");
                // If no View Cart link found, navigate directly to cart URL
                String baseUrl = driver.getCurrentUrl();
                if (baseUrl.contains("ecommerce.tealiumdemo.com")) {
                    driver.get("https://ecommerce.tealiumdemo.com/checkout/cart/");
                    System.out.println("Navigated directly to cart page");
                }
            }
        } catch (Exception e) {
            System.out.println("Error accessing shopping cart: " + e.getMessage());
            // Try direct navigation as last resort
            try {
                driver.get("https://ecommerce.tealiumdemo.com/checkout/cart/");
                System.out.println("Used direct navigation to cart page");
            } catch (Exception ex) {
                System.out.println("Could not navigate to cart page");
            }
        }

        // Wait for cart page to load
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        System.out.println("After cart navigation, URL: " + driver.getCurrentUrl());
    }

    public boolean isUserLoggedIn() {
        return waitHelper.isElementDisplayed(welcomeMessage);
    }

    public String getWelcomeMessageText() {
        waitHelper.waitForElementVisible(welcomeMessage);
        return welcomeMessage.getText();
    }

    public String getAccountMenuText() {
        clickAccountMenu();
        // Get the text from the dropdown menu after clicking
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Check if wishlist link contains item count
        if (waitHelper.isElementDisplayed(myWishListLink)) {
            String wishlistText = myWishListLink.getText();
            System.out.println("Wishlist link text: " + wishlistText);
            return wishlistText;
        }

        // Try alternative selector for wishlist with count
        try {
            WebElement wishlistWithCount = driver.findElement(
                By.xpath("//a[@title='My Wishlist' or contains(text(),'Wishlist')]"));
            String text = wishlistWithCount.getText();
            System.out.println("Alternative wishlist text: " + text);
            return text;
        } catch (Exception e) {
            System.out.println("Could not find wishlist text");
        }

        return "";
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    public void scrollToElement(WebElement element) {
        js.executeScript("arguments[0].scrollIntoView(true);", element);
    }
}
