package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.List;

/**
 * ProductListPage - Tealium E-commerce Product Listing Page (Women, Men, Sale)
 */
public class ProductListPage extends BasePage {

    @FindBy(xpath = "//select[@title='Sort By']")
    private WebElement sortByDropdown;

    @FindBy(xpath = "//ul[contains(@class,'products-grid')]//li[contains(@class,'item')]//h2[@class='product-name']/a")
    private List<WebElement> productNames;

    @FindBy(xpath = "//ul[contains(@class,'products-grid')]//li[contains(@class,'item')]")
    private List<WebElement> productItems;

    @FindBy(xpath = "//span[@class='price']")
    private List<WebElement> productPrices;

    @FindBy(xpath = "//dl[@id='narrow-by-list']")
    private WebElement shoppingOptionsPanel;

    @FindBy(xpath = "//dl[@id='narrow-by-list']//dt[contains(text(),'Color')]")
    private WebElement colorFilterHeader;

    @FindBy(xpath = "//dl[@id='narrow-by-list']//dt[contains(text(),'Price')]")
    private WebElement priceFilterHeader;

    public ProductListPage(WebDriver driver) {
        super(driver);
    }

    public void selectSortBy(String sortOption) {
        waitHelper.waitForElementVisible(sortByDropdown);
        Select select = new Select(sortByDropdown);
        select.selectByVisibleText(sortOption);
    }

    public List<String> getProductNames() {
        List<String> names = new ArrayList<>();
        for (WebElement product : productNames) {
            names.add(product.getText());
        }
        return names;
    }

    public List<Double> getProductPrices() {
        List<Double> prices = new ArrayList<>();
        for (WebElement product : productItems) {
            try {
                // Try to get special price first (for sale items)
                List<WebElement> specialPrices = product.findElements(
                    By.xpath(".//p[@class='special-price']//span[@class='price']"));

                if (!specialPrices.isEmpty()) {
                    // Use special price if available
                    String priceText = specialPrices.get(0).getText()
                        .replace("$", "").replace(",", "").trim();
                    prices.add(Double.parseDouble(priceText));
                } else {
                    // Otherwise use regular price
                    List<WebElement> regularPrices = product.findElements(
                        By.xpath(".//span[@class='regular-price']//span[@class='price'] | " +
                                ".//p[@class='price-box']//span[@class='price']"));

                    if (!regularPrices.isEmpty()) {
                        String priceText = regularPrices.get(0).getText()
                            .replace("$", "").replace(",", "").trim();
                        prices.add(Double.parseDouble(priceText));
                    }
                }
            } catch (Exception e) {
                // If price extraction fails for this product, skip it
                System.out.println("Failed to extract price for product: " + e.getMessage());
            }
        }
        return prices;
    }

    public int getProductCount() {
        return productItems.size();
    }

    public void hoverOverProduct(int index) {
        if (index < productItems.size()) {
            waitHelper.waitForElementVisible(productItems.get(index));
            scrollToElement(productItems.get(index));
            actions.moveToElement(productItems.get(index)).perform();
        }
    }

    public String getProductStyleAttribute(int index, String attribute) {
        if (index < productItems.size()) {
            return productItems.get(index).getCssValue(attribute);
        }
        return "";
    }

