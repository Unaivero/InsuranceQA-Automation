package com.activepolicies.dashboard.tests;

import com.activepolicies.dashboard.constants.PolicyTypes;
import com.activepolicies.dashboard.pages.DashboardPage;
import com.activepolicies.dashboard.pages.LoginPage;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

/**
 * Tests for dashboard filtering functionality
 */
@Feature("Dashboard Filtering")
public class FilterTests extends BaseTest {

    private DashboardPage dashboardPage;
    
    @BeforeMethod
    public void navigateToDashboard() {
        // Login and navigate to dashboard
        LoginPage loginPage = new LoginPage(driver);
        loginPage.navigateToLoginPage();
        dashboardPage = loginPage.loginAsAdmin();
        
        // Verify dashboard page is loaded
        Assert.assertTrue(dashboardPage.isPageLoaded(), "Dashboard page failed to load");
    }
    
    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify filtering by policy type")
    @Story("Policy Type Filter")
    public void testFilterByPolicyType() {
        // Reset filters to ensure clean state
        dashboardPage.resetFilters();
        
        // Get initial count
        int initialCount = dashboardPage.getDisplayedPolicyCount();
        logger.info("Initial policy count: " + initialCount);
        
        // Filter by Auto policy type
        dashboardPage.selectPolicyTypeFilter(PolicyTypes.AUTO)
                     .clickApplyFilters();
        
        // Verify filter applied correctly
        int filteredCount = dashboardPage.getDisplayedPolicyCount();
        logger.info("Filtered policy count (Auto): " + filteredCount);
        
        // Verify count is less than initial (or equal if all policies are Auto)
        Assert.assertTrue(filteredCount <= initialCount, 
            "Filtered count should be less than or equal to initial count");
        
        // Verify only Auto policies are displayed
        List<WebElement> policyRows = dashboardPage.getPolicyTableRows();
        for (WebElement row : policyRows) {
            String policyType = row.findElement(By.cssSelector("td:nth-child(2)")).getText();
            Assert.assertEquals(policyType, PolicyTypes.AUTO, 
                "Non-Auto policy found after filtering: " + policyType);
        }
    }
    
    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify filtering by date range")
    @Story("Date Range Filter")
    public void testFilterByDateRange() {
        // Reset filters
        dashboardPage.resetFilters();
        
        // Get initial count
        int initialCount = dashboardPage.getDisplayedPolicyCount();
        
        // Filter by date range
        dashboardPage.enterDateRange("2024-01-01", "2024-02-01")
                     .clickApplyFilters();
        
        // Verify filter applied
        int filteredCount = dashboardPage.getDisplayedPolicyCount();
        
        // Verify count is less than or equal to initial
        Assert.assertTrue(filteredCount <= initialCount, 
            "Filtered count should be less than or equal to initial count");
    }
    
    @DataProvider(name = "policyTypeProvider")
    public Object[][] providePolicyTypes() {
        return new Object[][] {
            {PolicyTypes.AUTO},
            {PolicyTypes.HOME},
            {PolicyTypes.LIFE},
            {PolicyTypes.HEALTH},
            {PolicyTypes.COMMERCIAL},
            {PolicyTypes.TRAVEL},
            {PolicyTypes.PET}
        };
    }
    
    @Test(dataProvider = "policyTypeProvider")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify all policy type filters")
    @Story("All Policy Type Filters")
    public void testAllPolicyTypeFilters(String policyType) {
        // Reset filters
        dashboardPage.resetFilters();
        
        // Apply filter for current policy type
        dashboardPage.selectPolicyTypeFilter(policyType)
                     .clickApplyFilters();
        
        // Verify filter applied
        int filteredCount = dashboardPage.getDisplayedPolicyCount();
        logger.info("Filtered policy count (" + policyType + "): " + filteredCount);
        
        // Verify policy rows match filter
        if (filteredCount > 0) {
            List<WebElement> policyRows = dashboardPage.getPolicyTableRows();
            for (WebElement row : policyRows) {
                String rowPolicyType = row.findElement(By.cssSelector("td:nth-child(2)")).getText();
                Assert.assertEquals(rowPolicyType, policyType, 
                    "Policy type mismatch after filtering for " + policyType);
            }
        }
    }
    
    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify reset filters functionality")
    @Story("Reset Filters")
    public void testResetFilters() {
        // Get initial count
        int initialCount = dashboardPage.getDisplayedPolicyCount();
        
        // Apply filter
        dashboardPage.selectPolicyTypeFilter(PolicyTypes.AUTO)
                     .clickApplyFilters();
        
        // Verify filter applied
        int filteredCount = dashboardPage.getDisplayedPolicyCount();
        Assert.assertTrue(filteredCount <= initialCount, 
            "Filtered count should be less than or equal to initial count");
        
        // Reset filters
        dashboardPage.resetFilters();
        
        // Verify count returned to initial
        int resetCount = dashboardPage.getDisplayedPolicyCount();
        Assert.assertEquals(resetCount, initialCount, 
            "Count after reset should match initial count");
    }
}
