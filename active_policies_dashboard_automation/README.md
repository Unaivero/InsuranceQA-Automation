# 🧪 Active Policies Dashboard Automation

![CI](https://github.com/your-username/active-policies-dashboard-automation/actions/workflows/ci.yml/badge.svg)

A comprehensive test automation framework for the Active Policies Insurance Dashboard application. This project automates functional, regression, and visual testing using Selenium WebDriver, TestNG, and Allure reporting.

## 🌟 Features

- **Page Object Model** design pattern for maintainable UI interaction
- **Data-driven testing** with CSV and TestNG data providers
- **Parallel test execution** configured in TestNG
- **Visual testing** capabilities with screenshot comparison
- **Cross-browser testing** support
- **Comprehensive reporting** with Allure
- **Continuous Integration** with GitHub Actions

## 🏗️ Architecture

The framework follows a layered architecture:

- **Test Layer**: Test classes organized by feature/functionality
- **Page Object Layer**: Encapsulates page interactions and UI elements
- **Utility Layer**: Common helpers, file operations, configuration management
- **Configuration Layer**: Environment-specific settings

## 📂 Project Structure

```
active_policies_dashboard_automation/
├── .github/
│   └── workflows/          # CI/CD pipeline configurations
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/activepolicies/dashboard/
│   │           ├── constants/     # Constants and enums
│   │           ├── pages/         # Page objects
│   │           └── utils/         # Utility classes
│   └── test/
│       ├── java/
│       │   └── com/activepolicies/dashboard/
│       │       ├── listeners/     # TestNG listeners
│       │       └── tests/         # Test classes
│       └── resources/
│           ├── config/           # Environment configurations
│           ├── test-data/        # Test data files
│           ├── expected-results/ # Expected test outputs
│           └── testng.xml        # TestNG configuration
├── CONTRIBUTING.md         # Contribution guidelines
├── pom.xml                 # Maven dependencies and build config
└── README.md               # Project documentation
```

## 🚀 Getting Started

### Prerequisites

- Java 11 JDK
- Maven 3.6+
- Chrome/Firefox browser

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/active-policies-dashboard-automation.git
   cd active-policies-dashboard-automation
   ```

2. Install dependencies:
   ```bash
   mvn clean install
   ```

### Running Tests

Run all tests:
```bash
mvn test
```

Run specific test class:
```bash
mvn test -Dtest=LoginTests
```

Run tests with specific environment:
```bash
mvn test -Denv=staging
```

Generate Allure report:
```bash
mvn allure:report
mvn allure:serve
```

## 📊 Test Reporting

This project uses Allure for comprehensive test reporting with:

- Test execution status
- Step-by-step test execution
- Screenshots on failures
- Test execution metrics
- Filtering and categorization

## 🔄 Continuous Integration

Tests are automatically executed via GitHub Actions when:
- Code is pushed to the main branch
- Pull requests are created against the main branch

Artifacts from the CI build (including Allure reports) are available as downloadable assets.

## 🤝 Contributing

Contributions are welcome! Please see [CONTRIBUTING.md](CONTRIBUTING.md) for guidelines.

## 📝 License

This project is licensed under the MIT License.