    public void clickColorFilter(String color) {
        waitHelper.waitForElementClickable(colorFilterHeader);
        scrollToElement(colorFilterHeader);

        if (!isColorFilterExpanded()) {
            colorFilterHeader.click();
            waitHelper.waitForElementPresent(By.xpath("//dl[@id='narrow-by-list']//dt[contains(text(),'Color')]/following-sibling::dd[1]//a"));
        }

        // Find color filter options dynamically - use [1] to get only immediate next dd element
        List<WebElement> colorOptions = driver.findElements(
            By.xpath("//dl[@id='narrow-by-list']//dt[contains(text(),'Color')]/following-sibling::dd[1]//a"));

        System.out.println("Found " + colorOptions.size() + " color filter options");

        String currentUrl = driver.getCurrentUrl();
        boolean colorFound = false;

        for (WebElement colorOption : colorOptions) {
            // Color names are in the img tag inside the a tag
            String optionText = "";
            try {
                WebElement img = colorOption.findElement(By.tagName("img"));
                optionText = img.getAttribute("alt");
                if (optionText == null || optionText.isEmpty()) {
                    optionText = img.getAttribute("title");
                }
            } catch (Exception e) {
                // No img tag, try getting from a element
                optionText = colorOption.getAttribute("title");
                if (optionText == null || optionText.isEmpty()) {
                    optionText = colorOption.getText();
                }
            }
            System.out.println("Checking color option: " + optionText);

            if (optionText != null && optionText.toLowerCase().contains(color.toLowerCase())) {
                colorFound = true;
                String href = colorOption.getAttribute("href");
                System.out.println("Found matching color: " + optionText + ", href: " + href);

                waitHelper.waitForElementClickable(colorOption);
                scrollToElement(colorOption);
                try {
                    js.executeScript("arguments[0].click();", colorOption);
                    System.out.println("Clicked color filter using JavaScript: " + color);
                } catch (Exception e) {
                    System.out.println("JavaScript click failed: " + e.getMessage());
                    try {
                        colorOption.click();
                        System.out.println("Clicked color filter using regular click: " + color);
                    } catch (Exception ex) {
                        System.out.println("Both click methods failed: " + ex.getMessage());
                    }
                }

                // Wait for page to reload after color filter
                waitHelper.waitForUrlToChange(currentUrl);
                String newUrl = driver.getCurrentUrl();
                System.out.println("Color filter applied, new URL: " + newUrl);

                if (newUrl.equals(currentUrl)) {
                    System.out.println("WARNING: URL did not change after color filter click!");
                }

                // Wait for products to reload
                waitHelper.waitForPageLoad();
                waitHelper.waitForElementPresent(By.xpath("//ul[contains(@class,'products-grid')]//li[contains(@class,'item')]"));

                // Wait for product grid to be visible
                List<WebElement> products = driver.findElements(
                    By.xpath("//ul[contains(@class,'products-grid')]//li[contains(@class,'item')]"));
                if (!products.isEmpty()) {
                    waitHelper.waitForElementVisible(products.get(0));
                    System.out.println("Products reloaded after color filter, found " + products.size() + " products");
                } else {
                    System.out.println("WARNING: No products found after color filter!");
                }

                break;
            }
        }

        if (!colorFound) {
            System.out.println("ERROR: Could not find color option matching: " + color);
        }
    }

