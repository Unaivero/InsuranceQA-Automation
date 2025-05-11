package com.activepolicies.dashboard.api;

import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Service for interacting with Policy API endpoints
 */
public class PolicyApiService {
    private static final Logger logger = LogManager.getLogger(PolicyApiService.class);
    private final ApiClient apiClient;
    
    /**
     * Constructor
     * @param apiClient API client
     */
    public PolicyApiService(ApiClient apiClient) {
        this.apiClient = apiClient;
    }
    
    /**
     * Gets all policies
     * @return Response with policies
     */
    public Response getAllPolicies() {
        logger.info("Getting all policies");
        return apiClient.get("/policies");
    }
    
    /**
     * Gets policies by type
     * @param policyType Policy type
     * @return Response with policies
     */
    public Response getPoliciesByType(String policyType) {
        logger.info("Getting policies by type: {}", policyType);
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("type", policyType);
        return apiClient.get("/policies", queryParams);
    }
    
    /**
     * Gets policy by ID
     * @param policyId Policy ID
     * @return Response with policy
     */
    public Response getPolicyById(String policyId) {
        logger.info("Getting policy by ID: {}", policyId);
        return apiClient.get("/policies/" + policyId);
    }
    
    /**
     * Creates a new policy
     * @param policyData Policy data
     * @return Response with created policy
     */
    public Response createPolicy(Map<String, Object> policyData) {
        logger.info("Creating new policy: {}", policyData);
        return apiClient.post("/policies", policyData);
    }
    
    /**
     * Updates policy
     * @param policyId Policy ID
     * @param policyData Policy data
     * @return Response with updated policy
     */
    public Response updatePolicy(String policyId, Map<String, Object> policyData) {
        logger.info("Updating policy {}: {}", policyId, policyData);
        return apiClient.put("/policies/" + policyId, policyData);
    }
    
    /**
     * Deletes policy
     * @param policyId Policy ID
     * @return Response
     */
    public Response deletePolicy(String policyId) {
        logger.info("Deleting policy: {}", policyId);
        return apiClient.delete("/policies/" + policyId);
    }
    
    /**
     * Gets policies by date range
     * @param startDate Start date
     * @param endDate End date
     * @return Response with policies
     */
    public Response getPoliciesByDateRange(String startDate, String endDate) {
        logger.info("Getting policies by date range: {} to {}", startDate, endDate);
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("startDate", startDate);
        queryParams.put("endDate", endDate);
        return apiClient.get("/policies", queryParams);
    }
    
    /**
     * Gets policies by customer
     * @param customerId Customer ID
     * @return Response with policies
     */
    public Response getPoliciesByCustomer(String customerId) {
        logger.info("Getting policies by customer: {}", customerId);
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("customerId", customerId);
        return apiClient.get("/policies", queryParams);
    }
    
    /**
     * Gets policy statistics
     * @return Response with policy statistics
     */
    public Response getPolicyStatistics() {
        logger.info("Getting policy statistics");
        return apiClient.get("/policies/statistics");
    }
    
    /**
     * Gets policy statistics by type
     * @param policyType Policy type
     * @return Response with policy statistics
     */
    public Response getPolicyStatisticsByType(String policyType) {
        logger.info("Getting policy statistics by type: {}", policyType);
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("type", policyType);
        return apiClient.get("/policies/statistics", queryParams);
    }
}
