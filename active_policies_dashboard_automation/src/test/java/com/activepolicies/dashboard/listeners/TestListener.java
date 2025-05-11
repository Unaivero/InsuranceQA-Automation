package com.activepolicies.dashboard.listeners;

import com.activepolicies.dashboard.utils.ConfigurationManager;
import com.activepolicies.dashboard.utils.ScreenshotUtils;
import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * TestNG Listener for test execution events
 */
public class TestListener implements ITestListener {
    private static final Logger logger = Logger.getLogger(TestListener.class.getName());
    private final ConfigurationManager config = ConfigurationManager.getInstance();

    @Override
    public void onTestStart(ITestResult result) {
        logger.info("Starting test: " + result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        logger.info("Test passed: " + result.getName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        logger.log(Level.SEVERE, "Test failed: " + result.getName(), result.getThrowable());
        
        // Get WebDriver instance from test class
        Object testInstance = result.getInstance();
        if (testInstance instanceof BaseTestWithDriver) {
            WebDriver driver = ((BaseTestWithDriver) testInstance).getDriver();
            if (driver != null) {
                captureScreenshot(driver, result.getName());
                saveScreenshotToAllure(driver);
            }
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        logger.warning("Test skipped: " + result.getName());
    }

    @Override
    public void onStart(ITestContext context) {
        logger.info("Starting test suite: " + context.getName());
        
        // Create screenshot directory if it doesn't exist
        String screenshotDir = config.getProperty("screenshot.dir");
        try {
            Files.createDirectories(Paths.get(screenshotDir));
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to create screenshot directory", e);
        }
    }

    @Override
    public void onFinish(ITestContext context) {
        logger.info("Finished test suite: " + context.getName());
        logger.info("Passed tests: " + context.getPassedTests().size());
        logger.info("Failed tests: " + context.getFailedTests().size());
        logger.info("Skipped tests: " + context.getSkippedTests().size());
    }
    
    /**
     * Interface for test classes that have a WebDriver
     */
    public interface BaseTestWithDriver {
        WebDriver getDriver();
    }
    
    /**
     * Capture screenshot and save to file
     */
    private void captureScreenshot(WebDriver driver, String testName) {
        try {
            String screenshotDir = config.getProperty("screenshot.dir");
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String filename = String.format("%s/%s_%s.%s", 
                              screenshotDir, testName, timestamp, 
                              config.getProperty("screenshot.format", "png"));
            
            ScreenshotUtils.captureScreenshot(driver, filename);
            logger.info("Screenshot saved: " + filename);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to capture screenshot", e);
        }
    }
    
    /**
     * Attach screenshot to Allure report
     */
    @Attachment(value = "Screenshot", type = "image/png")
    private byte[] saveScreenshotToAllure(WebDriver driver) {
        try {
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to capture screenshot for Allure", e);
            return new byte[0];
        }
    }
}
