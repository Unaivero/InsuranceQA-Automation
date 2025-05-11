package com.activepolicies.dashboard.tests;

import com.activepolicies.dashboard.constants.PolicyTypes;
import com.activepolicies.dashboard.pages.DashboardPage;
import com.activepolicies.dashboard.pages.LoginPage;
import com.activepolicies.dashboard.utils.FileUtils;
import com.opencsv.CSVReader;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileReader;
import java.util.List;

/**
 * Tests for data export functionality
 */
@Feature("Export")
public class ExportTests extends BaseTest {

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
    @Description("Verify export to CSV functionality")
    @Story("CSV Export")
    public void testExportToCsv() throws Exception {
        // Reset filters to ensure all data is included
        dashboardPage.resetFilters();
        
        // Get current policy count
        int policyCount = dashboardPage.getDisplayedPolicyCount();
        
        // Export to CSV
        dashboardPage.clickExportCsv();
        
        // Wait for file download
        String downloadDir = config.getProperty("download.dir");
        int downloadTimeout = config.getIntProperty("file.download.timeout.seconds");
        File csvFile = FileUtils.waitForFile(downloadDir, "policies", downloadTimeout);
        
        // Verify file downloaded
        Assert.assertTrue(csvFile.exists(), "CSV file was not downloaded");
        Assert.assertTrue(csvFile.length() > 0, "CSV file is empty");
        
        // Parse CSV and verify content
        List<String[]> csvData = FileUtils.readCsv(csvFile);
        
        // Verify header row exists
        Assert.assertTrue(csvData.size() > 0, "CSV has no data");
        
        // Verify data rows match count (header + data rows)
        Assert.assertEquals(csvData.size(), policyCount + 1, 
            "CSV row count doesn't match policy count");
    }
    
    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify export to PDF functionality")
    @Story("PDF Export")
    public void testExportToPdf() throws Exception {
        // Reset filters
        dashboardPage.resetFilters();
        
        // Export to PDF
        dashboardPage.clickExportPdf();
        
        // Wait for file download
        String downloadDir = config.getProperty("download.dir");
        int downloadTimeout = config.getIntProperty("file.download.timeout.seconds");
        File pdfFile = FileUtils.waitForFile(downloadDir, "policies", downloadTimeout);
        
        // Verify file downloaded
        Assert.assertTrue(pdfFile.exists(), "PDF file was not downloaded");
        Assert.assertTrue(pdfFile.length() > 0, "PDF file is empty");
        
        // Additional PDF content validation could be added here
    }
    
    @Test
    @Severity(SeverityLevel.HIGH)
    @Description("Verify filtered data export to CSV")
    @Story("Filtered CSV Export")
    public void testFilteredExportToCsv() throws Exception {
        // Apply filter
        dashboardPage.selectPolicyTypeFilter(PolicyTypes.AUTO)
                     .clickApplyFilters();
        
        // Get filtered count
        int filteredCount = dashboardPage.getDisplayedPolicyCount();
        
        // Export filtered data
        dashboardPage.clickExportCsv();
        
        // Wait for file download
        String downloadDir = config.getProperty("download.dir");
        int downloadTimeout = config.getIntProperty("file.download.timeout.seconds");
        File csvFile = FileUtils.waitForFile(downloadDir, "policies", downloadTimeout);
        
        // Parse CSV
        List<String[]> csvData = FileUtils.readCsv(csvFile);
        
        // Verify row count matches filtered data (header + data rows)
        Assert.assertEquals(csvData.size(), filteredCount + 1, 
            "CSV row count doesn't match filtered policy count");
        
        // Verify all exported policies are of correct type
        for (int i = 1; i < csvData.size(); i++) { // Skip header row
            String[] row = csvData.get(i);
            String policyType = row[1]; // Assuming policy type is in column index 1
            Assert.assertEquals(policyType, PolicyTypes.AUTO, 
                "Non-Auto policy found in filtered export: " + policyType);
        }
    }
    
    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify exported CSV matches expected format")
    @Story("CSV Format Validation")
    public void testCsvFormat() throws Exception {
        // Reset filters
        dashboardPage.resetFilters();
        
        // Export to CSV
        dashboardPage.clickExportCsv();
        
        // Wait for file download
        String downloadDir = config.getProperty("download.dir");
        int downloadTimeout = config.getIntProperty("file.download.timeout.seconds");
        File csvFile = FileUtils.waitForFile(downloadDir, "policies", downloadTimeout);
        
        // Parse CSV
        try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {
            // Read header row
            String[] header = reader.readNext();
            
            // Verify header columns
            Assert.assertEquals(header[0], "PolicyID", "First column should be PolicyID");
            Assert.assertEquals(header[1], "PolicyType", "Second column should be PolicyType");
            Assert.assertTrue(header.length >= 7, "CSV should have at least 7 columns");
            
            // Read first data row
            String[] dataRow = reader.readNext();
            if (dataRow != null) {
                // Verify policy ID format
                Assert.assertTrue(dataRow[0].matches("POL-\\d+"), 
                    "Policy ID should match POL-#### format: " + dataRow[0]);
                
                // Verify policy type is valid
                String policyType = dataRow[1];
                boolean validType = policyType.equals(PolicyTypes.AUTO) ||
                                   policyType.equals(PolicyTypes.HOME) ||
                                   policyType.equals(PolicyTypes.LIFE) ||
                                   policyType.equals(PolicyTypes.HEALTH) ||
                                   policyType.equals(PolicyTypes.COMMERCIAL) ||
                                   policyType.equals(PolicyTypes.TRAVEL) ||
                                   policyType.equals(PolicyTypes.PET);
                                   
                Assert.assertTrue(validType, "Invalid policy type: " + policyType);
            }
        }
    }
}
