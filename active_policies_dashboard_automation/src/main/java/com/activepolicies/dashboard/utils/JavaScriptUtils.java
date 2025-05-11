package com.activepolicies.dashboard.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Utility class for JavaScript operations in Selenium
 */
public class JavaScriptUtils {
    private static final Logger logger = LogManager.getLogger(JavaScriptUtils.class);
    private final WebDriver driver;
    private final JavascriptExecutor jsExecutor;
    
    /**
     * Constructor
     * @param driver WebDriver instance
     */
    public JavaScriptUtils(WebDriver driver) {
        this.driver = driver;
        this.jsExecutor = (JavascriptExecutor) driver;
    }
    
    /**
     * Executes JavaScript and returns result
     * @param script JavaScript to execute
     * @param args Arguments to pass to script
     * @return Result of script execution
     */
    public Object executeScript(String script, Object... args) {
        try {
            return jsExecutor.executeScript(script, args);
        } catch (Exception e) {
            logger.error("Failed to execute script: {}", script, e);
            throw e;
        }
    }
    
    /**
     * Executes asynchronous JavaScript and returns result
     * @param script JavaScript to execute
     * @param args Arguments to pass to script
     * @return Result of script execution
     */
    public Object executeAsyncScript(String script, Object... args) {
        try {
            return jsExecutor.executeAsyncScript(script, args);
        } catch (Exception e) {
            logger.error("Failed to execute async script: {}", script, e);
            throw e;
        }
    }
    
    /**
     * Clicks an element using JavaScript
     * @param element Element to click
     */
    public void click(WebElement element) {
        try {
            executeScript("arguments[0].click();", element);
        } catch (Exception e) {
            logger.error("Failed to click element using JavaScript", e);
            throw e;
        }
    }
    
    /**
     * Scrolls to element
     * @param element Element to scroll to
     */
    public void scrollIntoView(WebElement element) {
        try {
            executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
        } catch (Exception e) {
            logger.error("Failed to scroll to element", e);
            throw e;
        }
    }
    
    /**
     * Scrolls to the top of the page
     */
    public void scrollToTop() {
        try {
            executeScript("window.scrollTo(0, 0);");
        } catch (Exception e) {
            logger.error("Failed to scroll to top", e);
            throw e;
        }
    }
    
    /**
     * Scrolls to the bottom of the page
     */
    public void scrollToBottom() {
        try {
            executeScript("window.scrollTo(0, document.body.scrollHeight);");
        } catch (Exception e) {
            logger.error("Failed to scroll to bottom", e);
            throw e;
        }
    }
    
    /**
     * Highlights an element by changing its background color
     * @param element Element to highlight
     */
    public void highlightElement(WebElement element) {
        try {
            String originalStyle = element.getAttribute("style");
            executeScript("arguments[0].setAttribute('style', arguments[1]);", 
                element, "border: 2px solid red; background-color: yellow;");
            
            // Revert after a short delay
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            executeScript("arguments[0].setAttribute('style', arguments[1]);", 
                element, originalStyle);
        } catch (Exception e) {
            logger.error("Failed to highlight element", e);
            // Don't throw - this is just visual assistance
        }
    }
    
    /**
     * Sets value of an input element
     * @param element Element to set value for
     * @param value Value to set
     */
    public void setValue(WebElement element, String value) {
        try {
            executeScript("arguments[0].value = arguments[1];", element, value);
        } catch (Exception e) {
            logger.error("Failed to set value using JavaScript", e);
            throw e;
        }
    }
    
    /**
     * Checks if page has finished loading
     * @return true if page has loaded, false otherwise
     */
    public boolean isPageLoaded() {
        Object result = executeScript("return document.readyState;");
        return "complete".equals(result);
    }
    
    /**
     * Waits for page to load completely
     * @param timeoutSeconds Maximum time to wait in seconds
     */
    public void waitForPageLoad(int timeoutSeconds) {
        int count = 0;
        while (count < timeoutSeconds) {
            if (isPageLoaded()) {
                return;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
            count++;
        }
        logger.warn("Page did not load within {} seconds", timeoutSeconds);
    }
}
