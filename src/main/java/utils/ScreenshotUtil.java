package utils;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ScreenshotUtil - Captures screenshots (REQUIREMENT: Configure Screenshot Capture on failure)
 *
 * Purpose: Take screenshots when tests fail for debugging
 * - Automatically called by TestListener when test fails
 * - Screenshots saved in "screenshots/" folder
 * - Filenames include test name and timestamp for easy identification
 */
public class ScreenshotUtil {

    /**
     * Captures screenshot of current browser window
     *
     * @param driver - WebDriver instance
     * @param screenshotName - Name for the screenshot (usually test name)
     * @return Path to saved screenshot file
     */
    public static String captureScreenshot(WebDriver driver, String screenshotName) {
        // Add timestamp to make filename unique
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = screenshotName + "_" + timestamp + ".png";
        String screenshotPath = "screenshots/" + fileName;

        try {
            // Create screenshots folder if it doesn't exist
            File screenshotDir = new File("screenshots");
            if (!screenshotDir.exists()) {
                screenshotDir.mkdirs();
            }

            // Take screenshot and save to file
            TakesScreenshot ts = (TakesScreenshot) driver;
            File source = ts.getScreenshotAs(OutputType.FILE);  // Capture screenshot
            File destination = new File(screenshotPath);
            FileUtils.copyFile(source, destination);  // Save to disk

            System.out.println("Screenshot captured: " + screenshotPath);
            return screenshotPath;
        } catch (IOException e) {
            System.out.println("Failed to capture screenshot: " + e.getMessage());
            return null;
        }
    }
}
