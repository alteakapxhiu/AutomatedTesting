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

    @FindBy(xpath = "//h1[contains(text(),'My Wishlist') or contains(text(),'Wishlist')]")
    private WebElement pageHeading;

    @FindBy(xpath = "//table[@id='wishlist-table']//tbody/tr | //form[@id='wishlist-view-form']//li[contains(@class,'item')]")
    private List<WebElement> wishlistItems;

    @FindBy(xpath = "//button[@title='Add to Cart' or contains(@class,'btn-cart')]")
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

            try {
                // Try to select size if available
                List<WebElement> sizeDropdowns = item.findElements(By.xpath(".//select[@title='Size' or contains(@class,'super-attribute-select')]"));
                if (!sizeDropdowns.isEmpty()) {
                    WebElement sizeDropdown = sizeDropdowns.get(0);
                    waitHelper.waitForElementVisible(sizeDropdown);
                    Select sizeSelect = new Select(sizeDropdown);
                    try {
                        sizeSelect.selectByVisibleText(size);
                    } catch (Exception e) {
                        // If exact text doesn't match, try selecting first option that's not "Choose an Option"
                        for (int i = 0; i < sizeSelect.getOptions().size(); i++) {
                            if (!sizeSelect.getOptions().get(i).getText().contains("Choose")) {
                                sizeSelect.selectByIndex(i);
                                break;
                            }
                        }
                    }
                }

                // Try to select color if available
                List<WebElement> colorDropdowns = item.findElements(By.xpath(".//select[@title='Color' or contains(@class,'super-attribute-select')]"));
                if (!colorDropdowns.isEmpty()) {
                    WebElement colorDropdown = colorDropdowns.get(0);
                    waitHelper.waitForElementVisible(colorDropdown);
                    Select colorSelect = new Select(colorDropdown);
                    try {
                        colorSelect.selectByVisibleText(color);
                    } catch (Exception e) {
                        // If exact text doesn't match, try selecting first option that's not "Choose an Option"
                        for (int i = 0; i < colorSelect.getOptions().size(); i++) {
                            if (!colorSelect.getOptions().get(i).getText().contains("Choose")) {
                                colorSelect.selectByIndex(i);
                                break;
                            }
                        }
                    }
                }

                // Wait a moment for selections to register
                Thread.sleep(500);

                // Click add to cart
                WebElement addToCartBtn = item.findElement(By.xpath(".//button[@title='Add to Cart' or contains(@class,'btn-cart') or contains(text(),'Add to Cart')]"));
                waitHelper.waitForElementClickable(addToCartBtn);
                scrollToElement(addToCartBtn);
                try {
                    addToCartBtn.click();
                } catch (Exception e) {
                    js.executeScript("arguments[0].click();", addToCartBtn);
                }

                // Wait for add to cart to complete
                Thread.sleep(2000);
            } catch (Exception e) {
                System.out.println("Error adding item to cart: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public void addAllItemsToCart(String size, String color) {
        for (int i = 0; i < wishlistItems.size(); i++) {
            addItemToCart(i, size, color);
        }
    }
}
