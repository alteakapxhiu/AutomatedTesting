# Tealium E-commerce Automated Tests

Selenium + Java automated testing framework for the Tealium E-commerce Demo application.

## Project Overview

This project automates 8 test scenarios for the Tealium E-commerce demo website using Selenium WebDriver, Java, and TestNG framework. The tests are organized using the Page Object Model (POM) design pattern for better maintainability and code reusability.

**Application Under Test:** https://ecommerce.tealiumdemo.com/

## Technologies Used

- **Java 11** - Programming language
- **Selenium WebDriver 4.16.1** - Browser automation
- **TestNG 7.8.0** - Test framework
- **WebDriverManager 5.6.3** - Automatic driver management
- **ExtentReports 5.1.1** - HTML test reporting
- **Maven** - Build and dependency management

## Project Structure

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

## Features

1. **Page Object Model (POM)** - Clean separation of test logic and page elements
2. **TestNG Framework** - Organized test execution with priorities and dependencies
3. **Explicit Waits** - Proper synchronization with WebDriverWait
4. **Screenshot on Failure** - Automatic screenshot capture when tests fail
5. **ExtentReports** - Professional HTML test reports
6. **WebDriverManager** - Automatic browser driver management
7. **Configurable** - Browser and timeout settings in properties file

## Test Scenarios

### Test 1: Create an Account
- Navigate to the website
- Click Account and Register
- Verify page title
- Fill registration form
- Verify success message
- Logout

### Test 2: Sign In
- Navigate to the website
- Click Account and Sign In
- Login with credentials from Test 1
- Verify username is displayed
- Logout

### Test 3: Check Hover Style
- Sign in to the application
- Navigate to Women products
- Hover over a product
- Verify hover style changes

### Test 4: Check Sale Products Style
- Sign in to the application
- Navigate to Sale products
- Verify multiple prices (original + discounted)
- Verify original price is grey and strikethrough
- Verify final price is blue and not strikethrough

### Test 5: Check Page Filters
- Sign in to the application
- Navigate to Men products
- Filter by black color
- Verify selected color is bordered in blue
- Filter by price range ($0.00 - $99.99)
- Verify only 3 products are displayed
- Verify all prices match the criteria

### Test 6: Check Sorting
- Sign in to the application
- Navigate to Women products
- Sort by Price
- Verify products are sorted correctly
- Add two products to wishlist
- Verify wishlist count shows 2 items

### Test 7: Shopping Cart Test
- Go to Wishlist
- Add products to cart with size and color
- Open Shopping Cart
- Change quantity to 2 for one product
- Verify Grand Total equals sum of item prices

### Test 8: Empty Shopping Cart Test
- Delete items from cart one by one
- Verify item count decreases
- Verify empty cart message is displayed

## Setup Instructions

### Prerequisites

- Java JDK 11 or higher
- Maven 3.6 or higher
- Chrome/Firefox/Edge browser installed

### Installation

1. Clone the repository:
```bash
git clone <repository-url>
cd AlbaniaCampAutomatedTests
```

2. Install dependencies:
```bash
mvn clean install
```

### Configuration

Edit `src/test/resources/config.properties` to configure:

```properties
# Browser: chrome, firefox, edge
browser=chrome

# Timeouts (in seconds)
implicit.wait=10
explicit.wait=15
page.load.timeout=30
```

## Running Tests

### Important: Test Account Setup

Tests 3-8 require a valid test account. You have two options:

**Option 1: Create account via Test 1 (Recommended)**
1. Run Test 1 only:
   ```bash
   mvn test -Dtest=AuthenticationTests#testCreateAccount
   ```
2. Note the email and password from the test output
3. Update `EcommerceTests.java` with these credentials:
   ```java
   private static String testEmail = "your-email@test.com";
   private static String testPassword = "Test@1234";
   ```

**Option 2: Create account manually**
1. Go to https://ecommerce.tealiumdemo.com/
2. Create an account manually
3. Update the credentials in `EcommerceTests.java`

### Run all tests:
```bash
mvn test
```

### Run specific test:
```bash
mvn test -Dtest=AuthenticationTests#testCreateAccount
mvn test -Dtest=AuthenticationTests#testSignIn
```

### Run specific test suite:
```bash
mvn test -DsuiteXmlFile=src/test/resources/testng.xml
```

### Run tests in specific browser:
```bash
mvn test -Dbrowser=firefox
```

### Run via IntelliJ:
1. Right-click on `src/test/resources/testng.xml`
2. Select "Run testng.xml"

## Test Reports

After test execution, reports are generated in:
- **ExtentReports:** `test-output/ExtentReport.html`
- **TestNG Reports:** `test-output/index.html`
- **Screenshots:** `screenshots/` (on failure)

## Key Design Decisions

1. **Page Object Model** - Each page is represented by a class with its elements and methods
2. **Base Page** - Common navigation elements are in BasePage to avoid duplication
3. **Explicit Waits** - Using WebDriverWait instead of Thread.sleep for better reliability
4. **Test Dependencies** - Some tests depend on previous tests (e.g., Test 7 depends on Test 6)
5. **Data Management** - Test credentials are generated dynamically to avoid conflicts

## Best Practices Followed

- Avoided `Thread.sleep()` where possible, using explicit waits instead
- Used assertions to verify each test step
- Implemented screenshot capture on test failure
- Used meaningful test and method names
- Added comprehensive comments and documentation
- Organized code using POM structure

## Project Requirements Compliance

This project follows the Selenium + Java Miniproject requirements:

| Requirement | Implementation | Status |
|-------------|----------------|--------|
| **1. Selenium + Java Framework** | Selenium WebDriver 4.16.1 + Java 11 | ✅ Complete |
| **2. JUnit / TestNG** | TestNG 7.8.0 with test suites | ✅ Complete |
| **3. Page Object Model** | All pages in `pages` package | ✅ Complete |
| **4. Assertions** | TestNG Assert for all verifications | ✅ Complete |
| **5. Wait Methods** | WaitHelper with explicit waits | ✅ Complete |
| **6. Screenshot on Failure** | TestListener captures screenshots | ✅ Complete |
| **7. Reports** | ExtentReports with HTML reports | ✅ Complete |
| **8. Additional Features** | Multi-browser support, ConfigReader | ✅ Complete |

## Git Commands

```bash
# Initialize repository
git init

# Add all files
git add .

# Commit changes
git commit -m "Tealium E-commerce automation tests implementation"

# Add remote repository
git remote add origin <your-repository-url>

# Push to GitHub
git push -u origin master
```

## Submission

Submit the project to: **sidorela.bano@lhind.dlh.de**

## Notes

- The project includes proper wait mechanisms (explicit waits)
- Screenshot capture is configured on test failure
- ExtentReports provides detailed HTML reports
- All tests use assertions for verification
- Test execution order is controlled using TestNG priorities
- Tests 3-8 require user to be signed in (handled in @BeforeClass)
- Test 7 depends on Test 6 (wishlist items)
- Test 8 depends on Test 7 (shopping cart items)

## Troubleshooting

### Tests fail with "Element not found"
- Wait for page to load completely
- Check element locators match the current page structure
- Increase explicit wait timeout in config.properties

### Browser driver issues
- WebDriverManager handles driver downloads automatically
- Check internet connection
- Update WebDriverManager version if needed

### Login tests fail
- Ensure you have valid credentials or create a new account
- Check if the site requires CAPTCHA
- Verify the site is accessible

## Author

Developed as a mini-project for Selenium + Java automation training.

---

**Date:** January 2026
**Framework:** Selenium + Java + TestNG + Maven
**Design Pattern:** Page Object Model
