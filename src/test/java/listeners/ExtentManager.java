package listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ExtentManager - Manages ExtentReports for HTML test reporting (REQUIREMENT: Configure Report)
 *
 * Purpose: Creates beautiful HTML reports showing test results
 * - Shows which tests passed/failed
 * - Includes screenshots of failures
 * - Displays test execution time and details
 * - Report is saved in "test-output/" folder
 */
public class ExtentManager {
    private static ExtentReports extent;

    /**
     * Returns singleton instance of ExtentReports
     * Creates new instance if it doesn't exist
     */
    public static ExtentReports getInstance() {
        if (extent == null) {
            createInstance();
        }
        return extent;
    }

    /**
     * Creates and configures ExtentReports instance
     * Sets up HTML reporter with custom styling and metadata
     */
    private static ExtentReports createInstance() {
        // Generate unique report filename with timestamp
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String reportPath = "test-output/ExtentReport_" + timestamp + ".html";

        // Configure the HTML reporter
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
        sparkReporter.config().setTheme(Theme.DARK);  // Dark theme looks professional
        sparkReporter.config().setDocumentTitle("YelpCamp Automation Report");
        sparkReporter.config().setReportName("YelpCamp Test Execution Report");
        sparkReporter.config().setTimeStampFormat("EEEE, MMMM dd, yyyy, hh:mm a '('zzz')'");

        // Create ExtentReports and attach the reporter
        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);

        // Add system information to report header
        extent.setSystemInfo("Application", "YelpCamp");
        extent.setSystemInfo("Environment", "Local");
        extent.setSystemInfo("Tester", "Automation Team");

        return extent;
    }

    /**
     * Writes all data to the report file
     * Must be called at end of test suite
     */
    public static void flush() {
        if (extent != null) {
            extent.flush();  // Write report to disk
        }
    }
}
