package pages;

import org.openqa.selenium.By;
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

    @FindBy(xpath = "//table[@id='shopping-cart-table']//tbody//tr")
    private List<WebElement> cartItems;

    @FindBy(xpath = "//button[@title='Update']")
    private WebElement updateButton;

    @FindBy(xpath = "//td[@class='a-right']//span[@class='price']")
    private List<WebElement> itemPrices;

    @FindBy(xpath = "//tr[@class='last']//span[@class='price']")
    private WebElement grandTotalPrice;

    @FindBy(xpath = "//p[@class='empty']")
    private WebElement emptyCartMessage;

    public ShoppingCartPage(WebDriver driver) {
        super(driver);
    }

    public boolean isShoppingCartPageLoaded() {
        return waitHelper.isElementDisplayed(pageHeading);
    }

    public int getCartItemCount() {
        return cartItems.size();
    }

    public void updateQuantity(int itemIndex, int quantity) {
        if (itemIndex < cartItems.size()) {
            WebElement item = cartItems.get(itemIndex);
            WebElement qtyInput = item.findElement(By.xpath(".//input[@title='Qty']"));
            waitHelper.waitForElementVisible(qtyInput);
            qtyInput.clear();
            qtyInput.sendKeys(String.valueOf(quantity));
        }
    }

    public void clickUpdateButton() {
        waitHelper.waitForElementClickable(updateButton);
        updateButton.click();
    }

    public void deleteItem(int itemIndex) {
        if (itemIndex < cartItems.size()) {
            WebElement item = cartItems.get(itemIndex);
            WebElement deleteBtn = item.findElement(By.xpath(".//a[@title='Remove item']"));
            waitHelper.waitForElementClickable(deleteBtn);
            deleteBtn.click();
        }
    }

    public double getItemSubtotal(int itemIndex) {
        if (itemIndex < cartItems.size()) {
            WebElement item = cartItems.get(itemIndex);
            WebElement subtotal = item.findElement(By.xpath(".//td[@class='a-right']//span[@class='price']"));
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
