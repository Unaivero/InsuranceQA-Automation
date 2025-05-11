package com.activepolicies.dashboard.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

import java.time.Duration;

/**
 * Factory class for creating WebDriver instances for different browsers
 */
public class DriverFactory {
    private static final Logger logger = LogManager.getLogger(DriverFactory.class);

    /**
     * Creates a Chrome WebDriver instance
     * 
     * @param headless Whether to run in headless mode
     * @return Chrome WebDriver instance
     */
    public static WebDriver createChromeDriver(boolean headless) {
        logger.info("Setting up Chrome driver. Headless: {}", headless);
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--remote-allow-origins=*");
        
        if (headless) {
            options.addArguments("--headless=new");
            options.addArguments("--window-size=1920,1080");
        }

        return new ChromeDriver(options);
    }
    
    /**
     * Creates a Firefox WebDriver instance
     * 
     * @param headless Whether to run in headless mode
     * @return Firefox WebDriver instance
     */
    public static WebDriver createFirefoxDriver(boolean headless) {
        logger.info("Setting up Firefox driver. Headless: {}", headless);
        WebDriverManager.firefoxdriver().setup();
        
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("--start-maximized");
        
        if (headless) {
            options.addArguments("--headless");
            options.addArguments("--width=1920");
            options.addArguments("--height=1080");
        }
        
        return new FirefoxDriver(options);
    }
    
    /**
     * Creates an Edge WebDriver instance
     * 
     * @param headless Whether to run in headless mode
     * @return Edge WebDriver instance
     */
    public static WebDriver createEdgeDriver(boolean headless) {
        logger.info("Setting up Edge driver. Headless: {}", headless);
        WebDriverManager.edgedriver().setup();
        
        EdgeOptions options = new EdgeOptions();
        options.addArguments("--start-maximized");
        
        if (headless) {
            options.addArguments("--headless=new");
            options.addArguments("--window-size=1920,1080");
        }
        
        return new EdgeDriver(options);
    }
    
    /**
     * Creates a Safari WebDriver instance (headless not supported)
     * 
     * @return Safari WebDriver instance
     */
    public static WebDriver createSafariDriver() {
        logger.info("Setting up Safari driver");
        // Safari doesn't support headless mode
        SafariOptions options = new SafariOptions();
        return new SafariDriver(options);
    }
    
    /**
     * Creates a WebDriver instance based on browser type
     * 
     * @param browserType Browser type (chrome, firefox, edge, safari)
     * @param headless Whether to run in headless mode
     * @return WebDriver instance
     */
    public static WebDriver createDriver(String browserType, boolean headless) {
        WebDriver driver;
        
        switch (browserType.toLowerCase()) {
            case "chrome":
                driver = createChromeDriver(headless);
                break;
            case "firefox":
                driver = createFirefoxDriver(headless);
                break;
            case "edge":
                driver = createEdgeDriver(headless);
                break;
            case "safari":
                driver = createSafariDriver();
                break;
            default:
                logger.warn("Unsupported browser: {}. Defaulting to Chrome", browserType);
                driver = createChromeDriver(headless);
        }
        
        // Configure timeouts
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        driver.manage().window().maximize();
        
        return driver;
    }
}
