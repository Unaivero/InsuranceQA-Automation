package com.activepolicies.dashboard.tests.api;

import com.activepolicies.dashboard.api.PolicyApiService;
import com.activepolicies.dashboard.constants.PolicyTypes;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Tests for Policy API endpoints
 */
@Feature("Policy API")
public class PolicyApiTests extends BaseApiTest {

    private PolicyApiService policyApiService;
    
    @BeforeClass
    public void setupPolicyTests() {
        authenticateAsAdmin();
        policyApiService = new PolicyApiService(apiClient);
    }
    
    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that all policies can be retrieved")
    @Story("Get All Policies")
    public void testGetAllPolicies() {
        Response response = policyApiService.getAllPolicies();
        
        Assert.assertEquals(response.getStatusCode(), 200, "Status code should be 200");
        Assert.assertTrue(response.jsonPath().getList("").size() > 0, "Response should contain policies");
    }
    
    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that policies can be filtered by type")
    @Story("Filter Policies By Type")
    public void testGetPoliciesByType() {
        Response response = policyApiService.getPoliciesByType(PolicyTypes.AUTO);
        
        Assert.assertEquals(response.getStatusCode(), 200, "Status code should be 200");
        
        // Verify all returned policies are of type AUTO
        response.jsonPath().getList("").forEach(policy -> {
            Map<String, Object> policyMap = (Map<String, Object>) policy;
            Assert.assertEquals(policyMap.get("type"), PolicyTypes.AUTO, 
                "Policy type should be " + PolicyTypes.AUTO);
        });
    }
    
    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that a policy can be retrieved by ID")
    @Story("Get Policy By ID")
    public void testGetPolicyById() {
        // First get all policies to find a policy ID
        Response allPoliciesResponse = policyApiService.getAllPolicies();
        Assert.assertEquals(allPoliciesResponse.getStatusCode(), 200, "Status code should be 200");
        
        // Get first policy ID
        String policyId = allPoliciesResponse.jsonPath().getString("[0].id");
        Assert.assertNotNull(policyId, "Policy ID should not be null");
        
        // Get policy by ID
        Response response = policyApiService.getPolicyById(policyId);
        
        Assert.assertEquals(response.getStatusCode(), 200, "Status code should be 200");
        Assert.assertEquals(response.jsonPath().getString("id"), policyId, 
            "Policy ID in response should match requested ID");
    }
    
    @Test
    @Severity(SeverityLevel.HIGH)
    @Description("Verify that a new policy can be created")
    @Story("Create Policy")
    public void testCreatePolicy() {
        // Create policy data
        Map<String, Object> policyData = new HashMap<>();
        policyData.put("type", PolicyTypes.AUTO);
        policyData.put("startDate", "2025-01-01");
        policyData.put("endDate", "2026-01-01");
        policyData.put("premium", 1000.00);
        policyData.put("customerName", "John Doe");
        policyData.put("customerEmail", "john.doe@example.com");
        policyData.put("status", "Active");
        
        // Create policy
        Response response = policyApiService.createPolicy(policyData);
        
        Assert.assertEquals(response.getStatusCode(), 201, "Status code should be 201");
        Assert.assertNotNull(response.jsonPath().getString("id"), "Policy ID should not be null");
        Assert.assertEquals(response.jsonPath().getString("type"), PolicyTypes.AUTO, 
            "Policy type should match");
        Assert.assertEquals(response.jsonPath().getString("customerName"), "John Doe", 
            "Customer name should match");
    }
    
    @Test
    @Severity(SeverityLevel.HIGH)
    @Description("Verify that a policy can be updated")
    @Story("Update Policy")
    public void testUpdatePolicy() {
        // First create a policy
        Map<String, Object> policyData = new HashMap<>();
        policyData.put("type", PolicyTypes.AUTO);
        policyData.put("startDate", "2025-02-01");
        policyData.put("endDate", "2026-02-01");
        policyData.put("premium", 1200.00);
        policyData.put("customerName", "Jane Smith");
        policyData.put("customerEmail", "jane.smith@example.com");
        policyData.put("status", "Active");
        
        Response createResponse = policyApiService.createPolicy(policyData);
        Assert.assertEquals(createResponse.getStatusCode(), 201, "Status code should be 201");
        
        String policyId = createResponse.jsonPath().getString("id");
        
        // Update policy data
        Map<String, Object> updateData = new HashMap<>();
        updateData.put("premium", 1500.00);
        updateData.put("status", "Renewed");
        
        // Update policy
        Response updateResponse = policyApiService.updatePolicy(policyId, updateData);
        
        Assert.assertEquals(updateResponse.getStatusCode(), 200, "Status code should be 200");
        Assert.assertEquals(updateResponse.jsonPath().getDouble("premium"), 1500.00, 
            "Premium should be updated");
        Assert.assertEquals(updateResponse.jsonPath().getString("status"), "Renewed", 
            "Status should be updated");
    }
    
    @Test
    @Severity(SeverityLevel.HIGH)
    @Description("Verify that a policy can be deleted")
    @Story("Delete Policy")
    public void testDeletePolicy() {
        // First create a policy
        Map<String, Object> policyData = new HashMap<>();
        policyData.put("type", PolicyTypes.HOME);
        policyData.put("startDate", "2025-03-01");
        policyData.put("endDate", "2026-03-01");
        policyData.put("premium", 800.00);
        policyData.put("customerName", "Alex Brown");
        policyData.put("customerEmail", "alex.brown@example.com");
        policyData.put("status", "Active");
        
        Response createResponse = policyApiService.createPolicy(policyData);
        Assert.assertEquals(createResponse.getStatusCode(), 201, "Status code should be 201");
        
        String policyId = createResponse.jsonPath().getString("id");
        
        // Delete policy
        Response deleteResponse = policyApiService.deletePolicy(policyId);
        
        Assert.assertEquals(deleteResponse.getStatusCode(), 204, "Status code should be 204");
        
        // Verify policy is deleted
        Response getResponse = policyApiService.getPolicyById(policyId);
        Assert.assertEquals(getResponse.getStatusCode(), 404, "Status code should be 404");
    }
    
    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that policy statistics can be retrieved")
    @Story("Get Policy Statistics")
    public void testGetPolicyStatistics() {
        Response response = policyApiService.getPolicyStatistics();
        
        Assert.assertEquals(response.getStatusCode(), 200, "Status code should be 200");
        Assert.assertTrue(response.jsonPath().getMap("").containsKey("totalPolicies"), 
            "Response should contain totalPolicies");
        Assert.assertTrue(response.jsonPath().getMap("").containsKey("totalPremium"), 
            "Response should contain totalPremium");
        Assert.assertTrue(response.jsonPath().getMap("").containsKey("averagePremium"), 
            "Response should contain averagePremium");
    }
}
