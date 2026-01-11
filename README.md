# Automated Testing per Faqen Tealium

## Pershkrimi i Projektit

Ky projekt ka per qellim **automatizimin e testimit te funksionaliteteve kryesore** te faqes:

**Faqja per tu testuar:** https://ecommerce.tealiumdemo.com/

## Testimi eshte realizuar duke perdorur:

- **Java 11** - Programming language
- **Selenium WebDriver 4.16.1** - Browser automation
- **TestNG 7.8.0** - Test framework
- **WebDriverManager 5.6.3** - Automatic driver management
- **ExtentReports 5.1.1** - HTML test reporting
- **Maven** - Build and dependency management
  **Page Object Model (POM)** - Arkitektura

Ne total jane implementuar **8 test case kryesore** qe mbulojne rrjedhen e plote te perdoruesit: nga krijimi i llogarise deri te blerja dhe boshatisja e shportes.

## Struktura e projektit

Projekti eshte organizuar sipas **Page Object Model**:

- `pages/` → permban te gjitha Page Object classes
- `tests/` → permban testet automatike
- `utils/` → permban helper classes (waits, screenshots, driver manager, etj)

```
AlbaniaCampAutomatedTests/
├── src/
│   ├── main/
│   │   └── java/
│   │       ├── pages/                  # Page Object Model classes
│   │       │   ├── BasePage.java       # Base page with common elements
│   │       │   ├── HomePage.java       # Home page
│   │       │   ├── RegisterPage.java   # Registration page
│   │       │   ├── LoginPage.java      # Login page
│   │       │   ├── ProductListPage.java # Product listing page
│   │       │   ├── WishlistPage.java   # Wishlist page
│   │       │   └── ShoppingCartPage.java # Shopping cart page
│   │       └── utils/                  # Utility classes
│   │           ├── ConfigReader.java   # Configuration reader
│   │           ├── DriverManager.java  # WebDriver manager
│   │           ├── ScreenshotUtil.java # Screenshot utility
│   │           └── WaitHelper.java     # Wait utility
│   └── test/
│       ├── java/
│       │   ├── listeners/              # TestNG listeners
│       │   │   ├── ExtentManager.java  # ExtentReports manager
│       │   │   └── TestListener.java   # Test listener for reports
│       │   └── tests/                  # Test classes
│       │       ├── BaseTest.java       # Base test setup
│       │       ├── AuthenticationTests.java # Account tests (Tests 1-2)
│       │       └── EcommerceTests.java # E-commerce tests (Tests 3-8)
│       └── resources/
│           ├── config.properties       # Configuration file
│           └── testng.xml              # TestNG suite configuration
└── pom.xml                             # Maven dependencies
```

## Test Case Kryesore

### Test 1: Create an Account

1. Navigate to: https://ecommerce.tealiumdemo.com/
2. Click Account and then Register button
3. Check title of the open page
4. Fill in form fields
5. Click Register button
6. Check successful message is displayed on the screen
7. Click on Account and Log Out

### Test 2: Sign In

1. Navigate to: https://ecommerce.tealiumdemo.com/
2. Click on Account then Sign in
3. Login with the credentials created from Test 1
4. Check your username is displayed on right corner of the page
5. Click on Account and Log Out

### Test 3: Check hover style

**Precondition:** Sign In Tealium Application

1. Hover over Woman and click View All Woman menu option
2. Hover over one of the displayed products
3. Assert that the styles have changed to indicate a hover effect

### Test 4: Check sale products style

**Precondition:** Sign In Tealium Application

1. Hover over Sale and click View All Sale menu option
2. For each product, check if multiple prices are shown (original + discounted)
3. Verify that the original price has grey color and is strikethrough
4. Verify that the final price does not have a strikethrough and has blue color

### Test 5: Check page filters

**Precondition:** Sign In Tealium Application

1. Hover over Man and click View All Men menu option
2. From Shopping Options panel, click on black color
3. Check that all displayed products have the selected color bordered in blue
4. Click on Price dropdown and select the first option ($0.00 - $99.99)
5. Check that only three products are displayed on the page
6. For each displayed product, check that the price matches the defined criteria

### Test 6: Check Sorting

**Precondition:** Sign In Tealium Application

1. Hover over Woman and click View All Woman menu option
2. Click on Sort By dropdown and select Price
3. Check products are displayed sorted based on their price
4. Add the first two products to the wishlist
5. Click on Account and check the correct number of items is displayed (My Wish List - 2 items)

### Test 7: Shopping Cart Test

**Precondition:** Test 6

1. Go to My Wishlist
2. Add the products to the shopping cart (select color and size)
3. Open the Shopping Cart, change quantity to 2 for one of the products and click Update
4. Verify that the sum of prices for all items equals the Grand Total price

### Test 8: Empty Shopping Cart Test

**Precondition:** Test 7

1. Delete the first item in the shopping cart
2. Verify that the number of elements in the shopping cart table decreased by 1
3. Repeat steps 1 & 2 until the last item is deleted
4. Verify that the shopping cart is empty (check message ‘You have no items in your shopping cart.’ is displayed)
5. Close the browser

## Funksionalitete Shtese

- **Screenshot capture** on test failure
- **Reusable driver sessions**
- **Explicit waits** (avoid Thread.sleep)
- Optional: **Test report generation**

## Udhëzime per ta hapur lokalisht

1. **Clone project nga GitHub**
2. **Build me Maven**:

```bash
mvn clean install
```
