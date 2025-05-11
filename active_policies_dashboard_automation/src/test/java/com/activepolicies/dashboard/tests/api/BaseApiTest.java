package com.activepolicies.dashboard.tests.api;

import com.activepolicies.dashboard.api.ApiClient;
import com.activepolicies.dashboard.utils.ConfigurationManager;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

/**
 * Base class for API tests
 */
public class BaseApiTest {
    protected static final Logger logger = LogManager.getLogger(BaseApiTest.class);
    protected ConfigurationManager config;
    protected ApiClient apiClient;
    protected String adminUsername;
    protected String adminPassword;
    
    @BeforeClass
    public void setupClass() {
        config = ConfigurationManager.getInstance();
        
        RestAssured.config = RestAssuredConfig.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails(LogDetail.ALL));
        
        // Add Allure listener for REST Assured
        RestAssured.filters(new AllureRestAssured());
        
        // Set base URI from config
        RestAssured.baseURI = config.getProperty("base.api.url", 
                config.getProperty("base.url") + "/api");
        
        logger.info("API base URI: {}", RestAssured.baseURI);
        
        // Get admin credentials
        adminUsername = config.getProperty("admin.username");
        adminPassword = config.getProperty("admin.password");
    }
    
    @BeforeMethod
    public void setupMethod() {
        apiClient = new ApiClient();
    }
    
    /**
     * Authenticates admin user
     */
    protected void authenticateAsAdmin() {
        apiClient.authenticate(adminUsername, adminPassword);
    }
}
