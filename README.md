# JobConnect Backend

A RESTful backend API for a job portal application built with **Spring Boot 3** and **Java 21**. JobConnect enables job seekers and employers to connect through a secure, scalable platform with role-based access, JWT authentication, and OTP-based verification.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.5 |
| Security | Spring Security + JWT (jjwt 0.11.5) |
| ORM | Spring Data JPA (Hibernate) |
| Database | MySQL |
| SMS | Twilio SDK 9.14 |
| API Docs | SpringDoc OpenAPI (Swagger UI) |
| Build Tool | Maven (Maven Wrapper included) |
| Monitoring | Spring Boot Actuator |

---

## Features

- **User Registration & Login** – Secure sign-up and login for job seekers and employers
- **JWT Authentication** – Stateless token-based auth with access token support
- **SMS Notification** – Send job status via Twilio SMS
- **Role-Based Access Control** – Separate roles and permissions for different user types
- **Job Postings** – Employers can create, update, and delete job listings
- **Job Applications** – Job seekers can browse and apply to open positions
- **Input Validation** – Request-level validation using Spring Validation
- **API Documentation** – Interactive Swagger UI via SpringDoc OpenAPI
- **Health Monitoring** – Actuator endpoints for app health and metrics

---

## Prerequisites

Before running this project, make sure you have the following installed:

- Java 21+
- Maven 3.8+ (or use the included `./mvnw` wrapper)
- MySQL 8.x
- A [Twilio](https://www.twilio.com/) account (for SMS Notification)

---

## Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/poojavijayakumar95-pixel/jobconnect-backend.git
cd jobconnect-backend
```

### 2. Configure the Database

Create a MySQL database:

```sql
CREATE DATABASE jobconnect;
```

### 3. Set Up Application Properties

Edit `src/main/resources/application.properties` (or `application.yml`) with your configuration:

```properties
# Database
spring.datasource.url=jdbc:mysql://localhost:3306/jobconnect
spring.datasource.username=your_mysql_username
spring.datasource.password=your_mysql_password
spring.jpa.hibernate.ddl-auto=update

# JWT
jwt.secret=your_jwt_secret_key
jwt.expiration=86400000

# Twilio (OTP)
twilio.account-sid=your_twilio_account_sid
twilio.auth-token=your_twilio_auth_token
twilio.phone-number=your_twilio_phone_number
```

### 4. Build and Run

Using the Maven wrapper:

```bash
# On Linux/Mac
./mvnw spring-boot:run

# On Windows
mvnw.cmd spring-boot:run
```

Or build a JAR and run it:

```bash
./mvnw clean package
java -jar target/jobconnect-0.0.1-SNAPSHOT.jar
```

The server starts at **`http://localhost:8080`** by default.

---

## API Documentation

Once the application is running, access the interactive Swagger UI at:

```
http://localhost:8080/swagger-ui/index.html
```

The OpenAPI spec (JSON) is available at:

```
http://localhost:8080/v3/api-docs
```

---

## Health & Monitoring

Spring Boot Actuator exposes health and metrics endpoints:

```
http://localhost:8080/actuator/health
http://localhost:8080/actuator/info
```

---

## Project Structure

```
jobconnect-backend/
├── src/
│   ├── main/
│   │   ├── java/com/example/jobconnect/
│   │   │   ├── controller/       # REST controllers
│   │   │   ├── service/          # Business logic
│   │   │   ├── repository/       # JPA repositories
│   │   │   ├── model/            # Entity classes
│   │   │   ├── dto/              # Data Transfer Objects
│   │   │   ├── security/         # JWT & Spring Security config
│   │   │   └── JobconnectApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/com/example/jobconnect/
├── .mvn/wrapper/
├── pom.xml
├── mvnw
├── mvnw.cmd
└── README.md
```

---

## Running Tests

```bash
./mvnw test
```

---

## Dependencies Overview

| Dependency | Purpose |
|---|---|
| `spring-boot-starter-web` | REST API layer |
| `spring-boot-starter-data-jpa` | Database ORM |
| `spring-boot-starter-security` | Authentication & authorization |
| `spring-boot-starter-validation` | Input validation |
| `spring-boot-starter-actuator` | Health monitoring |
| `mysql-connector-j` | MySQL JDBC driver |
| `jjwt-api / jjwt-impl / jjwt-jackson` | JWT token generation & parsing |
| `twilio` | SMS service |
| `springdoc-openapi-starter-webmvc-ui` | Swagger UI & API docs |

---

## Author

**Pooja Vijayakumar**  
GitHub: [@poojavijayakumar95-pixel](https://github.com/poojavijayakumar95-pixel)