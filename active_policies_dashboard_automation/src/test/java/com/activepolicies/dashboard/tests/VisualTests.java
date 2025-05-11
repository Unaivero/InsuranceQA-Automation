package com.activepolicies.dashboard.tests;

import com.activepolicies.dashboard.constants.PolicyTypes;
import com.activepolicies.dashboard.pages.DashboardPage;
import com.activepolicies.dashboard.pages.LoginPage;
import com.activepolicies.dashboard.utils.ImageComparator;
import com.activepolicies.dashboard.utils.ScreenshotUtils;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;

/**
 * Visual tests for dashboard elements
 */
@Feature("Visual Testing")
public class VisualTests extends BaseTest {

    private DashboardPage dashboardPage;
    private final String screenshotDir = "target/screenshots/visual";
    private final int visualThreshold = 2; // 2% difference threshold
    
    @BeforeMethod
    public void navigateToDashboard() {
        // Create screenshot directory
        new File(screenshotDir).mkdirs();
        
        // Login and navigate to dashboard
        LoginPage loginPage = new LoginPage(driver);
        loginPage.navigateToLoginPage();
        dashboardPage = loginPage.loginAsAdmin();
        
        // Verify dashboard page is loaded
        Assert.assertTrue(dashboardPage.isPageLoaded(), "Dashboard page failed to load");
    }
    
    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify chart visual appearance")
    @Story("Chart Visual")
    public void testChartVisual() throws Exception {
        // Reset filters
        dashboardPage.resetFilters();
        
        // Verify chart is visible
        WebElement chartElement = dashboardPage.getChartContainer();
        Assert.assertTrue(chartElement.isDisplayed(), "Chart is not displayed");
        
        // Capture screenshot of chart
        File chartScreenshot = new File(screenshotDir + "/current_chart.png");
        dashboardPage.scrollToElement(chartElement);
        ScreenshotUtils.captureScreenshot(driver, chartScreenshot.getPath());
        
        // Get baseline image (in real implementation, this would be a pre-approved baseline)
        File baselineImage = new File("src/test/resources/expected-results/chart_baseline.png");
        
        // If baseline doesn't exist (first run), use current as baseline
        if (!baselineImage.exists()) {
            logger.warning("Baseline image doesn't exist, using current screenshot as baseline");
            chartScreenshot.renameTo(baselineImage);
        } else {
            // Compare screenshots
            File diffImage = new File(screenshotDir + "/chart_diff.png");
            boolean result = ImageComparator.compareImages(baselineImage, chartScreenshot, diffImage, visualThreshold);
            
            // Assert images match within threshold
            Assert.assertTrue(result, "Chart visual appearance has changed beyond threshold");
        }
    }
    
    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify policy table visual appearance")
    @Story("Table Visual")
    public void testTableVisual() throws Exception {
        // Reset filters
        dashboardPage.resetFilters();
        
        // Find table element
        WebElement tableElement = driver.findElement(By.id("policy-data-table"));
        Assert.assertTrue(tableElement.isDisplayed(), "Policy table is not displayed");
        
        // Capture screenshot of table
        File tableScreenshot = new File(screenshotDir + "/current_table.png");
        dashboardPage.scrollToElement(tableElement);
        ScreenshotUtils.captureScreenshot(driver, tableScreenshot.getPath());
        
        // Get baseline image
        File baselineImage = new File("src/test/resources/expected-results/table_baseline.png");
        
        // If baseline doesn't exist, use current as baseline
        if (!baselineImage.exists()) {
            logger.warning("Baseline image doesn't exist, using current screenshot as baseline");
            tableScreenshot.renameTo(baselineImage);
        } else {
            // Compare screenshots
            File diffImage = new File(screenshotDir + "/table_diff.png");
            boolean result = ImageComparator.compareImages(baselineImage, tableScreenshot, diffImage, visualThreshold);
            
            // Assert images match within threshold
            Assert.assertTrue(result, "Table visual appearance has changed beyond threshold");
        }
    }
    
    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify filter panel visual appearance")
    @Story("Filter Panel Visual")
    public void testFilterPanelVisual() throws Exception {
        // Reset filters
        dashboardPage.resetFilters();
        
        // Find filter panel element
        WebElement filterPanel = driver.findElement(By.id("filter-panel"));
        Assert.assertTrue(filterPanel.isDisplayed(), "Filter panel is not displayed");
        
        // Capture screenshot
        File filterScreenshot = new File(screenshotDir + "/current_filter_panel.png");
        dashboardPage.scrollToElement(filterPanel);
        ScreenshotUtils.captureScreenshot(driver, filterScreenshot.getPath());
        
        // Get baseline image
        File baselineImage = new File("src/test/resources/expected-results/filter_panel_baseline.png");
        
        // If baseline doesn't exist, use current as baseline
        if (!baselineImage.exists()) {
            logger.warning("Baseline image doesn't exist, using current screenshot as baseline");
            filterScreenshot.renameTo(baselineImage);
        } else {
            // Compare screenshots
            File diffImage = new File(screenshotDir + "/filter_panel_diff.png");
            boolean result = ImageComparator.compareImages(baselineImage, filterScreenshot, diffImage, visualThreshold);
            
            // Assert images match within threshold
            Assert.assertTrue(result, "Filter panel visual appearance has changed beyond threshold");
        }
    }
    
