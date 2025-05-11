package com.activepolicies.dashboard.api;

import com.activepolicies.dashboard.utils.ConfigurationManager;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

/**
 * Client for making API requests
 */
public class ApiClient {
    private static final Logger logger = LogManager.getLogger(ApiClient.class);
    private final ConfigurationManager config;
    private String token;
    
    /**
     * Constructor
     */
    public ApiClient() {
        config = ConfigurationManager.getInstance();
        String baseApiUrl = config.getProperty("base.api.url", 
                config.getProperty("base.url") + "/api");
        
        RestAssured.baseURI = baseApiUrl;
    }
    
    /**
     * Sets authorization token
     * @param token Authorization token
     */
    public void setToken(String token) {
        this.token = token;
    }
    
    /**
     * Creates a request specification
     * @return Request specification
     */
    private RequestSpecification createRequest() {
        RequestSpecification request = RestAssured.given()
                .contentType(ContentType.JSON);
        
        if (token != null && !token.isEmpty()) {
            request.header("Authorization", "Bearer " + token);
        }
        
        return request;
    }
    
    /**
     * Performs GET request
     * @param endpoint API endpoint
     * @return Response
     */
    public Response get(String endpoint) {
        logger.info("Making GET request to: {}", endpoint);
        return createRequest().get(endpoint);
    }
    
    /**
     * Performs GET request with query parameters
     * @param endpoint API endpoint
     * @param queryParams Query parameters
     * @return Response
     */
    public Response get(String endpoint, Map<String, Object> queryParams) {
        logger.info("Making GET request to: {} with params: {}", endpoint, queryParams);
        return createRequest().queryParams(queryParams).get(endpoint);
    }
    
    /**
     * Performs POST request
     * @param endpoint API endpoint
     * @param body Request body
     * @return Response
     */
    public Response post(String endpoint, Object body) {
        logger.info("Making POST request to: {}", endpoint);
        return createRequest().body(body).post(endpoint);
    }
    
    /**
     * Performs PUT request
     * @param endpoint API endpoint
     * @param body Request body
     * @return Response
     */
    public Response put(String endpoint, Object body) {
        logger.info("Making PUT request to: {}", endpoint);
        return createRequest().body(body).put(endpoint);
    }
    
    /**
     * Performs DELETE request
     * @param endpoint API endpoint
     * @return Response
     */
    public Response delete(String endpoint) {
        logger.info("Making DELETE request to: {}", endpoint);
        return createRequest().delete(endpoint);
    }
    
    /**
     * Performs PATCH request
     * @param endpoint API endpoint
     * @param body Request body
     * @return Response
     */
    public Response patch(String endpoint, Object body) {
        logger.info("Making PATCH request to: {}", endpoint);
        return createRequest().body(body).patch(endpoint);
    }
    
    /**
     * Gets authentication token
     * @param username Username
     * @param password Password
     * @return Token
     */
    public String authenticate(String username, String password) {
        logger.info("Authenticating user: {}", username);
        
        LoginRequest loginRequest = new LoginRequest(username, password);
        
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(loginRequest)
                .post("/auth/login");
        
        if (response.getStatusCode() == 200) {
            token = response.jsonPath().getString("token");
            return token;
        } else {
            logger.error("Authentication failed with status code: {}", response.getStatusCode());
            throw new RuntimeException("Authentication failed: " + response.getStatusCode());
        }
    }
    
    /**
     * Login request class
     */
    private static class LoginRequest {
        private final String username;
        private final String password;
        
        public LoginRequest(String username, String password) {
            this.username = username;
            this.password = password;
        }
        
        public String getUsername() {
            return username;
        }
        
        public String getPassword() {
            return password;
        }
    }
}
