# Chinook Security System - "Security & Privacy by Design" Project

This repository contains the source code for the final project of the **Software Systems Security** course. The goal was to develop a CRM (Customer Relationship Management) system based on the Chinook database, implementing rigorous "by design" security standards to protect data and access.

## 📋 Project Objectives

The system consists of a Full Stack architecture (Backend + Frontend) ensuring:

* **Secure Authentication:** Access via JWT Token and encrypted password management.
* **Data Protection:** Application of the **Need to Know** principle (data segregation based on role).
* **Privacy:** Basic GDPR compliance through data minimization.
* **Resilience:** Protection against common attacks (SQL Injection, mitigated Brute Force, mitigated Session Hijacking).

---

## 🛠 Technology Stack

### Backend
* **Language:** Java 17+
* **Framework:** Spring Boot 3
* **Security:** Spring Security 6, BCrypt (Hashing), JJWT (Token Management)
* **Logging:** SLF4J

### Frontend
* **Framework:** Vue.js 3 (Composition API)
* **State Management:** Pinia
* **Routing:** Vue Router
* **UI:** Bootstrap 5

### Database & Tools
* **Database:** MySQL 8 (Containerized with Docker)
* **IDE:** IntelliJ IDEA
* **Code Quality:** SonarQube for IDE

---

## 🔒 Implemented Security Features

### 1. Authentication & Password Management
* **BCrypt Hashing:** Passwords are never stored in plain text. BCrypt with automatic Salting is used.
* **JWT (JSON Web Token):** Stateless authentication with short-term expiration (5 minutes).
* **Mandatory Password Change:** On first access (or if using the default password `Jo5hu4!`), the system "locks" the user on a dedicated page until they set a secure password.
* **Password Policy:** Regex enforces complexity (Must contain Uppercase, Lowercase, Numbers, and Special Characters).

### 2. Access Control (RBAC)
The system distinguishes two specific roles:
* **MANAGER:** Can view the complete list of all customers. Has automatic Refresh Token permissions.
* **EMPLOYEE:** Can view **only** the customers assigned to them (Data Segregation).

### 3. Session Protection
* **Inactivity Logout:** The Frontend monitors mouse and keyboard activity; after **2 minutes** of inactivity, the user is automatically logged out.
* **Protected Navigation:** Vue Router Navigation Guards prevent access to protected pages without a valid token.

### 4. Monitoring
* **Logging:** Every access attempt (success or failure) is recorded in server logs with details. **Note:** Sensitive data (like passwords) is never logged.

---

## 🚀 Installation Instructions

### Prerequisites
* [Docker Desktop](https://www.docker.com/products/docker-desktop/) installed and running.
* [Node.js](https://nodejs.org/) (v18+) and NPM.
* [Java JDK 17+](https://www.oracle.com/java/technologies/downloads/).

### 1. Start the Database (Docker)
Run the MySQL container with the preloaded Chinook database:

```bash
docker run --name chinook-db -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=Chinook -p 3306:3306 -d mysql:8.0
