# 🤝 Contributing to Active Policies Dashboard Automation

Thank you for considering contributing! Here’s how to get started:

---

## 🛠️ Setup

1. Clone the repo:
   ```bash
   git clone https://github.com/your-username/active-policies-dashboard-automation.git
   cd active-policies-dashboard-automation
   ```

2. Install dependencies:
   ```bash
   mvn clean install
   ```

3. Run tests:
   ```bash
   mvn test
   ```

4. Generate Allure report:
   ```bash
   mvn allure:report
   mvn allure:serve
   ```

---

## ✨ Guidelines

- Follow the existing code structure and naming conventions
- Group tests logically (`FilterTests`, `ExportTests`, etc.)
- Run all tests before pushing
- Make sure your code is Java 11 compatible

---

## 📂 Folder Structure

- `src/main/...` → utilities and page objects
- `src/test/...` → base classes and test suites
- `resources/expected/` → expected outputs for file validations
- `.github/workflows/` → CI pipeline

---

## 📄 License

This project uses the MIT License.
