package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * ConfigReader - Reads configuration from config.properties file
 *
 * Purpose: Centralized configuration management
 * - Loads properties file once when class is loaded (static block)
 * - Provides easy access to configuration values (browser, URLs, timeouts)
 * - Makes it easy to change settings without modifying code
 */
public class ConfigReader {
    private static Properties properties;
    private static final String CONFIG_FILE_PATH = "src/test/resources/config.properties";

    // Static block runs when class is first loaded - loads config file once
    static {
        try {
            FileInputStream fis = new FileInputStream(CONFIG_FILE_PATH);
            properties = new Properties();
            properties.load(fis);  // Load all properties from file
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load config.properties file");
        }
    }

    /**
     * Get any property value by its key
     * Example: getProperty("browser") returns "chrome"
     */
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    /**
     * Convenience method to get the base URL
     * Returns the URL of YelpCamp application (http://localhost:5173)
     */
    public static String getBaseUrl() {
        return properties.getProperty("base.url");
    }
}
