# 🧪 Insurance QA Automation

![CI](https://github.com/your-username/InsuranceQA-Automation/actions/workflows/ci.yml/badge.svg)

A comprehensive QA automation framework for insurance dashboard applications. This project provides end-to-end testing capabilities including UI, API, and database validation.

## 🌟 Framework Features

- **Multi-layered Testing Architecture**:
  - UI Testing with Selenium WebDriver
  - API Testing with REST Assured
  - Database Testing with JDBC
  - Visual Testing with screenshot comparison

- **Page Object Model** design for maintainable UI automation
- **Data-driven testing** with CSV and parameterized tests
- **Multi-environment support** (dev, staging, production)
- **Comprehensive reporting** with Allure
- **Parallel test execution** for faster feedback
- **CI/CD Integration** with GitHub Actions

## 🏗️ Architecture

```
InsuranceQA-Automation/
└── active_policies_dashboard_automation/
    ├── src/
    │   ├── main/
    │   │   └── java/
    │   │       └── com/activepolicies/dashboard/
    │   │           ├── api/        # API client and services
    │   │           ├── constants/  # Constant values and enums
    │   │           ├── db/         # Database services
    │   │           ├── pages/      # Page objects for UI
    │   │           └── utils/      # Utility classes
    │   └── test/
    │       ├── java/
    │       │   └── com/activepolicies/dashboard/
    │       │       ├── listeners/  # TestNG listeners
    │       │       └── tests/      # Test classes
    │       │           ├── api/    # API tests
    │       │           └── db/     # Database tests
    │       └── resources/
    │           ├── config/         # Environment configurations
    │           ├── test-data/      # Test data files
    │           ├── expected-results/ # Expected outputs for validation
    │           └── testng.xml      # Test suite configuration
    └── .github/
        └── workflows/              # CI/CD pipeline
```

## 🚀 Getting Started

### Prerequisites

- Java 11 JDK
- Maven 3.6+
- Chrome/Firefox browser
- MySQL database (for database tests)

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/InsuranceQA-Automation.git
   cd InsuranceQA-Automation
   ```

2. Install dependencies:
   ```bash
   mvn clean install -DskipTests
   ```

### Running Tests

#### Running All Tests

```bash
mvn test
```

#### Running Specific Test Types

UI Tests:
```bash
mvn test -Dgroups=ui
```

API Tests:
```bash
mvn test -Dgroups=api
```

Database Tests:
```bash
mvn test -Dgroups=db
```

#### Running in Different Environments

```bash
mvn test -Pdev    # Development environment (default)
mvn test -Pstaging   # Staging environment
mvn test -Pprod   # Production environment
```

### Generating Reports

```bash
mvn allure:report   # Generate report
mvn allure:serve    # Generate and open in browser
```

## 📊 Test Reporting

Allure reports provide detailed test execution information:

- Test execution status and statistics
- Step-by-step test execution details
- Screenshots on test failures
- API request/response details
- Test execution metrics and trends
- Environment information

## 🔄 Continuous Integration

Tests are automatically executed via GitHub Actions when:
- Code is pushed to the main branch
- Pull requests are created against the main branch

The CI pipeline includes:
- Running tests in headless mode
- Generating Allure reports
- Uploading test artifacts

## 🧩 Key Components

### UI Testing

- **Page Objects**: Encapsulate page interactions and elements
- **Visual Validation**: Compare screenshots to detect visual regressions
- **Cross-browser Testing**: Support for Chrome, Firefox, and Edge

### API Testing

- **RESTful API Testing**: Validate responses, status codes, and payloads
- **Schema Validation**: Ensure API responses match expected schemas
- **Data-driven Testing**: Test multiple scenarios with different data sets

### Database Testing

- **Data Validation**: Verify database records match expected values
- **Database Integration**: Compare UI/API data with database state
- **Transaction Verification**: Ensure database transactions work correctly

## 🤝 Contributing

Contributions are welcome! Please see [CONTRIBUTING.md](active_policies_dashboard_automation/CONTRIBUTING.md) for guidelines.

## 📝 License

This project is licensed under the MIT License.
