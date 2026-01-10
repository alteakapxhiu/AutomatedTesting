package tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.*;

/**
 * EcommerceTests - Tests 3-8 for Tealium E-commerce Application
 * These tests require the user to be signed in
 *
 * IMPORTANT: Before running these tests, you need to create a test account:
 * 1. Run Test 1 (testCreateAccount) to create an account
 * 2. Update the testEmail and testPassword below with the created credentials
 * OR
 * 1. Manually create an account on https://ecommerce.tealiumdemo.com/
 * 2. Update the credentials below
 */
public class EcommerceTests extends BaseTest {

    // TODO: Update these credentials with a valid test account
    private static String testEmail = "altea_kapxhiu@universitetipolis.edu.al";
    private static String testPassword = "AlteaPolis2004";

    /**
     * Login before each test (runs after BaseTest.setup())
     */
    @BeforeMethod(dependsOnMethods = "setup")
    public void loginBeforeEachTest() {
        HomePage homePage = new HomePage(driver);
        LoginPage loginPage = new LoginPage(driver);

        homePage.clickSignIn();
        loginPage.login(testEmail, testPassword);
    }

    /**
     * Test 3: Check hover style
     * Precondition: Sign In Tealium Application
     * 1. Hover over Woman and on the open pop up click on View All Woman menu option.
     * 2. Hover over one of the displayed products.
     * 3. Assert that the styles have changed to indicate a hover effect.
     */
    @Test(priority = 3, description = "Test 3: Check hover style")
    public void testCheckHoverStyle() {
        HomePage homePage = new HomePage(driver);
        ProductListPage productListPage = new ProductListPage(driver);

        // Step 1: Hover over Woman and click View All Woman
        homePage.hoverOverWomenAndClickViewAll();

        // Step 2 & 3: Hover over a product and check style changes
        String beforeHoverStyle = productListPage.getProductStyleAttribute(0, "opacity");
        productListPage.hoverOverProduct(0);

        // Wait a moment for hover effect
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String afterHoverStyle = productListPage.getProductStyleAttribute(0, "opacity");

        // Assert that styles have changed (this could be opacity, border, shadow, etc.)
        Assert.assertNotEquals(beforeHoverStyle, afterHoverStyle,
                "Product style should change on hover");
    }

    /**
     * Test 4: Check sale products style
     * Precondition: Sign In Tealium Application
     * 1. Hover over Sale and on the open pop up click on View All Sale menu option.
     * 2. For each, check if multiple prices are shown (original + discounted).
     * 3. Verify that the original price has grey color and is strikethrough.
     * 4. Verify that the final price does not have a strikethrough and has blue color.
     */
    @Test(priority = 4, description = "Test 4: Check sale products style")
    public void testCheckSaleProductsStyle() {
        HomePage homePage = new HomePage(driver);
        ProductListPage productListPage = new ProductListPage(driver);

        // Step 1: Hover over Sale and click View All Sale
        homePage.hoverOverSaleAndClickViewAll();

        int productCount = productListPage.getProductCount();
        Assert.assertTrue(productCount > 0, "Sale products should be displayed");

        // Steps 2-4: Check each product's price styling
        for (int i = 0; i < productCount; i++) {
            // Step 2: Check if multiple prices are shown
            boolean hasMultiplePrices = productListPage.hasMultiplePrices(i);

            if (hasMultiplePrices) {
                // Step 3: Verify original price is grey and strikethrough
                Assert.assertTrue(productListPage.isOriginalPriceStrikethrough(i),
                        "Original price should be grey and strikethrough for product " + i);

                // Step 4: Verify final price is blue and not strikethrough
                Assert.assertTrue(productListPage.isFinalPriceBlueAndNotStrikethrough(i),
                        "Final price should be blue and not strikethrough for product " + i);
            }
        }
    }

    /**
     * Test 5: Check page filters
     * Precondition: Sign In Tealium Application
     * 1. Hover over Man and on the open pop up click on View All Men menu option.
     * 2. From Shopping Options panel click on black color.
     * 3. Check that all the displayed products have the selected color bordered in blue.
     * 4. Click on Price dropdown and select the first option ($0.00 - $99.99) and check that only three
     * products are displayed on the page.
     * 5. For each product displayed, check that the price matches the defined criteria.
     */
    @Test(priority = 5, description = "Test 5: Check page filters")
    public void testCheckPageFilters() {
        HomePage homePage = new HomePage(driver);
        ProductListPage productListPage = new ProductListPage(driver);

        // Step 1: Hover over Man and click View All Men
        homePage.hoverOverMenAndClickViewAll();

        // Step 2: Click on black color filter
        productListPage.clickColorFilter("Black");

        // Wait for filter to apply
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Step 3: Check products have selected color bordered in blue
        int productsAfterColorFilter = productListPage.getProductCount();
        for (int i = 0; i < productsAfterColorFilter; i++) {
            Assert.assertTrue(productListPage.isProductColorBorderedInBlue(i),
                    "Product " + i + " should have selected color bordered in blue");
        }

        // Step 4: Click on Price filter and select first option
        productListPage.clickPriceFilter(0);

        // Wait for filter to apply
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int productsAfterPriceFilter = productListPage.getProductCount();
        Assert.assertEquals(productsAfterPriceFilter, 3,
                "Only 3 products should be displayed after price filter");

        // Step 5: Check each product price matches criteria ($0.00 - $99.99)
        for (int i = 0; i < productsAfterPriceFilter; i++) {
            Assert.assertTrue(productListPage.isProductPriceInRange(i, 0.0, 99.99),
                    "Product " + i + " price should be in range $0.00 - $99.99");
        }
    }

