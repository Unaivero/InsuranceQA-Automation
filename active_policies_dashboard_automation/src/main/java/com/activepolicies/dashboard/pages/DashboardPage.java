package com.activepolicies.dashboard.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class DashboardPage {

    private WebDriver driver;
    private WebDriverWait wait;

    private By policyTypeDropdown = By.id("policy-type-filter");
    private By startDatePicker = By.id("start-date-filter");
    private By endDatePicker = By.id("end-date-filter");
    private By applyFiltersButton = By.id("apply-filters-button");
    private By policyTableRows = By.cssSelector("#policy-data-table tbody tr");
    private By policyCountElement = By.id("policy-count");
    private By exportCsvButton = By.id("export-csv-button");
    private By exportPdfButton = By.id("export-pdf-button");
    private By chartContainer = By.id("policy-chart-container");

    public DashboardPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
    }

    public void selectPolicyTypeFilter(String policyType) {
        WebElement dropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(policyTypeDropdown));
        new Select(dropdown).selectByVisibleText(policyType);
    }

    public void enterDateRange(String startDate, String endDate) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(startDatePicker)).sendKeys(startDate);
        wait.until(ExpectedConditions.visibilityOfElementLocated(endDatePicker)).sendKeys(endDate);
    }

    public void clickApplyFilters() {
        wait.until(ExpectedConditions.elementToBeClickable(applyFiltersButton)).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loading-spinner")));
    }

    public int getDisplayedPolicyCount() {
        WebElement countElement = wait.until(ExpectedConditions.visibilityOfElementLocated(policyCountElement));
        String countText = countElement.getText().replaceAll("[^0-9]", "");
        return Integer.parseInt(countText);
    }

    public List<WebElement> getPolicyTableRows() {
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(policyTableRows));
    }

    public void clickExportCsv() {
        wait.until(ExpectedConditions.elementToBeClickable(exportCsvButton)).click();
    }

    public void clickExportPdf() {
        wait.until(ExpectedConditions.elementToBeClickable(exportPdfButton)).click();
    }

    public WebElement getChartContainer() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(chartContainer));
    }
}
