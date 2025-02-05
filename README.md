# BelQAWright (Playwright testing)
This project contains automated **UI and API tests** for a web application using **Playwright**, **TestNG**, and **Allure Reporting**. The tests cover various **E2E scenarios** and **component interactions**, such as **forms, charts, tables, smart home devices, and more**.  

The tested application is based on [pw-practice-app](https://github.com/bondar-artem/pw-practice-app), used in the [Playwright from Zero to Hero](https://www.udemy.com/course/playwright-from-zero-to-hero/) course on Udemy.  

## 📌 Features  

- **End-to-End (E2E - Planned) and System UI tests**  
- **API Testing (Planned)**  
- **Page Object Model (POM) Structure**  
- **Logging and Screenshot Utilities**  
- **Allure Reports**  

## 🚀 Getting Started  

### Prerequisites  
- **Java 21+**  
- **Maven**  
- **Playwright**  
- **Node.js** (for Playwright dependencies)  

### Installation  
1. Clone the repository:  
   ```sh
   git clone <repo-url>
   cd <project-folder>
   ```
2. Install dependencies:  
   ```sh
   mvn clean install
   ```
3. Run tests:  
   ```sh
   mvn test
   ```

### Running Allure Reports  
1. Execute tests and generate reports:  
   ```sh
   mvn clean test
   ```
2. Serve the report:  
   ```sh
   allure serve target/allure-results
   ```

## 🛠️ Project Structure  

- **`src/main/java`** – Page Objects and Utilities  
- **`src/test/java`** – Test Cases  
- **`pom.xml`** – Maven dependencies  

## 🤖 Technologies  

- **Java 21**  
- **Playwright**  
- **TestNG**  
- **Maven**  
- **Allure**  
- **REST Assured (Planned)**  

## 📢 Contributing  

Contributions are welcome!  

