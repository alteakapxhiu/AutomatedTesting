package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

/**
 * WishlistPage - Tealium E-commerce Wishlist Page
 */
public class WishlistPage extends BasePage {

    @FindBy(xpath = "//h1[contains(text(),'My Wishlist')]")
    private WebElement pageHeading;

    @FindBy(xpath = "//li[contains(@class,'item')]")
    private List<WebElement> wishlistItems;

    @FindBy(xpath = "//button[@title='Add to Cart']")
    private List<WebElement> addToCartButtons;

    public WishlistPage(WebDriver driver) {
        super(driver);
    }

    public boolean isWishlistPageLoaded() {
        return waitHelper.isElementDisplayed(pageHeading);
    }

    public int getWishlistItemCount() {
        return wishlistItems.size();
    }

    public void addItemToCart(int index, String size, String color) {
        if (index < wishlistItems.size()) {
            WebElement item = wishlistItems.get(index);

            // Select size
            WebElement sizeDropdown = item.findElement(By.xpath(".//select[@title='Size']"));
            waitHelper.waitForElementVisible(sizeDropdown);
            Select sizeSelect = new Select(sizeDropdown);
            sizeSelect.selectByVisibleText(size);

            // Select color
            WebElement colorDropdown = item.findElement(By.xpath(".//select[@title='Color']"));
            waitHelper.waitForElementVisible(colorDropdown);
            Select colorSelect = new Select(colorDropdown);
            colorSelect.selectByVisibleText(color);

            // Click add to cart
            WebElement addToCartBtn = item.findElement(By.xpath(".//button[@title='Add to Cart']"));
            waitHelper.waitForElementClickable(addToCartBtn);
            addToCartBtn.click();
        }
    }

    public void addAllItemsToCart(String size, String color) {
        for (int i = 0; i < wishlistItems.size(); i++) {
            addItemToCart(i, size, color);
        }
    }
}