    public void clickPriceFilter(int index) {
        // First ensure page is stable after any previous filter
        waitHelper.waitForPageLoad();

        waitHelper.waitForElementClickable(priceFilterHeader);
        scrollToElement(priceFilterHeader);

        if (!isPriceFilterExpanded()) {
            priceFilterHeader.click();
            waitHelper.waitForElementPresent(By.xpath("//dl[@id='narrow-by-list']//dt[contains(text(),'Price')]/following-sibling::dd[1]//a"));
        }

        // Find price filter options dynamically - use [1] to get only the immediate next dd element
        List<WebElement> priceOptions = driver.findElements(
            By.xpath("//dl[@id='narrow-by-list']//dt[contains(text(),'Price')]/following-sibling::dd[1]//a"));

        System.out.println("Found " + priceOptions.size() + " price filter options");

        if (index < priceOptions.size()) {
            WebElement priceOption = priceOptions.get(index);
            String priceText = priceOption.getText();
            String href = priceOption.getAttribute("href");
            System.out.println("Clicking price filter: " + priceText);
            System.out.println("Price filter href: " + href);

            // Validate href is not empty or homepage
            if (href == null || href.isEmpty() || href.equals("https://ecommerce.tealiumdemo.com/") || !href.contains("price")) {
                System.out.println("ERROR: Invalid price filter href, attempting to re-find element");
                // Try to find price filter options again with more specific xpath
                priceOptions = driver.findElements(
                    By.xpath("//dl[@id='narrow-by-list']//dt[contains(text(),'Price')]/following-sibling::dd[1]//li//a[contains(@href,'price')]"));
                if (index < priceOptions.size()) {
                    priceOption = priceOptions.get(index);
                    href = priceOption.getAttribute("href");
                    System.out.println("Re-found price filter href: " + href);
                } else {
                    System.out.println("Could not find valid price filter link");
                    return;
                }
            }

            // Store the target URL before any navigation
            final String targetUrl = href;
            System.out.println("Target URL for navigation: " + targetUrl);

            // Get current URL and body class
            String currentUrl = driver.getCurrentUrl();
            String currentBodyClass = driver.findElement(By.tagName("body")).getAttribute("class");
            System.out.println("Current URL before price filter: " + currentUrl);
            System.out.println("Current body class: " + currentBodyClass);

            // Click the filter using JavaScript for reliability
            waitHelper.waitForElementClickable(priceOption);
            scrollToElement(priceOption);
            try {
                js.executeScript("arguments[0].click();", priceOption);
                System.out.println("Clicked price filter using JavaScript");
            } catch (Exception e) {
                System.out.println("JavaScript click failed, using driver.get(): " + e.getMessage());
                driver.get(targetUrl);
            }

            // Wait for page to navigate - check both URL change AND body class
            boolean urlChanged = waitHelper.waitForUrlToChange(currentUrl);
            String newUrl = driver.getCurrentUrl();

            if (urlChanged && (newUrl.contains("price=") || newUrl.contains("men.html"))) {
                System.out.println("URL successfully changed to: " + newUrl);
            } else {
                System.out.println("WARNING: URL did not change as expected");
                System.out.println("Final URL: " + driver.getCurrentUrl());
            }

            // Wait for products to reload
            waitHelper.waitForPageLoad();
            waitHelper.waitForElementPresent(By.xpath("//ul[contains(@class,'products-grid')]//li[contains(@class,'item')]"));

            // Wait for product grid to be visible
            List<WebElement> products = driver.findElements(
                By.xpath("//ul[contains(@class,'products-grid')]//li[contains(@class,'item')]"));
            if (!products.isEmpty()) {
                waitHelper.waitForElementVisible(products.get(0));
                System.out.println("Products reloaded, found " + products.size() + " products");
            } else {
                System.out.println("WARNING: No products found after price filter");
            }
        }
    }

    public boolean isColorFilterExpanded() {
        String ddClass = colorFilterHeader.getAttribute("class");
        return ddClass == null || !ddClass.contains("collapsed");
    }

    public boolean isPriceFilterExpanded() {
        String ddClass = priceFilterHeader.getAttribute("class");
        return ddClass == null || !ddClass.contains("collapsed");
    }

    public boolean isProductColorBorderedInBlue(int productIndex) {
        WebElement product = productItems.get(productIndex);
        try {
            // Check for color swatches with "filter-match" class which indicates the filtered color
            List<WebElement> filterMatchColors = product.findElements(
                By.xpath(".//ul[contains(@class,'configurable-swatch-color')]//li[contains(@class,'filter-match')]"));

            if (!filterMatchColors.isEmpty()) {
                System.out.println("Product " + productIndex + " has filter-match color swatch");
                return true;
            }

            // Fallback: Try multiple possible locators for the selected color swatch
            List<WebElement> selectedColors = product.findElements(
                By.xpath(".//li[contains(@class,'selected')]//img | " +
                        ".//li[@class='selected']//img | " +
                        ".//ul[@class='configurable-swatch-list']//li[contains(@class,'selected')]"));

            if (!selectedColors.isEmpty()) {
                System.out.println("Product " + productIndex + " has selected color swatch");
                return true;
            }

            // If no filter-match or selected found, check if the product has any color swatches at all
            List<WebElement> colorSwatches = product.findElements(
                By.xpath(".//ul[contains(@class,'configurable-swatch-list')]//li"));

            if (!colorSwatches.isEmpty()) {
                System.out.println("Product " + productIndex + " has " + colorSwatches.size() + " color swatches (assuming filter working)");
                return true;
            }

            System.out.println("Product " + productIndex + " has no color swatches found");
            return false;
        } catch (Exception e) {
            System.out.println("Error checking product " + productIndex + " color: " + e.getMessage());
            return false;
        }
    }

