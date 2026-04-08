# Finance Data Processing and Access Control Backend

## Overview
This project is a robust Spring Boot backend developed for a Finance Dashboard assignment. It manages financial records (Income/Expenses) with a focus on granular **Role-Based Access Control (RBAC)**, data aggregation for analytical summaries, and secure stateless authentication using **JWT (JSON Web Tokens)**.

The system is designed to handle different levels of user access—**Admin**, **Analyst**, and **Viewer**—ensuring that data integrity and privacy are maintained according to organizational roles.

## Tech Stack
- **Core Framework**: Spring Boot 3.x
- **Security**: Spring Security (RBAC + JWT)
- **Persistence**: Spring Data JPA with MySQL (or H2 for local dev)
- **Validation**: Jakarta Validation (Hibernate Validator)
- **Utilities**: Lombok, Project JJWT (v0.12.5)

---

## Core Requirements & Implementation

### 1. User and Role Management
- **Security Logic**: Implemented using Spring Security's `UserDetailsService`.
- **Roles**:
    - `ADMIN`: Full management access (CRUD on all users and records).
    - `ANALYST`: View access to detailed records and the analytical dashboard.
    - `VIEWER`: Restricted to viewing only their own or basic summary data (depending on system scope).
- **Status**: Supports `active` and `inactive` flags on user accounts to prevent unauthorized access.

### 2. Financial Records Management
- **Entities**: The `FinancialRecord` entity handles `Amount`, `Type` (Income/Expense), `Category`, `Date`, and detailed `Description`.
- **Filtering**: Advanced filtering support via Spring Data JPA `Specifications` for searching by date range, category, and type.
- **Access Control**: Record modification is restricted exclusively to Admin roles in this implementation.

### 3. Dashboard Summary APIs
- **Aggregation Logic**: The `DashboardService` calculates real-time summaries:
    - **Total Income / Total Expenses**.
    - **Net Balance**.
    - **Category-wise Breakdown**: Grouped results using Java Streams for performant categorization.
- **Endpoint**: `GET /api/dashboard/summary`

### 4. Security & Access Control
- **Stateful vs Stateless**: Used **JWT** for stateless authentication, making the backend scalable and suited for modern frontend integrations.
- **Method Level Security**: Applied `@PreAuthorize` annotations on controller endpoints to enforce role checks at the entry point.
- **CSRF & CORS**: CSRF is disabled for the REST API, and a global CORS policy is configured to allow dashboard frontend integration.

---

## Prerequisites
- **JDK 17** or higher
- **Maven 3.6+**
- **MySQL** (configured in `application.properties`)

## Getting Started

1. **Clone the repository**:
   ```bash
   git clone <repository-link>
   ```

2. **Configure Database**:
   Update `src/main/resources/application.properties` with your database credentials.

3. **Build the project**:
   ```bash
   ./mvnw clean compile
   ```

4. **Run the application**:
   ```bash
   ./mvnw spring-boot:run
   ```

## Assumptions & Design Choices
- **Authentication**: JWT signing uses a Base64-encoded HS256 secret.
- **Date Handling**: All dates are handled as `LocalDate` (ISO format) for time-zone consistency.
- **Scalability**: Used Constructor Injection in all Security configurations and Controllers to improve testability and follow modern Spring standards.
- **Error Handling**: Implemented custom `AuthEntryPointJwt` to provide clean UNAUTHORIZED responses for failed token validation.

---
© 2026 Ganesh Kute - Internship Assignment Submission
