package com.activepolicies.dashboard.db;

import com.activepolicies.dashboard.utils.ConfigurationManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages database connections and operations
 */
public class DatabaseManager {
    private static final Logger logger = LogManager.getLogger(DatabaseManager.class);
    private final ConfigurationManager config;
    private Connection connection;
    
    /**
     * Constructor
     */
    public DatabaseManager() {
        config = ConfigurationManager.getInstance();
    }
    
    /**
     * Opens database connection
     * @throws SQLException if connection fails
     */
    public void openConnection() throws SQLException {
        String dbUrl = config.getProperty("db.url");
        String dbUser = config.getProperty("db.username");
        String dbPassword = config.getProperty("db.password");
        
        logger.info("Opening database connection to: {}", dbUrl);
        
        try {
            connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        } catch (SQLException e) {
            logger.error("Failed to open database connection", e);
            throw e;
        }
    }
    
    /**
     * Closes database connection
     */
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                logger.info("Database connection closed");
            } catch (SQLException e) {
                logger.error("Failed to close database connection", e);
            }
        }
    }
    
    /**
     * Executes SELECT query and returns results as list of maps
     * @param query SQL query
     * @return List of maps representing rows
     * @throws SQLException if query fails
     */
    public List<Map<String, Object>> executeQuery(String query) throws SQLException {
        return executeQuery(query, null);
    }
    
    /**
     * Executes SELECT query with parameters and returns results as list of maps
     * @param query SQL query
     * @param params Query parameters
     * @return List of maps representing rows
     * @throws SQLException if query fails
     */
    public List<Map<String, Object>> executeQuery(String query, List<Object> params) throws SQLException {
        logger.info("Executing query: {}", query);
        
        List<Map<String, Object>> results = new ArrayList<>();
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            // Set parameters if any
            if (params != null) {
                for (int i = 0; i < params.size(); i++) {
                    stmt.setObject(i + 1, params.get(i));
                }
            }
            
            ResultSet rs = stmt.executeQuery();
            int columnCount = rs.getMetaData().getColumnCount();
            
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = rs.getMetaData().getColumnName(i);
                    Object value = rs.getObject(i);
                    row.put(columnName, value);
                }
                
                results.add(row);
            }
        } catch (SQLException e) {
            logger.error("Failed to execute query", e);
            throw e;
        }
        
        logger.info("Query returned {} rows", results.size());
        return results;
    }
    
    /**
     * Executes UPDATE, INSERT, or DELETE query
     * @param query SQL query
     * @return Number of affected rows
     * @throws SQLException if query fails
     */
    public int executeUpdate(String query) throws SQLException {
        return executeUpdate(query, null);
    }
    
    /**
     * Executes UPDATE, INSERT, or DELETE query with parameters
     * @param query SQL query
     * @param params Query parameters
     * @return Number of affected rows
     * @throws SQLException if query fails
     */
    public int executeUpdate(String query, List<Object> params) throws SQLException {
        logger.info("Executing update: {}", query);
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            // Set parameters if any
            if (params != null) {
                for (int i = 0; i < params.size(); i++) {
                    stmt.setObject(i + 1, params.get(i));
                }
            }
            
            int rowsAffected = stmt.executeUpdate();
            logger.info("Update affected {} rows", rowsAffected);
            return rowsAffected;
        } catch (SQLException e) {
            logger.error("Failed to execute update", e);
            throw e;
        }
    }
    
    /**
     * Executes INSERT query and returns generated keys
     * @param query SQL query
     * @param params Query parameters
     * @return Generated keys
     * @throws SQLException if query fails
     */
    public List<Object> executeInsert(String query, List<Object> params) throws SQLException {
        logger.info("Executing insert: {}", query);
        
        List<Object> generatedKeys = new ArrayList<>();
        
        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            // Set parameters if any
            if (params != null) {
                for (int i = 0; i < params.size(); i++) {
                    stmt.setObject(i + 1, params.get(i));
                }
            }
            
            int rowsAffected = stmt.executeUpdate();
            logger.info("Insert affected {} rows", rowsAffected);
            
            ResultSet rs = stmt.getGeneratedKeys();
            while (rs.next()) {
                generatedKeys.add(rs.getObject(1));
            }
        } catch (SQLException e) {
            logger.error("Failed to execute insert", e);
            throw e;
        }
        
        return generatedKeys;
    }
    
    /**
     * Checks if table exists
     * @param tableName Table name
     * @return true if table exists, false otherwise
     * @throws SQLException if query fails
     */
    public boolean tableExists(String tableName) throws SQLException {
        try (ResultSet rs = connection.getMetaData().getTables(
                null, null, tableName, new String[]{"TABLE"})) {
            return rs.next();
        } catch (SQLException e) {
            logger.error("Failed to check if table exists", e);
            throw e;
        }
    }
    
    /**
     * Gets row count for table
     * @param tableName Table name
     * @return Row count
     * @throws SQLException if query fails
     */
    public int getRowCount(String tableName) throws SQLException {
        String query = "SELECT COUNT(*) AS count FROM " + tableName;
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getInt("count");
            }
            return 0;
        } catch (SQLException e) {
            logger.error("Failed to get row count for {}", tableName, e);
            throw e;
        }
    }
}