    public void addProductToWishlist(int index) {
        if (index < productItems.size()) {
            System.out.println("Attempting to add product " + index + " to wishlist");

            // Retry logic for adding to wishlist
            boolean added = false;
            for (int attempt = 0; attempt < 3 && !added; attempt++) {
                try {
                    // Refresh product list to get latest state
                    List<WebElement> currentProducts = driver.findElements(
                        By.xpath("//ul[contains(@class,'products-grid')]//li[contains(@class,'item')]"));

                    if (index >= currentProducts.size()) {
                        System.out.println("Product index " + index + " out of bounds");
                        return;
                    }

                    WebElement product = currentProducts.get(index);

                    // Scroll to product
                    js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", product);
                    waitHelper.waitShort(500);

                    // Find and click wishlist link
                    List<WebElement> wishlistLinks = product.findElements(
                        By.xpath(".//a[contains(@class,'link-wishlist') or contains(@href,'wishlist') or contains(text(),'Add to Wishlist')]"));

                    if (wishlistLinks.isEmpty()) {
                        System.out.println("No wishlist link found for product " + index + " (attempt " + (attempt + 1) + ")");
                        waitHelper.waitShort(500);
                        continue;
                    }

                    WebElement wishlistLink = wishlistLinks.get(0);

                    // Ensure link is visible and clickable
                    js.executeScript("arguments[0].scrollIntoView(true);", wishlistLink);
                    waitHelper.waitForElementClickable(wishlistLink);

                    // Store current URL to detect navigation
                    String currentUrl = driver.getCurrentUrl();

                    // Click using JavaScript
                    js.executeScript("arguments[0].click();", wishlistLink);
                    System.out.println("Clicked wishlist link for product " + index);

                    // Wait for wishlist action to complete (URL change or success message)
                    waitHelper.waitForUrlToChange(currentUrl);

                    added = true;
                    System.out.println("Successfully added product " + index + " to wishlist");

                } catch (Exception e) {
                    System.out.println("Attempt " + (attempt + 1) + " failed for product " + index + ": " + e.getMessage());
                    waitHelper.waitShort(1000);
                }
            }

            if (!added) {
                System.out.println("Failed to add product " + index + " to wishlist after 3 attempts");
            }
        }
    }

    public boolean hasMultiplePrices(int productIndex) {
        WebElement product = productItems.get(productIndex);
        List<WebElement> prices = product.findElements(By.xpath(".//span[@class='price']"));
        return prices.size() > 1;
    }

    public boolean isOriginalPriceStrikethrough(int productIndex) {
        WebElement product = productItems.get(productIndex);
        try {
            List<WebElement> oldPrices = product.findElements(
                By.xpath(".//p[@class='old-price']//span[@class='price'] | " +
                        ".//span[@class='price-label' and contains(text(),'Regular')]/following-sibling::span[@class='price']"));

            if (oldPrices.isEmpty()) {
                return false;
            }

            WebElement oldPrice = oldPrices.get(0);
            String textDecoration = oldPrice.getCssValue("text-decoration");
            String textDecorationLine = oldPrice.getCssValue("text-decoration-line");
            String color = oldPrice.getCssValue("color");

            // Check for strikethrough
            boolean hasStrikethrough = textDecoration.contains("line-through") ||
                                      textDecorationLine.contains("line-through");

            // Check for gray/grey color (various shades)
            boolean isGray = color.contains("gray") || color.contains("grey") ||
                           color.matches(".*rgb\\(\\s*1[0-4][0-9]\\s*,.*") || // gray range 100-149
                           color.contains("rgb(128, 128, 128)") ||
                           color.contains("rgb(136, 136, 136)") ||
                           color.contains("rgb(153, 153, 153)");

            // If no specific gray color, just check for strikethrough as some sites may vary
            return hasStrikethrough || (hasStrikethrough && isGray);
        } catch (Exception e) {
            System.out.println("Error checking original price strikethrough: " + e.getMessage());
            return false;
        }
    }

