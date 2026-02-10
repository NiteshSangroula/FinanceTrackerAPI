# 💰 Finance Tracker API

A simple **Spring Boot backend** for managing bank-like accounts and money transactions  
(deposit, withdraw, and transfer) with validation, error handling, and unit testing.

This project is built as a **learning-focused fintech backend** to practice real-world backend  
design, clean architecture, and testing.

---

## 🚀 Features

### Account Management
- Create new accounts  
- View all accounts  
- Delete accounts  

### Transactions
- Deposit money into an account  
- Withdraw money with **insufficient balance protection**  
- Transfer money between accounts (**atomic & transactional**)  
- View transaction history  

### Reliability & Safety
- Input validation using **Jakarta Validation**  
- Custom exceptions:
  - `AccountNotFoundException`
  - `InsufficientBalanceException`
- **Transactional integrity** for transfers  
- **Unit tests** for service layer logic  

---

## 🛠 Tech Stack

- **Java 21**  
- **Spring Boot**  
- **Spring Data JDBC**  
- **H2 Database** (development)  
- **JUnit 5 + Mockito** (testing)  
- **Maven**

---

## 📂 Project Structure

```bash
src/main/java
 ├── controller        # REST controllers
 ├── service           # Business logic
 ├── repository        # CRUD repositories
 ├── entity            # Database entities
 ├── dto               # Request/response models
 └── exception         # Custom exceptions
```

---

## ▶️ Running Locally

### 1. Clone the repository

```bash
git clone https://github.com/NiteshSangroula/FinanceTrackerAPI.git
cd FinanceTrackerAPI
```

### 2. Run the application

```bash
./mvnw spring-boot:run
```

App starts at:

```bash
http://localhost:8080
```

---

## 🗄 Database

- Default: **H2 in-memory database**

H2 Console:

```bash
http://localhost:8080/h2-console
```

Typical config:

```bash
JDBC URL: jdbc:h2:mem:testdb
User: sa
Password: (empty)
```

---

## 📡 API Endpoints

### Account APIs

**Create account**
```bash
POST /api/accounts
```

**Get all accounts**
```bash
GET /api/accounts
```

**Get account by id**
```bash
GET /api/accounts/{id}
```

**Delete account**
```bash
DELETE /api/accounts/{id}
```

---

### Transaction APIs

**Deposit**
```bash
POST /api/transactions/deposit
```

**Withdraw**
```bash
POST /api/transactions/withdraw
```

**Transfer**
```bash
POST /api/transactions/transfer
```

**Get all transactions**
```bash
GET /api/transactions
```

**Get transaction by id**
```bash
GET /api/transactions/{id}
```

---

## 🧪 Testing

Run unit tests:

```bash
./mvnw test
```

Tests cover:

- Deposit success & failure  
- Withdraw success & insufficient balance  
- Transfer success & edge cases  
- Account not found scenarios  

---

## 📈 Current Status

✔ Core banking operations implemented  
✔ Validation and exception handling  
✔ Unit tested service layer  

### 🔜 Planned Improvements

- Global exception handler (`@RestControllerAdvice`)  
- Integration tests  
- Pagination & filtering  
- Swagger/OpenAPI documentation  
- PostgreSQL + Docker support  
- Basic authentication & security  

---

## 🎯 Learning Goals

- Writing **clean service-layer business logic**  
- Designing **safe financial transactions**  
- Practicing **unit testing with Mockito**  
- Building a **realistic backend structure**  

---

## 👤 Author

**Nitesh Sangroula**

GitHub:  
https://github.com/NiteshSangroula
