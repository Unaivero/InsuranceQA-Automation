package com.activepolicies.dashboard.pages;

import com.activepolicies.dashboard.utils.ConfigurationManager;
import com.activepolicies.dashboard.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.logging.Logger;

/**
 * Base class for all Page Objects
 */
public abstract class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected WaitUtils waitUtils;
    protected Logger logger = Logger.getLogger(getClass().getName());
    protected ConfigurationManager config = ConfigurationManager.getInstance();
    
    // Common web elements
    protected By loadingSpinner = By.id("loading-spinner");
    protected By pageTitle = By.cssSelector(".page-title");
    protected By navigationMenu = By.id("main-nav");
    
    public BasePage(WebDriver driver) {
        this.driver = driver;
        int explicitWaitSeconds = config.getIntProperty("explicit.wait.seconds");
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(explicitWaitSeconds));
        this.waitUtils = new WaitUtils(driver, explicitWaitSeconds);
        PageFactory.initElements(driver, this);
    }
    
    /**
     * Wait for loading spinner to disappear
     */
    public void waitForLoad() {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingSpinner));
    }
    
    /**
     * Get the page title text
     */
    public String getPageTitle() {
        return waitUtils.waitForVisibility(pageTitle).getText();
    }
    
    /**
     * Check if a specific navigation menu item is active
     */
    public boolean isMenuItemActive(String itemText) {
        By menuItemLocator = By.xpath("//nav[@id='main-nav']//a[contains(text(),'" + itemText + "')]");
        WebElement menuItem = waitUtils.waitForVisibility(menuItemLocator);
        return menuItem.getAttribute("class").contains("active");
    }
    
    /**
     * Navigate to a specific page via navigation menu
     */
    public void navigateToMenu(String menuItem) {
        By menuItemLocator = By.xpath("//nav[@id='main-nav']//a[contains(text(),'" + menuItem + "')]");
        waitUtils.waitForClickabilityAndClick(menuItemLocator);
        waitForLoad();
    }
    
    /**
     * Scroll to element using JavaScript executor
     */
    public void scrollToElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }
    
    /**
     * Verify if page is loaded
     */
    public abstract boolean isPageLoaded();
}
