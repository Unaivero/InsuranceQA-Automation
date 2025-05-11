package com.activepolicies.dashboard.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Service for interacting with policy data in the database
 */
public class PolicyDatabaseService {
    private static final Logger logger = LogManager.getLogger(PolicyDatabaseService.class);
    private final DatabaseManager dbManager;
    
    /**
     * Constructor
     * @param dbManager Database manager
     */
    public PolicyDatabaseService(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }
    
    /**
     * Gets all policies
     * @return List of policies
     * @throws SQLException if query fails
     */
    public List<Map<String, Object>> getAllPolicies() throws SQLException {
        String query = "SELECT * FROM policies";
        return dbManager.executeQuery(query);
    }
    
    /**
     * Gets policy by ID
     * @param policyId Policy ID
     * @return Policy data
     * @throws SQLException if query fails
     */
    public Map<String, Object> getPolicyById(String policyId) throws SQLException {
        String query = "SELECT * FROM policies WHERE policy_id = ?";
        List<Object> params = new ArrayList<>();
        params.add(policyId);
        
        List<Map<String, Object>> results = dbManager.executeQuery(query, params);
        return results.isEmpty() ? null : results.get(0);
    }
    
    /**
     * Gets policies by type
     * @param policyType Policy type
     * @return List of policies
     * @throws SQLException if query fails
     */
    public List<Map<String, Object>> getPoliciesByType(String policyType) throws SQLException {
        String query = "SELECT * FROM policies WHERE policy_type = ?";
        List<Object> params = new ArrayList<>();
        params.add(policyType);
        
        return dbManager.executeQuery(query, params);
    }
    
    /**
     * Gets policies by date range
     * @param startDate Start date
     * @param endDate End date
     * @return List of policies
     * @throws SQLException if query fails
     */
    public List<Map<String, Object>> getPoliciesByDateRange(String startDate, String endDate) throws SQLException {
        String query = "SELECT * FROM policies WHERE start_date >= ? AND end_date <= ?";
        List<Object> params = new ArrayList<>();
        params.add(startDate);
        params.add(endDate);
        
        return dbManager.executeQuery(query, params);
    }
    
    /**
     * Creates policy
     * @param policyData Policy data
     * @return Generated policy ID
     * @throws SQLException if query fails
     */
    public String createPolicy(Map<String, Object> policyData) throws SQLException {
        String query = "INSERT INTO policies (policy_type, start_date, end_date, premium, customer_name, customer_email, status) " +
                      "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        List<Object> params = new ArrayList<>();
        params.add(policyData.get("type"));
        params.add(policyData.get("startDate"));
        params.add(policyData.get("endDate"));
        params.add(policyData.get("premium"));
        params.add(policyData.get("customerName"));
        params.add(policyData.get("customerEmail"));
        params.add(policyData.get("status"));
        
        List<Object> generatedKeys = dbManager.executeInsert(query, params);
        return generatedKeys.isEmpty() ? null : generatedKeys.get(0).toString();
    }
    
    /**
     * Updates policy
     * @param policyId Policy ID
     * @param policyData Policy data
     * @return Number of affected rows
     * @throws SQLException if query fails
     */
    public int updatePolicy(String policyId, Map<String, Object> policyData) throws SQLException {
        StringBuilder queryBuilder = new StringBuilder("UPDATE policies SET ");
        List<Object> params = new ArrayList<>();
        
        boolean firstParam = true;
        
        if (policyData.containsKey("type")) {
            queryBuilder.append("policy_type = ?");
            params.add(policyData.get("type"));
            firstParam = false;
        }
        
        if (policyData.containsKey("startDate")) {
            if (!firstParam) queryBuilder.append(", ");
            queryBuilder.append("start_date = ?");
            params.add(policyData.get("startDate"));
            firstParam = false;
        }
        
        if (policyData.containsKey("endDate")) {
            if (!firstParam) queryBuilder.append(", ");
            queryBuilder.append("end_date = ?");
            params.add(policyData.get("endDate"));
            firstParam = false;
        }
        
        if (policyData.containsKey("premium")) {
            if (!firstParam) queryBuilder.append(", ");
            queryBuilder.append("premium = ?");
            params.add(policyData.get("premium"));
            firstParam = false;
        }
        
        if (policyData.containsKey("customerName")) {
            if (!firstParam) queryBuilder.append(", ");
            queryBuilder.append("customer_name = ?");
            params.add(policyData.get("customerName"));
            firstParam = false;
        }
        
        if (policyData.containsKey("customerEmail")) {
            if (!firstParam) queryBuilder.append(", ");
            queryBuilder.append("customer_email = ?");
            params.add(policyData.get("customerEmail"));
            firstParam = false;
        }
        
        if (policyData.containsKey("status")) {
            if (!firstParam) queryBuilder.append(", ");
            queryBuilder.append("status = ?");
            params.add(policyData.get("status"));
        }
        
        queryBuilder.append(" WHERE policy_id = ?");
        params.add(policyId);
        
        return dbManager.executeUpdate(queryBuilder.toString(), params);
    }
    
