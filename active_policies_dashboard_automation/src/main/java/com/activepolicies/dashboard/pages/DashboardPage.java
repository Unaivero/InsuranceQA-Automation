package com.activepolicies.dashboard.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Page object for the dashboard page
 */
public class DashboardPage extends BasePage {

    private By policyTypeDropdown = By.id("policy-type-filter");
    private By startDatePicker = By.id("start-date-filter");
    private By endDatePicker = By.id("end-date-filter");
    private By applyFiltersButton = By.id("apply-filters-button");
    private By resetFiltersButton = By.id("reset-filters-button");
    private By policyTableRows = By.cssSelector("#policy-data-table tbody tr");
    private By policyCountElement = By.id("policy-count");
    private By exportCsvButton = By.id("export-csv-button");
    private By exportPdfButton = By.id("export-pdf-button");
    private By chartContainer = By.id("policy-chart-container");

    public DashboardPage(WebDriver driver) {
        super(driver);
        logger = Logger.getLogger(DashboardPage.class.getName());
    }

    /**
     * Select policy type from dropdown
     */
    public DashboardPage selectPolicyTypeFilter(String policyType) {
        logger.info("Selecting policy type: " + policyType);
        WebElement dropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(policyTypeDropdown));
        new Select(dropdown).selectByVisibleText(policyType);
        return this;
    }

    /**
     * Enter date range for filtering
     */
    public DashboardPage enterDateRange(String startDate, String endDate) {
        logger.info("Setting date range: " + startDate + " to " + endDate);
        wait.until(ExpectedConditions.visibilityOfElementLocated(startDatePicker)).sendKeys(startDate);
        wait.until(ExpectedConditions.visibilityOfElementLocated(endDatePicker)).sendKeys(endDate);
        return this;
    }

    /**
     * Click apply filters button
     */
    public DashboardPage clickApplyFilters() {
        logger.info("Applying filters");
        wait.until(ExpectedConditions.elementToBeClickable(applyFiltersButton)).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loading-spinner")));
        return this;
    }
    
    /**
     * Reset all filters
     */
    public DashboardPage resetFilters() {
        logger.info("Resetting filters");
        waitUtils.waitForClickabilityAndClick(resetFiltersButton);
        waitForLoad();
        return this;
    }

    /**
     * Get displayed policy count
     */
    public int getDisplayedPolicyCount() {
        WebElement countElement = wait.until(ExpectedConditions.visibilityOfElementLocated(policyCountElement));
        String countText = countElement.getText().replaceAll("[^0-9]", "");
        logger.info("Policy count: " + countText);
        return Integer.parseInt(countText);
    }

    /**
     * Get all policy table rows
     */
    public List<WebElement> getPolicyTableRows() {
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(policyTableRows));
    }
    
    /**
     * Get policy IDs from the table
     */
    public List<String> getPolicyIds() {
        List<WebElement> rows = getPolicyTableRows();
        return rows.stream()
            .map(row -> row.findElement(By.cssSelector("td:first-child")).getText())
            .collect(Collectors.toList());
    }

    /**
     * Click export to CSV button
     */
    public DashboardPage clickExportCsv() {
        logger.info("Clicking Export CSV button");
        wait.until(ExpectedConditions.elementToBeClickable(exportCsvButton)).click();
        waitForLoad();
        return this;
    }

    /**
     * Click export to PDF button
     */
    public DashboardPage clickExportPdf() {
        logger.info("Clicking Export PDF button");
        wait.until(ExpectedConditions.elementToBeClickable(exportPdfButton)).click();
        waitForLoad();
        return this;
    }

    /**
     * Get chart container element
     */
    public WebElement getChartContainer() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(chartContainer));
    }
    
    /**
     * Check if chart is visible
     */
    public boolean isChartVisible() {
        try {
            return getChartContainer().isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Check if specific policy exists in the table
     */
    public boolean isPolicyDisplayed(String policyId) {
        By policyRow = By.xpath("//table[@id='policy-data-table']//td[text()='" + policyId + "']");
        try {
            return waitUtils.waitForVisibility(policyRow).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public boolean isPageLoaded() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(chartContainer)).isDisplayed() &&
               wait.until(ExpectedConditions.visibilityOfElementLocated(policyTypeDropdown)).isDisplayed();
    }
}
