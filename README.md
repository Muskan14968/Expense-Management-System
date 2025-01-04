# Expense Management System

## Overview
The Expense Management System is a comprehensive full-stack application designed to empower individuals in managing their personal finances effectively. This application provides users with an intuitive interface to track income and expenses, set financial goals, and generate insightful reports, enabling better financial decision-making. By combining robust backend services with an interactive frontend, the system ensures seamless user experience and operational efficiency.

## Features
### Backend
- Developed using **Java Spring Boot**.
- Implements features such as:
  - User authentication and authorization.
  - Multi-role functionality with secure access for both users and administrators.
  - Budget management with dynamic budget adjustment based on spending patterns.
  - Goal setting for financial planning, including alerts for nearing goals.
  - Transaction tracking and categorization with detailed breakdowns.
  - Monthly expense and income summary reports.
  - Notification system for activities made.
  - Advanced management capabilities including:
    - Search, filter, and pagination.
    - Exporting data as PDF file.

### Frontend
- Built with **React.js**.
- Features include:
  - Interactive dashboards with charts and statistics using **Chart.js**.
  - Profile management and password reset functionality.
  - Real-time validation and feedback on forms.
  - Light and dark mode themes for enhanced usability.

### Key Functionalities
- **User Features:**
  - Intuitive dashboards with:
    - **Dashboard**: Displays an overview of expenses, income, and financial goals.
    - **Transaction History**: A detailed list of past transactions with filters.
    - **New Transaction**: Add a new transaction with categories and details.
    - **Saved Transactions**: Quick access to frequently used transactions.
    - **Goal Setting**: Define and monitor financial goals with visual progress indicators.
    - **Statistics**: Visualize spending patterns and income using charts.
    - **Settings**: Manage profile picture, and change password.
    - **Logout**: Securely log out of the system.

- **Administrator Features:**
  - Comprehensive management tools including:
    - **Transactions**: View and manage all user transactions.
    - **Users**: Administer user accounts and roles.
    - **Categories**: Update and delete categories.
    - **New Category**: Create custom categories for transactions.
    - **Settings**: Manage profile picture, and change password.
    - **Logout**: Securely log out of the system.

## Tools and Technologies
### Backend (Spring Boot with JDK 17 or above)
1. **Java Development Kit (JDK):**
   - JDK 17 or above (17 is a Long-Term Support version).
   - [Download JDK 17](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html).

2. **IntelliJ IDEA (IDE for Backend Development):**
   - IntelliJ IDEA Community or Ultimate Edition (Community is free; Ultimate offers more advanced features).
   - Version: Latest stable version, e.g., 2023.2.x.
   - [Download IntelliJ IDEA](https://www.jetbrains.com/idea/download/).

3. **Spring Initializr:**
   - Used to generate a Spring Boot starter project.
   - Can be accessed directly through IntelliJ or online at [start.spring.io](https://start.spring.io/).

### Database

**MySQL Community Server:**
   - Version: 8.0.x.
   - [Download MySQL](https://dev.mysql.com/downloads/mysql/).
   - **MySQL Workbench** (optional but recommended for managing MySQL databases):
     - Version: 8.0.x.
     - [Download MySQL Workbench](https://dev.mysql.com/downloads/workbench/).

### Frontend (React with npm)
1. **Node.js (includes npm):**
   - Version: 18.x LTS (recommended for stability).
   - [Download Node.js](https://nodejs.org/en/download/).

2. **Visual Studio Code (VS Code):**
   - Version: Latest stable version, e.g., 1.83.x.
   - [Download VS Code](https://code.visualstudio.com/Download).

3. **Chart.js:**
   - Used for rendering dynamic and interactive charts in the frontend.
   - Install via npm: `npm install chart.js`.

### API Testing (Optional but Recommended)
1. **Postman (for testing APIs):**
   - Version: Latest stable version.
   - [Download Postman](https://www.postman.com/downloads/).

### Additional Tools
**Git (version control):**
   - Version: Latest stable version, e.g., 2.42.x.
   - [Download Git](https://git-scm.com/downloads).

## Installation
### Prerequisites
- Java 17 or higher.
- Maven 3.6+.
- Node.js 16+.
- MySQL database.

### Steps
1. Install the required tools as listed above.
2. Clone the repository:
   ```bash
   git clone https://github.com/chavapardhakalyan/Expense_Management_System.git
   ```

3. Set up the backend:
   ```bash
   cd backend
   mvn clean install
   ```

4. Set up the frontend:
   ```bash
   cd frontend
   npm install
   npm start
   ```

5. Access the application at `http://localhost:3000`.

## Configuration
- Update the `application.properties` file in the backend to configure the database:
  ```properties
  spring.datasource.url=jdbc:mysql://localhost:3306/expense_tracker
  spring.datasource.username=<your-username>
  spring.datasource.password=<your-password>
  ```

## Usage
- Register as a new user.
- Log in to access your dashboard.
- Add, edit, or delete transactions.
- Set budgets and financial goals.
- Generate monthly financial reports.
- Share reports and receive notifications.

## License
This project is licensed under the MIT License. See the `LICENSE` file for details.

## Summary
The Expense Management System is a user-friendly platform offering a wide range of functionalities to simplify personal finance management. With its advanced features for both users and administrators, the system ensures a comprehensive approach to tracking and optimizing financial health. By leveraging cutting-edge technologies like Spring Boot and React.js, the application guarantees performance, scalability, and a seamless user experience.
