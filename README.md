# JobConnect Backend

A RESTful backend API for a job portal application built with **Spring Boot 3** and **Java 21**. JobConnect enables job seekers and employers to connect through a secure, scalable platform with role-based access, JWT authentication.

---

## Live Demo

🚀 **Backend API (Railway):** [https://jobconnect-backend-production.up.railway.app](https://jobconnect-backend-production.up.railway.app)  
🌐 **Frontend App (Netlify):** [https://jobconnect-project1.netlify.app](https://jobconnect-project1.netlify.app)  
📄 **Swagger UI:** [https://jobconnect-backend-production.up.railway.app/swagger-ui/index.html](https://jobconnect-backend-production.up.railway.app/swagger-ui/index.html)

---

## Demo Videos

Three walkthrough videos are included in the `Testing videos/` folder at the root of this repository, demonstrating the full application in action.

### 🎥 Employer Workflow
> Shows employer registration, login, job posting creation, and managing job applications.

https://github.com/poojavijayakumar95-pixel/jobconnect-backend/raw/main/Testing%20videos/employeer.mp4

---

### 🎥 Job Seeker Workflow
> Shows job seeker registration, job search, and applying to a job.

https://github.com/poojavijayakumar95-pixel/jobconnect-backend/raw/main/Testing%20videos/jobseeker%20testing%20video.mp4

---

### 🎥 Final End-to-End Testing
> Full integration test covering both roles, the complete job application lifecycle, and real-time SMS notification via Twilio.

https://github.com/poojavijayakumar95-pixel/jobconnect-backend/raw/main/Testing%20videos/Final%20Testing%20.mp4

> **Note:** GitHub does not preview `.mp4` files inline. Click the links above to download and play, or clone the repo and open the files from the `Testing videos/` folder.

---

## Screenshots

### SMS Notification (Twilio)
When an employer updates an application status to **HIRED**, a real-time SMS is sent to the job seeker via the Twilio API.

![SMS Notification](Testing%20videos/sms_notification.jpeg)

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
- **Role-Based Access Control** – Separate roles and permissions for different user types
- **Job Postings** – Employers can create, update, and delete job listings
- **Job Applications** – Job seekers can browse and apply to open positions
- **Input Validation** – Request-level validation using Spring Validation
- **API Documentation** – Interactive Swagger UI via SpringDoc OpenAPI


---

## Prerequisites

Before running this project, make sure you have the following installed:

- Java 21+
- Maven 3.8+ (or use the included `./mvnw` wrapper)
- MySQL 8.x
- A [Twilio](https://www.twilio.com/) account (for SMS)

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
├── Testing videos/
│   ├── employeer.mp4                  # Employer workflow demo
│   ├── Final Testing .mp4             # Full end-to-end test demo
│   ├── jobseeker testing video.mp4    # Job seeker workflow demo
│   └── sms_notification.jpeg          # Twilio SMS notification screenshot
├── .mvn/wrapper/
├── jobconnect.pdf                     # Testing & QA report
├── pom.xml
├── mvnw
├── mvnw.cmd
└── README.md
```

---

## Testing & Quality Assurance

> 📄 The full testing report with all test case details and result screenshots is available in **[`jobconnect.pdf`](./jobconnect.pdf)** at the root of this repository.

### Unit Testing — JUnit 5 & Mockito

Unit tests were implemented across all three primary architectural layers to ensure backend reliability and isolate business logic.

#### A. Repository Layer — `JobRepositoryTest`

- **Annotation:** `@DataJpaTest` — spins up an isolated in-memory H2 database, keeping the production MySQL database untouched.
- **Test:** Verified the `findByTitleContainingIgnoreCaseOrLocationContainingIgnoreCase` custom query. Saved mock data to H2 and asserted the search method returned correctly filtered results.
- **Result:** ✅ 1/1 — `testSearchJobsByKeyword()` passed in 1.135s

#### B. Service Layer — `ApplicationServiceTest`

- **Annotation:** `@ExtendWith(MockitoExtension.class)` — repositories mocked with `@Mock`, injected into `ApplicationService` via `@InjectMocks`.
- **Test:** Tested the rule preventing duplicate job applications. Mockito simulated an existing application and asserted the service threw `RuntimeException("You have already applied for this job")`. Verified via `verify(repository, never()).save(...)` that the database was never touched.
- **Result:** ✅ 1/1 — `testApplyForJob_AlreadyApplied_ThrowsException()` passed in 3.559s

#### C. Controller Layer — `JobControllerTest`

- **Annotation:** `@WebMvcTest` — loads only the web layer. Services and JWT utilities bypassed via `@MockBean`. Uses `MockMvc` + `@WithMockUser` from `spring-security-test`.
- **Tests:** Tested the secured `POST /api/jobs/create` endpoint:
  - `testCreateJob_AsEmployer_ReturnsCreated()` — Simulated `EMPLOYER` role → **HTTP 201 Created** ✅
  - `testCreateJob_AsJobSeeker_ReturnsForbidden()` — Simulated `JOB_SEEKER` role → **HTTP 403 Forbidden** ✅ (confirms JWT RBAC is enforced)
- **Result:** ✅ 2/2 passed in 1.199s

#### Overall JUnit Results

| Test Class | Runs | Errors | Failures | Status |
|---|---|---|---|---|
| `JobconnectApplicationTests` | 1/1 | 0 | 0 | ✅ Pass |
| `JobRepositoryTest` | 1/1 | 0 | 0 | ✅ Pass |
| `ApplicationServiceTest` | 1/1 | 0 | 0 | ✅ Pass |
| `JobControllerTest` | 2/2 | 0 | 0 | ✅ Pass |
| **Total** | **5/5** | **0** | **0** | ✅ **All Pass** |

> All 5 tests completed in **28.95 seconds** with zero errors and zero failures.

---

### End-to-End (E2E) Testing

Manual E2E testing was performed to validate the full data lifecycle across the React frontend, Spring Boot API, MySQL database, and Twilio API.

#### Workflow 1 — Authentication & Routing
- A user registers and logs in via the React interface.
- The backend hashes the password, saves the user to MySQL, and returns a JWT containing the user's role.
- The React `AuthContext` decodes the token and routes **Employers** to the Employer Dashboard and **Job Seekers** to the Job Search page. ✅

#### Workflow 2 — Core Application Lifecycle
- An Employer creates a job posting; a Job Seeker searches for and applies to it.
- Data flows from React forms via Axios → Spring Boot controllers → persisted in MySQL relational tables.
- The UI correctly prevents duplicate applications and reflects live database state. ✅

#### Workflow 3 — Third-Party API (Twilio SMS)
- The Employer updates an application status from `PENDING` to `HIRED`.
- Spring Boot updates the database → `SmsService` is triggered → Twilio sends a **real-time SMS** to the job seeker's verified mobile number. ✅

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