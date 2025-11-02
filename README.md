# Spring Boot Admin Management Application

A Spring Boot application demonstrating enterprise-level best practices and patterns using Java 8, including:
- Layered architecture (Controller → Service → Repository)
- DTO pattern with validation
- Global exception handling
- HTTP Basic Security
- Resilience4j circuit breaker implementation
- H2 in-memory database
- Integration testing with Spring Security

## Technologies
- Java 8
- Spring Boot 2.7.12
- Spring Security
- Spring Data JPA
- H2 Database
- Resilience4j
- JUnit 5
- Maven

## Prerequisites
- Java 8 or higher
- Maven 3.6 or higher

## Build & Run
```bash
mvn clean package
java -jar target/demo-springboot-app-0.0.1-SNAPSHOT.jar
```

## API Endpoints

### Admin Management
- `GET /api/admins` - List all admins
- `GET /api/admins/{id}` - Get admin by ID
- `POST /api/admins` - Create new admin
- `PUT /api/admins/{id}` - Update admin
- `DELETE /api/admins/{id}` - Delete admin

### Circuit Breaker Demo
- `GET /api/external` - Demonstrates circuit breaker fallback behavior

## Authentication
The application uses HTTP Basic Authentication:
- Username: `demo`
- Password: `demo123`

## Data Validation
Admin entity validates:
- Name (required)
- Email (required, must be valid email format)
- Age (required, must be non-negative)

## Testing
Run integration tests:
```bash
mvn test -Dtest=AdminControllerIntegrationTest
```

## Circuit Breaker Configuration
Resilience4j circuit breaker is configured in `application.properties` with:
- Fallback mechanism for external service calls
- Demonstration endpoint that triggers fallback behavior

## Project Structure
```
src/
├── main/
│   ├── java/
│   │   └── com/example/demo/
│   │       ├── config/
│   │       ├── controller/
│   │       ├── dto/
│   │       ├── exception/
│   │       ├── mapper/
│   │       ├── model/
│   │       ├── repository/
│   │       └── service/
│   └── resources/
│       └── application.properties
└── test/
    └── java/
        └── com/example/demo/
            └── controller/
                └── AdminControllerIntegrationTest.java
```