    /**
     * Deletes policy
     * @param policyId Policy ID
     * @return Number of affected rows
     * @throws SQLException if query fails
     */
    public int deletePolicy(String policyId) throws SQLException {
        String query = "DELETE FROM policies WHERE policy_id = ?";
        List<Object> params = new ArrayList<>();
        params.add(policyId);
        
        return dbManager.executeUpdate(query, params);
    }
    
    /**
     * Gets policy count
     * @return Policy count
     * @throws SQLException if query fails
     */
    public int getPolicyCount() throws SQLException {
        return dbManager.getRowCount("policies");
    }
    
    /**
     * Gets policy count by type
     * @param policyType Policy type
     * @return Policy count
     * @throws SQLException if query fails
     */
    public int getPolicyCountByType(String policyType) throws SQLException {
        String query = "SELECT COUNT(*) AS count FROM policies WHERE policy_type = ?";
        List<Object> params = new ArrayList<>();
        params.add(policyType);
        
        List<Map<String, Object>> results = dbManager.executeQuery(query, params);
        return results.isEmpty() ? 0 : ((Number) results.get(0).get("count")).intValue();
    }
    
    /**
     * Gets total premium value
     * @return Total premium value
     * @throws SQLException if query fails
     */
    public double getTotalPremium() throws SQLException {
        String query = "SELECT SUM(premium) AS total FROM policies";
        
        List<Map<String, Object>> results = dbManager.executeQuery(query);
        return results.isEmpty() ? 0 : ((Number) results.get(0).get("total")).doubleValue();
    }
    
    /**
     * Gets average premium value
     * @return Average premium value
     * @throws SQLException if query fails
     */
    public double getAveragePremium() throws SQLException {
        String query = "SELECT AVG(premium) AS average FROM policies";
        
        List<Map<String, Object>> results = dbManager.executeQuery(query);
        return results.isEmpty() ? 0 : ((Number) results.get(0).get("average")).doubleValue();
    }
    
    /**
     * Gets policy statistics
     * @return Policy statistics
     * @throws SQLException if query fails
     */
    public Map<String, Object> getPolicyStatistics() throws SQLException {
        String query = "SELECT " +
                      "COUNT(*) AS total_policies, " +
                      "SUM(premium) AS total_premium, " +
                      "AVG(premium) AS average_premium " +
                      "FROM policies";
        
        List<Map<String, Object>> results = dbManager.executeQuery(query);
        return results.isEmpty() ? null : results.get(0);
    }
    
    /**
     * Verifies policy data in database matches UI
     * @param policyId Policy ID
     * @param uiData UI data
     * @return true if data matches, false otherwise
     * @throws SQLException if query fails
     */
    public boolean verifyPolicyData(String policyId, Map<String, Object> uiData) throws SQLException {
        Map<String, Object> dbData = getPolicyById(policyId);
        
        if (dbData == null) {
            logger.warn("Policy not found in database: {}", policyId);
            return false;
        }
        
        // Compare key fields
        boolean matches = true;
        
        if (uiData.containsKey("type") && !dbData.get("policy_type").equals(uiData.get("type"))) {
            logger.warn("Policy type mismatch: {} vs {}", dbData.get("policy_type"), uiData.get("type"));
            matches = false;
        }
        
        if (uiData.containsKey("premium")) {
            double dbPremium = ((Number) dbData.get("premium")).doubleValue();
            double uiPremium = Double.parseDouble(uiData.get("premium").toString());
            
            if (Math.abs(dbPremium - uiPremium) > 0.01) {
                logger.warn("Premium mismatch: {} vs {}", dbPremium, uiPremium);
                matches = false;
            }
        }
        
        if (uiData.containsKey("customerName") && !dbData.get("customer_name").equals(uiData.get("customerName"))) {
            logger.warn("Customer name mismatch: {} vs {}", dbData.get("customer_name"), uiData.get("customerName"));
            matches = false;
        }
        
        if (uiData.containsKey("status") && !dbData.get("status").equals(uiData.get("status"))) {
            logger.warn("Status mismatch: {} vs {}", dbData.get("status"), uiData.get("status"));
            matches = false;
        }
        
        return matches;
    }
}
