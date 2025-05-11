package com.activepolicies.dashboard.tests.db;

import com.activepolicies.dashboard.constants.PolicyTypes;
import com.activepolicies.dashboard.db.PolicyDatabaseService;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Tests for policy database operations
 */
@Feature("Database Validation")
public class PolicyDatabaseTests extends BaseDatabaseTest {

    private PolicyDatabaseService policyDbService;
    
    /**
     * Setup before class
     */
    @BeforeClass
    public void setupPolicyTests() {
        policyDbService = new PolicyDatabaseService(dbManager);
    }
    
    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that policies can be retrieved from database")
    @Story("Get All Policies")
    public void testGetAllPolicies() throws SQLException {
        List<Map<String, Object>> policies = policyDbService.getAllPolicies();
        
        Assert.assertNotNull(policies, "Policies should not be null");
        logger.info("Retrieved {} policies from database", policies.size());
    }
    
    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that policies can be filtered by type in database")
    @Story("Filter Policies By Type")
    public void testGetPoliciesByType() throws SQLException {
        List<Map<String, Object>> policies = policyDbService.getPoliciesByType(PolicyTypes.AUTO);
        
        Assert.assertNotNull(policies, "Policies should not be null");
        logger.info("Retrieved {} AUTO policies from database", policies.size());
        
        // Verify all policies are of type AUTO
        for (Map<String, Object> policy : policies) {
            Assert.assertEquals(policy.get("policy_type"), PolicyTypes.AUTO, 
                "Policy type should be AUTO");
        }
    }
    
    @Test
    @Severity(SeverityLevel.HIGH)
    @Description("Verify that a policy can be created in database")
    @Story("Create Policy")
    public void testCreatePolicy() throws SQLException {
        // Create policy data
        Map<String, Object> policyData = new HashMap<>();
        policyData.put("type", PolicyTypes.HOME);
        policyData.put("startDate", "2025-04-01");
        policyData.put("endDate", "2026-04-01");
        policyData.put("premium", 1200.00);
        policyData.put("customerName", "Test User");
        policyData.put("customerEmail", "test.user@example.com");
        policyData.put("status", "Active");
        
        // Create policy
        String policyId = policyDbService.createPolicy(policyData);
        
        Assert.assertNotNull(policyId, "Policy ID should not be null");
        logger.info("Created policy with ID: {}", policyId);
        
        // Verify policy exists
        Map<String, Object> createdPolicy = policyDbService.getPolicyById(policyId);
        Assert.assertNotNull(createdPolicy, "Created policy should exist in database");
        Assert.assertEquals(createdPolicy.get("policy_type"), PolicyTypes.HOME, 
            "Policy type should be HOME");
        Assert.assertEquals(createdPolicy.get("customer_name"), "Test User", 
            "Customer name should be Test User");
        
        // Cleanup
        policyDbService.deletePolicy(policyId);
    }
    
    @Test
    @Severity(SeverityLevel.HIGH)
    @Description("Verify that a policy can be updated in database")
    @Story("Update Policy")
    public void testUpdatePolicy() throws SQLException {
        // Create policy data
        Map<String, Object> policyData = new HashMap<>();
        policyData.put("type", PolicyTypes.COMMERCIAL);
        policyData.put("startDate", "2025-05-01");
        policyData.put("endDate", "2026-05-01");
        policyData.put("premium", 2500.00);
        policyData.put("customerName", "Business Corp");
        policyData.put("customerEmail", "contact@businesscorp.example.com");
        policyData.put("status", "Active");
        
        // Create policy
        String policyId = policyDbService.createPolicy(policyData);
        Assert.assertNotNull(policyId, "Policy ID should not be null");
        
        // Update policy data
        Map<String, Object> updateData = new HashMap<>();
        updateData.put("premium", 3000.00);
        updateData.put("status", "Renewed");
        
        // Update policy
        int rowsUpdated = policyDbService.updatePolicy(policyId, updateData);
        Assert.assertEquals(rowsUpdated, 1, "One row should be updated");
        
        // Verify policy updated
        Map<String, Object> updatedPolicy = policyDbService.getPolicyById(policyId);
        Assert.assertNotNull(updatedPolicy, "Updated policy should exist in database");
        Assert.assertEquals(((Number) updatedPolicy.get("premium")).doubleValue(), 3000.00, 
            "Premium should be updated to 3000.00");
        Assert.assertEquals(updatedPolicy.get("status"), "Renewed", 
            "Status should be updated to Renewed");
        
        // Cleanup
        policyDbService.deletePolicy(policyId);
    }
    
    @Test
    @Severity(SeverityLevel.HIGH)
    @Description("Verify that a policy can be deleted from database")
    @Story("Delete Policy")
    public void testDeletePolicy() throws SQLException {
        // Create policy data
        Map<String, Object> policyData = new HashMap<>();
        policyData.put("type", PolicyTypes.HEALTH);
        policyData.put("startDate", "2025-06-01");
        policyData.put("endDate", "2026-06-01");
        policyData.put("premium", 1800.00);
        policyData.put("customerName", "Test Delete");
        policyData.put("customerEmail", "test.delete@example.com");
        policyData.put("status", "Active");
        
        // Create policy
        String policyId = policyDbService.createPolicy(policyData);
        Assert.assertNotNull(policyId, "Policy ID should not be null");
        
        // Verify policy exists
        Map<String, Object> createdPolicy = policyDbService.getPolicyById(policyId);
        Assert.assertNotNull(createdPolicy, "Created policy should exist in database");
        
        // Delete policy
        int rowsDeleted = policyDbService.deletePolicy(policyId);
        Assert.assertEquals(rowsDeleted, 1, "One row should be deleted");
        
        // Verify policy deleted
        Map<String, Object> deletedPolicy = policyDbService.getPolicyById(policyId);
        Assert.assertNull(deletedPolicy, "Deleted policy should not exist in database");
    }
    
    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that policy statistics can be retrieved from database")
    @Story("Get Policy Statistics")
    public void testGetPolicyStatistics() throws SQLException {
        Map<String, Object> statistics = policyDbService.getPolicyStatistics();
        
        Assert.assertNotNull(statistics, "Statistics should not be null");
        Assert.assertTrue(statistics.containsKey("total_policies"), 
            "Statistics should contain total_policies");
        Assert.assertTrue(statistics.containsKey("total_premium"), 
            "Statistics should contain total_premium");
        Assert.assertTrue(statistics.containsKey("average_premium"), 
            "Statistics should contain average_premium");
        
        logger.info("Policy statistics: {}", statistics);
    }
    
    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify policy data integrity between UI and database")
    @Story("Data Integrity Validation")
    public void testPolicyDataIntegrity() throws SQLException {
        // Create policy data
        Map<String, Object> policyData = new HashMap<>();
        policyData.put("type", PolicyTypes.TRAVEL);
        policyData.put("startDate", "2025-07-01");
        policyData.put("endDate", "2025-08-01");
        policyData.put("premium", 250.00);
        policyData.put("customerName", "Test Integrity");
        policyData.put("customerEmail", "test.integrity@example.com");
        policyData.put("status", "Active");
        
        // Create policy
        String policyId = policyDbService.createPolicy(policyData);
        Assert.assertNotNull(policyId, "Policy ID should not be null");
        
        // Simulate UI data (same as what was created)
        Map<String, Object> uiData = new HashMap<>(policyData);
        
        // Verify data integrity
        boolean matches = policyDbService.verifyPolicyData(policyId, uiData);
        Assert.assertTrue(matches, "Policy data should match between UI and database");
        
        // Cleanup
        policyDbService.deletePolicy(policyId);
    }
}
