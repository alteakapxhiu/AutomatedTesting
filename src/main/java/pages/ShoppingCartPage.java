package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

/**
 * ShoppingCartPage - Tealium E-commerce Shopping Cart Page
 */
public class ShoppingCartPage extends BasePage {

    @FindBy(xpath = "//h1[contains(text(),'Shopping Cart')]")
    private WebElement pageHeading;

    @FindBy(xpath = "//table[@id='shopping-cart-table']//tbody/tr")
    private List<WebElement> cartItems;

    @FindBy(xpath = "//button[@title='Update' or @title='Update Shopping Cart' or contains(@class,'btn-update')]")
    private WebElement updateButton;

    @FindBy(xpath = "//td[contains(@class,'a-right') or contains(@class,'subtotal')]//span[@class='price']")
    private List<WebElement> itemPrices;

    @FindBy(xpath = "//tfoot//tr[contains(@class,'last') or contains(@class,'grand-total')]//span[@class='price']")
    private WebElement grandTotalPrice;

    @FindBy(xpath = "//p[@class='empty'] | //div[@class='cart-empty']/p")
    private WebElement emptyCartMessage;

    public ShoppingCartPage(WebDriver driver) {
        super(driver);
    }

    public boolean isShoppingCartPageLoaded() {
        try {
            // Wait for cart page to fully load
            waitHelper.waitForPageLoad();

            String currentUrl = driver.getCurrentUrl();
            System.out.println("Checking if shopping cart page loaded. Current URL: " + currentUrl);

            // Check if we're on cart page by URL
            boolean isOnCartPage = currentUrl.contains("checkout/cart") ||
                                  currentUrl.contains("shopping_cart") ||
                                  currentUrl.contains("/cart");

            if (isOnCartPage) {
                System.out.println("URL indicates we're on cart page");
                return true;
            }

            // Also try to find heading
            try {
                List<WebElement> headings = driver.findElements(
                    By.xpath("//h1[contains(text(),'Shopping Cart') or contains(text(),'shopping cart') or contains(text(),'Cart')]"));
                if (!headings.isEmpty()) {
                    System.out.println("Found cart heading: " + headings.get(0).getText());
                    return true;
                }
            } catch (Exception e) {
                System.out.println("Could not find cart heading: " + e.getMessage());
            }

            // Check for cart table or empty cart message
            try {
                List<WebElement> cartElements = driver.findElements(
                    By.xpath("//table[@id='shopping-cart-table'] | //p[@class='empty'] | //div[@class='cart-empty']"));
                if (!cartElements.isEmpty()) {
                    System.out.println("Found cart elements (table or empty message)");
                    return true;
                }
            } catch (Exception e) {
                System.out.println("Could not find cart elements: " + e.getMessage());
            }

            // Final check: page heading element
            boolean headingDisplayed = waitHelper.isElementDisplayed(pageHeading);
            System.out.println("Page heading displayed: " + headingDisplayed);

            return headingDisplayed;
        } catch (Exception e) {
            System.out.println("Error checking shopping cart page: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public int getCartItemCount() {
        try {
            // Check if cart is empty first
            if (waitHelper.isElementDisplayed(emptyCartMessage)) {
                return 0;
            }
            return cartItems.size();
        } catch (Exception e) {
            return 0;
        }
    }

    public void updateQuantity(int itemIndex, int quantity) {
        if (itemIndex < cartItems.size()) {
            WebElement item = cartItems.get(itemIndex);
            WebElement qtyInput = item.findElement(By.xpath(".//input[@title='Qty' or contains(@name,'qty')]"));
            waitHelper.waitForElementVisible(qtyInput);
            scrollToElement(qtyInput);
            qtyInput.clear();
            qtyInput.sendKeys(String.valueOf(quantity));
        }
    }

    public void clickUpdateButton() {
        waitHelper.waitForElementClickable(updateButton);
        scrollToElement(updateButton);
        try {
            updateButton.click();
        } catch (Exception e) {
            js.executeScript("arguments[0].click();", updateButton);
        }

        // Wait for update to process
        waitHelper.waitForPageLoad();
    }

    public void deleteItem(int itemIndex) {
        try {
            // Use JavaScript to find and click the first remove button in the cart table
            JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;

            // Wait for cart table to be present
            waitHelper.waitForElementPresent(By.id("shopping-cart-table"));

            // Find and click the first remove button using JavaScript
            boolean clicked = (boolean) jsExecutor.executeScript(
                "var removeBtn = document.querySelector('#shopping-cart-table .btn-remove, #shopping-cart-table a[title*=\"Remove\"]');" +
                "if (removeBtn) {" +
                "  var href = removeBtn.getAttribute('href');" +
                "  if (href && href !== '#') {" +
                "    window.location.href = href;" +
                "    return true;" +
                "  }" +
                "  removeBtn.click();" +
                "  return true;" +
                "}" +
                "return false;"
            );

            if (clicked) {
                System.out.println("Delete button clicked successfully via JavaScript");
                waitHelper.waitForPageLoad(); // Wait for page reload after deletion
            } else {
                System.out.println("Could not find remove button via JavaScript");
            }
        } catch (Exception e) {
            System.out.println("Error deleting item: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public double getItemSubtotal(int itemIndex) {
        if (itemIndex < cartItems.size()) {
            WebElement item = cartItems.get(itemIndex);
            WebElement subtotal = item.findElement(By.xpath(".//td[@class='product-cart-price']//span[@class='price']"));
            String priceText = subtotal.getText().replace("$", "").replace(",", "").trim();
            return Double.parseDouble(priceText);
        }
        return 0.0;
    }

    public double calculateTotalFromItems() {
        double total = 0.0;
        for (int i = 0; i < cartItems.size(); i++) {
            total += getItemSubtotal(i);
        }
        return total;
    }

    public double getGrandTotal() {
        waitHelper.waitForElementVisible(grandTotalPrice);
        String priceText = grandTotalPrice.getText().replace("$", "").replace(",", "").trim();
        return Double.parseDouble(priceText);
    }

    public boolean verifyGrandTotalMatchesItemsSum() {
        double calculatedTotal = calculateTotalFromItems();
        double displayedTotal = getGrandTotal();
        return Math.abs(calculatedTotal - displayedTotal) < 0.01;
    }

    public boolean isCartEmpty() {
        return waitHelper.isElementDisplayed(emptyCartMessage);
    }

    public String getEmptyCartMessage() {
        waitHelper.waitForElementVisible(emptyCartMessage);
        return emptyCartMessage.getText();
    }

    public void deleteAllItems() {
        while (getCartItemCount() > 0) {
            deleteItem(0);
        }
    }
}
