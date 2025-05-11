package com.activepolicies.dashboard.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Page object for the login page
 */
public class LoginPage extends BasePage {

    private By usernameField = By.id("username");
    private By passwordField = By.id("password");
    private By loginButton = By.id("login-button");
    private By errorMessage = By.id("error-message");
    private By forgotPasswordLink = By.id("forgot-password");
    private By rememberMeCheckbox = By.id("remember-me");
    
    public LoginPage(WebDriver driver) {
        super(driver);
    }
    
    /**
     * Navigate to login page
     */
    public LoginPage navigateToLoginPage() {
        String baseUrl = config.getProperty("base.url");
        driver.get(baseUrl + "/login");
        return this;
    }
    
    /**
     * Enter username
     */
    public LoginPage enterUsername(String username) {
        logger.info("Entering username: " + username);
        WebElement element = waitUtils.waitForVisibility(usernameField);
        element.clear();
        element.sendKeys(username);
        return this;
    }
    
    /**
     * Enter password
     */
    public LoginPage enterPassword(String password) {
        logger.info("Entering password");
        WebElement element = waitUtils.waitForVisibility(passwordField);
        element.clear();
        element.sendKeys(password);
        return this;
    }
    
    /**
     * Click login button
     */
    public DashboardPage clickLogin() {
        logger.info("Clicking login button");
        waitUtils.waitForClickabilityAndClick(loginButton);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingSpinner));
        return new DashboardPage(driver);
    }
    
    /**
     * Login with credentials
     */
    public DashboardPage login(String username, String password) {
        logger.info("Logging in with username: " + username);
        enterUsername(username);
        enterPassword(password);
        return clickLogin();
    }
    
    /**
     * Login with default admin credentials
     */
    public DashboardPage loginAsAdmin() {
        String adminUsername = config.getProperty("admin.username");
        String adminPassword = config.getProperty("admin.password");
        return login(adminUsername, adminPassword);
    }
    
    /**
     * Check if error message is displayed
     */
    public boolean isErrorMessageDisplayed() {
        try {
            return waitUtils.waitForVisibility(errorMessage).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Get error message text
     */
    public String getErrorMessageText() {
        if (isErrorMessageDisplayed()) {
            return driver.findElement(errorMessage).getText();
        }
        return "";
    }
    
    /**
     * Click forgot password link
     */
    public void clickForgotPassword() {
        waitUtils.waitForClickabilityAndClick(forgotPasswordLink);
    }
    
    /**
     * Check/uncheck Remember Me
     */
    public LoginPage setRememberMe(boolean remember) {
        WebElement checkbox = waitUtils.waitForVisibility(rememberMeCheckbox);
        boolean isChecked = checkbox.isSelected();
        if (isChecked != remember) {
            checkbox.click();
        }
        return this;
    }
    
    @Override
    public boolean isPageLoaded() {
        return wait.until(ExpectedConditions.urlContains("/login")) &&
               wait.until(ExpectedConditions.visibilityOfElementLocated(loginButton)).isDisplayed();
    }
}
