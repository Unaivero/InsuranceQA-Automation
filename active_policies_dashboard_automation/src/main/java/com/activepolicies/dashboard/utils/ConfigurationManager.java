package com.activepolicies.dashboard.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Handles application configuration and environment properties
 */
public class ConfigurationManager {
    private static final Properties properties = new Properties();
    private static ConfigurationManager instance;
    private static final String DEFAULT_ENV = "dev";

    private ConfigurationManager() {
        String env = System.getProperty("env", DEFAULT_ENV);
        loadProperties(env);
    }

    public static synchronized ConfigurationManager getInstance() {
        if (instance == null) {
            instance = new ConfigurationManager();
        }
        return instance;
    }

    private void loadProperties(String env) {
        try (InputStream input = new FileInputStream(
                "src/test/resources/config/" + env + ".properties")) {
            properties.load(input);
        } catch (IOException ex) {
            throw new RuntimeException("Failed to load properties file for environment: " + env, ex);
        }
    }

    public String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            throw new RuntimeException("Property not found: " + key);
        }
        return value;
    }

    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
    
    public int getIntProperty(String key) {
        return Integer.parseInt(getProperty(key));
    }
    
    public boolean getBooleanProperty(String key) {
        return Boolean.parseBoolean(getProperty(key));
    }
}
