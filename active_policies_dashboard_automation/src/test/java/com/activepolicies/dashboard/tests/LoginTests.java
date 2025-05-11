package com.activepolicies.dashboard.tests;

import com.activepolicies.dashboard.pages.DashboardPage;
import com.activepolicies.dashboard.pages.LoginPage;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Tests for login functionality
 */
@Feature("Login")
public class LoginTests extends BaseTest {

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify that user can login with valid credentials")
    @Story("Valid Login")
    public void testValidLogin() {
        LoginPage loginPage = new LoginPage(driver);
        
        // Navigate to login page
        loginPage.navigateToLoginPage();
        
        // Verify login page loaded
        Assert.assertTrue(loginPage.isPageLoaded(), "Login page failed to load");
        
        // Login using admin credentials from config
        String username = config.getProperty("admin.username");
        String password = config.getProperty("admin.password");
        DashboardPage dashboardPage = loginPage.login(username, password);
        
        // Verify dashboard page loaded
        Assert.assertTrue(dashboardPage.isPageLoaded(), "Dashboard page failed to load after login");
        
        // Verify user is logged in correctly
        Assert.assertTrue(dashboardPage.isChartVisible(), "Chart is not visible after login");
    }
    
    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that login fails with invalid credentials")
    @Story("Invalid Login")
    public void testInvalidLogin() {
        LoginPage loginPage = new LoginPage(driver);
        
        // Navigate to login page
        loginPage.navigateToLoginPage();
        
        // Attempt login with invalid credentials
        loginPage.enterUsername("invalid_user")
                 .enterPassword("invalid_password")
                 .clickLogin();
        
        // Verify error message is displayed
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message not displayed for invalid login");
        
        // Verify we're still on the login page
        Assert.assertTrue(loginPage.isPageLoaded(), "Not on login page after failed login attempt");
    }
    
    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that login fails with empty credentials")
    @Story("Empty Credentials")
    public void testEmptyCredentials() {
        LoginPage loginPage = new LoginPage(driver);
        
        // Navigate to login page
        loginPage.navigateToLoginPage();
        
        // Attempt login without entering credentials
        loginPage.clickLogin();
        
        // Verify error message is displayed
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message not displayed for empty credentials");
        
        // Verify we're still on the login page
        Assert.assertTrue(loginPage.isPageLoaded(), "Not on login page after failed login attempt");
    }
    
    @Test
    @Severity(SeverityLevel.MINOR)
    @Description("Verify that remember me functionality works")
    @Story("Remember Me")
    public void testRememberMe() {
        LoginPage loginPage = new LoginPage(driver);
        
        // Navigate to login page
        loginPage.navigateToLoginPage();
        
        // Login with remember me checked
        String username = config.getProperty("admin.username");
        String password = config.getProperty("admin.password");
        loginPage.enterUsername(username)
                 .enterPassword(password)
                 .setRememberMe(true)
                 .clickLogin();
        
        // Verify dashboard loaded
        DashboardPage dashboardPage = new DashboardPage(driver);
        Assert.assertTrue(dashboardPage.isPageLoaded(), "Dashboard page failed to load after login");
        
        // TODO: Add verification that user session is remembered (would require browser restart)
    }
}
