# Active Policies Dashboard Automation Framework Architecture

## 1. Introduction

This document provides an overview of the architecture and design of the Active Policies Dashboard Automation Framework. The framework is designed to automate testing of the insurance dashboard application at multiple levels: UI, API, and database.

## 2. Framework Architecture

The framework follows a multi-layered architecture with clear separation of concerns:

### 2.1. Page Object Layer

Encapsulates UI interactions and elements to provide a high-level interface for test scripts.

- **BasePage**: Abstract base class for all page objects with common functionality
- **LoginPage**: Handles authentication and login operations
- **DashboardPage**: Interacts with the dashboard UI elements
- Future page objects can be added as needed for new functionality

### 2.2. API Layer

Provides a clean interface for test scripts to interact with the application's REST APIs.

- **ApiClient**: Handles HTTP requests/responses and authentication
- **PolicyApiService**: Service-specific client for policy-related endpoints
- Can be extended with additional service clients as needed

### 2.3. Database Layer

Enables validation of database state and data integrity.

- **DatabaseManager**: Handles connection management and query execution
- **PolicyDatabaseService**: Service for policy-related database operations

### 2.4. Test Layer

Contains test classes organized by functionality and testing level.

- **UI Tests**: Tests for the user interface
- **API Tests**: Tests for the RESTful API
- **Database Tests**: Tests for database validation
- **Visual Tests**: Tests for visual regression

### 2.5. Utility Layer

Provides common functionality used across the framework.

- **ConfigurationManager**: Manages environment-specific configuration
- **DriverFactory**: Creates and configures WebDriver instances
- **FileUtils**: Handles file operations
- **ImageComparator**: Compares screenshots for visual testing
- **JavaScriptUtils**: Utility for JavaScript operations
- **ScreenshotUtils**: Takes and saves screenshots
- **TestDataLoader**: Loads test data from files
- **WaitUtils**: Handles synchronization and waits

### 2.6. Logging and Reporting

- **TestListener**: Handles test events and reporting
- **Allure Reporting**: Generates detailed test reports

## 3. Key Design Patterns

The framework implements several design patterns to ensure maintainability and extensibility:

### 3.1. Page Object Model (POM)

- Each page in the application has a corresponding Page Object class
- Page Objects abstract UI interactions and element locators
- Page Objects provide methods that represent user actions

### 3.2. Factory Pattern

- Used to create WebDriver instances based on configuration
- Provides flexibility to support multiple browsers and configurations

### 3.3. Singleton Pattern

- Used for ConfigurationManager to ensure a single instance is shared
- Provides consistent access to configuration properties

### 3.4. Builder Pattern

- Used in API clients to construct complex API requests
- Makes API test code more readable and maintainable

### 3.5. Data-Driven Testing

- External data sources (CSV, Excel) drive test scenarios
- Separates test data from test logic

## 4. Configuration Management

The framework supports multiple environments (dev, staging, production) through property files:

- **dev.properties**: Development environment configuration
- **staging.properties**: Staging environment configuration
- **prod.properties**: Production environment configuration

Environment-specific configuration includes:
- Base URLs
- Credentials
- Timeouts
- Database connection details

## 5. Test Execution

### 5.1. TestNG Configuration

Tests are organized and controlled through TestNG XML files:
- **testng.xml**: Main test suite configuration
- Parallel test execution is configured at the test level
- Tests are grouped logically by functionality

### 5.2. Maven Profiles

Environment selection is managed through Maven profiles:
- **dev**: Development environment (default)
- **staging**: Staging environment
- **prod**: Production environment

## 6. Reporting

Allure reporting provides comprehensive test reports including:
- Test execution status and statistics
- Step-by-step test execution details
- Screenshots for test failures
- API request/response details
- Environment information

## 7. Continuous Integration

The framework is integrated with GitHub Actions for continuous integration:
- Tests run automatically on code push and pull requests
- Test results are published as build artifacts
- Allure reports are generated and published

## 8. Test Data Management

Test data is managed through:
- CSV files for structured data
- Property files for configuration
- Baseline images for visual testing

## 9. Extensibility

The framework is designed to be extensible:
- New page objects can be added for new UI elements
- New API service clients can be added for new endpoints
- New database services can be added for new database operations
- New test classes can be added for new functionality

## 10. Best Practices

The framework follows these best practices:
- Clear separation of concerns
- Proper exception handling and logging
- Consistent naming conventions
- Comprehensive documentation
- Efficient resource management
- Proper cleanup in teardown methods
