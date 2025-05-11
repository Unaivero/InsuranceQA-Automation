package com.activepolicies.dashboard.tests;

import com.activepolicies.dashboard.listeners.TestListener.BaseTestWithDriver;
import com.activepolicies.dashboard.utils.ConfigurationManager;
import com.activepolicies.dashboard.utils.DriverFactory;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.logging.Logger;

/**
 * Base test class with common setup and teardown operations
 */
public class BaseTest implements BaseTestWithDriver {
    protected WebDriver driver;
    protected ConfigurationManager config;
    protected Logger logger;
    
    protected BaseTest() {
        config = ConfigurationManager.getInstance();
        logger = Logger.getLogger(getClass().getName());
    }
    
    @BeforeSuite
    public void beforeSuite() {
        try {
            // Create directories for test artifacts
            Files.createDirectories(Paths.get(config.getProperty("screenshot.dir")));
            Files.createDirectories(Paths.get(config.getProperty("download.dir")));
        } catch (Exception e) {
            logger.severe("Failed to create test directories: " + e.getMessage());
        }
    }
    
    @BeforeMethod
    @Parameters({"browser", "headless"})
    @Step("Setting up test environment")
    public void setUp(@Optional("") String browser, @Optional("") String headless) {
        String browserType = browser.isEmpty() 
            ? config.getProperty("browser", "chrome") 
            : browser;
        
        boolean isHeadless = !headless.isEmpty() 
            ? Boolean.parseBoolean(headless) 
            : config.getBooleanProperty("headless");
            
        logger.info("Setting up WebDriver: " + browserType + " (headless: " + isHeadless + ")");
        
        // Initialize WebDriver based on browser type
        switch (browserType.toLowerCase()) {
            case "chrome":
                driver = DriverFactory.createChromeDriver(isHeadless);
                break;
            // Add more browsers as needed
            default:
                throw new RuntimeException("Unsupported browser: " + browserType);
        }
        
        // Configure WebDriver
        int implicitWaitSeconds = config.getIntProperty("implicit.wait.seconds");
        int pageLoadTimeoutSeconds = config.getIntProperty("page.load.timeout.seconds");
        
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWaitSeconds));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(pageLoadTimeoutSeconds));
        driver.manage().window().maximize();
    }
    
    @AfterMethod
    @Step("Tearing down test environment")
    public void tearDown() {
        if (driver != null) {
            logger.info("Closing WebDriver");
            driver.quit();
        }
    }
    
    /**
     * Get the WebDriver instance (used by TestListener)
     */
    @Override
    public WebDriver getDriver() {
        return driver;
    }
}