    /**
     * Test 6: Check Sorting
     * Precondition: Sign In Tealium Application
     * 1. Hover over Woman and on the open pop up click on View All Woman menu option.
     * 2. Click on Sort By dropdown and Select Price.
     * 3. Check products are displayed sorted based on their price.
     * 4. Add two first products on the wishlist.
     * 5. Click on Account and check the correct number of items is displayed (My Wish List (2 items)).
     */
    @Test(priority = 6, description = "Test 6: Check Sorting")
    public void testCheckSorting() {
        HomePage homePage = new HomePage(driver);
        ProductListPage productListPage = new ProductListPage(driver);

        // Step 1: Hover over Woman and click View All Woman
        homePage.hoverOverWomenAndClickViewAll();

        // Step 2: Sort by Price
        productListPage.selectSortBy("Price");

        // Wait for sorting to apply
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Step 3: Check products are sorted by price
        Assert.assertTrue(productListPage.areProductsSortedByPrice(),
                "Products should be sorted by price in ascending order");

        // Step 4: Add first two products to wishlist
        productListPage.addProductToWishlist(0);
        productListPage.addProductToWishlist(1);

        // Step 5: Check wishlist count
        String accountMenuText = homePage.getAccountMenuText();
        Assert.assertTrue(accountMenuText.contains("(2") || accountMenuText.contains("2 items"),
                "Wishlist should show 2 items");
    }

    /**
     * Test 7: Shopping Cart test
     * Precondition: Test 6
     * 1. Go to My Wishlist.
     * 2. Add the products to the shopping cart (Select color and size).
     * 3. Open the Shopping Cart, change quantity to 2 for one of the products and click Update.
     * 4. Verify that the prices sum for all items is equal to Grand Total price.
     */
    @Test(priority = 7, description = "Test 7: Shopping Cart test", dependsOnMethods = "testCheckSorting")
    public void testShoppingCart() {
        HomePage homePage = new HomePage(driver);
        WishlistPage wishlistPage = new WishlistPage(driver);
        ShoppingCartPage shoppingCartPage = new ShoppingCartPage(driver);

        // Step 1: Go to My Wishlist
        homePage.clickMyWishList();
        Assert.assertTrue(wishlistPage.isWishlistPageLoaded(), "Wishlist page should be loaded");

        // Step 2: Add products to shopping cart
        int wishlistCount = wishlistPage.getWishlistItemCount();
        for (int i = 0; i < wishlistCount; i++) {
            wishlistPage.addItemToCart(i, "M", "Blue");
        }

        // Step 3: Open Shopping Cart and update quantity
        homePage.clickShoppingCart();
        Assert.assertTrue(shoppingCartPage.isShoppingCartPageLoaded(),
                "Shopping cart page should be loaded");

        shoppingCartPage.updateQuantity(0, 2);
        shoppingCartPage.clickUpdateButton();

        // Step 4: Verify total
        Assert.assertTrue(shoppingCartPage.verifyGrandTotalMatchesItemsSum(),
                "Grand total should match sum of item prices");
    }

    /**
     * Test 8: Empty Shopping Cart Test
     * Precondition: Test 7
     * 1. Delete the first item on shopping cart.
     * 2. Verify that the number of elements in Shopping Cart table is decreased by 1.
     * 3. Repeat steps 1&2 until the last item is deleted.
     * 4. Verify that Shopping Cart is empty (Check message 'You have no items in your shopping cart.' is displayed).
     */
    @Test(priority = 8, description = "Test 8: Empty Shopping Cart Test", dependsOnMethods = "testShoppingCart")
    public void testEmptyShoppingCart() {
        ShoppingCartPage shoppingCartPage = new ShoppingCartPage(driver);

        int initialCount = shoppingCartPage.getCartItemCount();
        Assert.assertTrue(initialCount > 0, "Shopping cart should have items");

        // Steps 1-3: Delete items one by one
        for (int i = initialCount; i > 0; i--) {
            shoppingCartPage.deleteItem(0);

            // Wait for deletion to process
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            int newCount = shoppingCartPage.getCartItemCount();
            Assert.assertEquals(newCount, i - 1,
                    "Cart should have " + (i - 1) + " items after deletion");
        }

        // Step 4: Verify cart is empty
        Assert.assertTrue(shoppingCartPage.isCartEmpty(),
                "Shopping cart should be empty");
        String emptyMessage = shoppingCartPage.getEmptyCartMessage();
        Assert.assertTrue(emptyMessage.contains("You have no items in your shopping cart"),
                "Empty cart message should be displayed");
    }
}
