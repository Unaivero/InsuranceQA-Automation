package com.activepolicies.dashboard.tests.db;

import com.activepolicies.dashboard.db.DatabaseManager;
import com.activepolicies.dashboard.utils.ConfigurationManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import java.sql.SQLException;

/**
 * Base class for database tests
 */
public class BaseDatabaseTest {
    protected static final Logger logger = LogManager.getLogger(BaseDatabaseTest.class);
    protected ConfigurationManager config;
    protected DatabaseManager dbManager;
    
    /**
     * Setup before class
     */
    @BeforeClass
    public void setupClass() {
        config = ConfigurationManager.getInstance();
        dbManager = new DatabaseManager();
    }
    
    /**
     * Setup before each test method
     * @throws SQLException if database connection fails
     */
    @BeforeMethod
    public void setupMethod() throws SQLException {
        try {
            dbManager.openConnection();
        } catch (SQLException e) {
            logger.error("Failed to open database connection", e);
            throw e;
        }
    }
    
    /**
     * Cleanup after each test method
     */
    @AfterMethod
    public void cleanupMethod() {
        dbManager.closeConnection();
    }
    
    /**
     * Cleanup after class
     */
    @AfterClass
    public void cleanupClass() {
        // Perform any necessary cleanup
    }
}