    @Test
    @Severity(SeverityLevel.LOW)
    @Description("Verify visual changes when filtering by policy type")
    @Story("Filtered View Visual")
    public void testFilteredViewVisual() throws Exception {
        // Apply filter
        dashboardPage.selectPolicyTypeFilter(PolicyTypes.AUTO)
                     .clickApplyFilters();
        
        // Verify filtered results loaded
        Assert.assertTrue(dashboardPage.getPolicyTableRows().size() > 0, "No policies displayed after filtering");
        
        // Capture screenshot of filtered view
        File filteredScreenshot = new File(screenshotDir + "/current_filtered_view.png");
        ScreenshotUtils.captureScreenshot(driver, filteredScreenshot.getPath());
        
        // Get baseline image
        File baselineImage = new File("src/test/resources/expected-results/filtered_view_baseline.png");
        
        // If baseline doesn't exist, use current as baseline
        if (!baselineImage.exists()) {
            logger.warning("Baseline image doesn't exist, using current screenshot as baseline");
            filteredScreenshot.renameTo(baselineImage);
        } else {
            // Compare screenshots
            File diffImage = new File(screenshotDir + "/filtered_view_diff.png");
            boolean result = ImageComparator.compareImages(baselineImage, filteredScreenshot, diffImage, visualThreshold);
            
            // Assert images match within threshold
            Assert.assertTrue(result, "Filtered view visual appearance has changed beyond threshold");
        }
    }
    
    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify full page visual appearance")
    @Story("Full Page Visual")
    public void testFullPageVisual() throws Exception {
        // Reset filters
        dashboardPage.resetFilters();
        
        // Capture full page screenshot
        File fullPageScreenshot = new File(screenshotDir + "/current_full_page.png");
        ScreenshotUtils.captureScreenshot(driver, fullPageScreenshot.getPath());
        
        // Get baseline image
        File baselineImage = new File("src/test/resources/expected-results/full_page_baseline.png");
        
        // If baseline doesn't exist, use current as baseline
        if (!baselineImage.exists()) {
            logger.warning("Baseline image doesn't exist, using current screenshot as baseline");
            fullPageScreenshot.renameTo(baselineImage);
        } else {
            // Compare screenshots with a slightly higher threshold for full page
            File diffImage = new File(screenshotDir + "/full_page_diff.png");
            boolean result = ImageComparator.compareImages(baselineImage, fullPageScreenshot, diffImage, visualThreshold + 1);
            
            // Assert images match within threshold
            Assert.assertTrue(result, "Full page visual appearance has changed beyond threshold");
        }
    }
}