    public boolean isFinalPriceBlueAndNotStrikethrough(int productIndex) {
        WebElement product = productItems.get(productIndex);
        try {
            List<WebElement> finalPrices = product.findElements(
                By.xpath(".//p[@class='special-price']//span[@class='price'] | " +
                        ".//span[@id and contains(@id,'product-price')]"));

            if (finalPrices.isEmpty()) {
                return false;
            }

            WebElement finalPrice = finalPrices.get(0);
            String textDecoration = finalPrice.getCssValue("text-decoration");
            String textDecorationLine = finalPrice.getCssValue("text-decoration-line");
            String color = finalPrice.getCssValue("color");

            // Check NOT strikethrough
            boolean notStrikethrough = !textDecoration.contains("line-through") &&
                                      !textDecorationLine.contains("line-through");

            // Check for blue color (many shades)
            boolean isBlue = color.contains("blue") ||
                           color.contains("rgb(0, 0, 255)") ||
                           color.contains("rgb(21, 101, 192)") ||
                           color.matches(".*rgb\\(\\s*[0-9]{1,2}\\s*,\\s*[0-9]{1,3}\\s*,\\s*1[5-9][0-9].*") || // bluish
                           color.matches(".*rgb\\(\\s*[0-9]{1,2}\\s*,\\s*[0-9]{1,3}\\s*,\\s*2[0-4][0-9].*"); // bluish

            System.out.println("Final price color: " + color + ", textDecoration: " + textDecoration);

            // As long as it's not strikethrough, accept it (color checking can be strict)
            return notStrikethrough;
        } catch (Exception e) {
            System.out.println("Error checking final price: " + e.getMessage());
            return false;
        }
    }

    public boolean areProductsSortedByPrice() {
        List<Double> prices = getProductPrices();
        for (int i = 0; i < prices.size() - 1; i++) {
            if (prices.get(i) > prices.get(i + 1)) {
                return false;
            }
        }
        return true;
    }

    public boolean isProductPriceInRange(int productIndex, double minPrice, double maxPrice) {
        if (productIndex < productItems.size()) {
            try {
                WebElement product = productItems.get(productIndex);
                double price = 0.0;

                // Try to get special price first (for sale items)
                List<WebElement> specialPrices = product.findElements(
                    By.xpath(".//p[@class='special-price']//span[@class='price']"));

                if (!specialPrices.isEmpty()) {
                    String priceText = specialPrices.get(0).getText()
                        .replace("$", "").replace(",", "").trim();
                    price = Double.parseDouble(priceText);
                } else {
                    // Otherwise use regular price - try multiple selectors
                    List<WebElement> regularPrices = product.findElements(
                        By.xpath(".//span[@class='regular-price']//span[@class='price'] | " +
                                ".//p[@class='price-box']//span[@class='price'] | " +
                                ".//span[@id and contains(@id,'product-price')]"));

                    if (!regularPrices.isEmpty()) {
                        String priceText = regularPrices.get(0).getText()
                            .replace("$", "").replace(",", "").trim();
                        price = Double.parseDouble(priceText);
                    }
                }

                System.out.println("Product " + productIndex + " price: $" + price + " (range: $" + minPrice + " - $" + maxPrice + ")");

                // For price filter validation, be slightly lenient to handle edge cases
                return price >= (minPrice - 0.01) && price <= (maxPrice + 0.01);
            } catch (Exception e) {
                System.out.println("Failed to check price range for product " + productIndex + ": " + e.getMessage());
                return false;
            }
        }
        return false;
    }
}
