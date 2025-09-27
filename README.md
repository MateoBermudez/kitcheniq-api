# KitchenIQ API

KitchenIQ Backend is the core back-office system for small and medium-sized restaurants, enabling efficient management of inventory, suppliers, finances, staff, and orders. By digitizing and automating key processes, it streamlines operations, supports data‑driven decisions, and boosts profitability.

Status: Work in progress (WIP)

- Language: Java 17
- Framework: Spring Boot 3.5.x
- Build tool: Maven
- Database: PostgreSQL

---

## Table of Contents

- [Key Features](#key-features)
- [Architecture and Tech Stack](#architecture-and-tech-stack)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Local Setup](#local-setup)
  - [Configuration](#configuration)
  - [Run the Application](#run-the-application)
  - [Build and Package](#build-and-package)
  - [Run Tests](#run-tests)
- [Project Structure](#project-structure)
- [Useful Resources](#useful-resources)
- [Roadmap](#roadmap)
- [Contributing](#contributing)
- [Credits](#credits)
- [License](#license)

---

## Key Features

Current and planned capabilities include:

- Inventory management: products, stock levels, categories, and units
- Supplier management: vendors, purchase orders, pricing
- Order management: internal orders and fulfillment workflows
- Finance support: costs, margins, and basic reporting (planned)
- Staff management: roles, permissions, and access control
- Authentication and authorization with Spring Security and JWT
- RESTful APIs following conventional HTTP semantics
- PostgreSQL persistence via Spring Data JPA/JDBC
- Developer experience: hot reload with Spring Boot DevTools

Note: The project is under active development; some features are planned and not yet available.

---

## Architecture and Tech Stack

- Spring Boot 3.5.x (Web, Security, Data JPA/JDBC)
- PostgreSQL driver
- JWT (io.jsonwebtoken:jjwt-*) for token-based auth
- Lombok for boilerplate reduction
- Maven build with spring-boot-maven-plugin

Key Maven dependencies (from pom.xml):
- spring-boot-starter-web
- spring-boot-starter-security
- spring-boot-starter-data-jpa / data-jdbc / jdbc
- postgresql
- io.jsonwebtoken (jjwt-api, jjwt-impl, jjwt-jackson)
- spring-boot-devtools (runtime)
- lombok (annotation processor)
- spring-boot-starter-test, spring-security-test (test scope)

Java version: 17

---

## Getting Started

### Prerequisites

- Java 17 (JDK 17)
- Maven 3.9+ 
- PostgreSQL 13+ (or compatible)
- Git
- Supabase

### Local Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/MateoBermudez/kitcheniq-api.git
   cd kitcheniq-api
   ```

2. Data base local initialization (optional)
   [Link Supabase](https://www.Supabase.com)
   

### Configuration

Configure your application properties via one of the following:

Option A) application.properties (src/main/resources/application.properties)
```properties
# Server
server.port=5000

# DataSource
spring.datasource.url=jdbc:postgresql://localhost:5000/kitcheniq
spring.datasource.username=kitcheniq_user
spring.datasource.password=changeme

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Security / JWT (adjust these to match your security config)
jwt.secret=replace-with-strong-secret
jwt.expiration-ms=86400000
```

Option B) Environment variables (example)
```bash
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5000/kitcheniq
export SPRING_DATASOURCE_USERNAME=kitcheniq_user
export SPRING_DATASOURCE_PASSWORD=changeme
export JWT_SECRET=replace-with-strong-secret
export JWT_EXPIRATION_MS=86400000
```

Notes:
- Property names for JWT may differ depending on the final configuration class. Use the names your code expects (e.g., `jwt.secret`, `security.jwt.secret`, etc.).
- For development, `spring.jpa.hibernate.ddl-auto=update` is acceptable. For production, use migrations and a safer strategy.

### Run the Application

Using Maven (recommended for dev):
```bash
mvn spring-boot:run
```

The API will start on:
- http://localhost:5000 (default)

### Build and Package

Build a runnable JAR:
```bash
mvn clean package
```

Run the packaged application:
```bash
java -jar target/KitchenIQ-0.0.1-SNAPSHOT.jar
```

### Run Tests

```bash
mvn test
```

---

## Project Structure

Typical Spring Boot layout:
```
kitcheniq-api/
├─ src/
│  ├─ main/
│  │  ├─ java/            # Application code (controllers, services, repositories, config, domain)
│  │  └─ resources/
│  │     ├─ application.properties (or application.yml)
│  └─ test/
│     └─ java/            # Unit and integration tests
├─ pom.xml                # Maven build configuration
└─ README.md
```

---

## Useful Resources

- Product workspace and documentation: https://www.notion.so/KitchenIQ-24e3e7aa9597800d98e2e1254efa3b62
- Frontend repository (UI): https://github.com/MateoBermudez/kitcheniq-ui
- Spring Boot Reference: https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/
- Spring Security Reference: https://docs.spring.io/spring-security/reference/
- JJWT Documentation: https://github.com/jwtk/jjwt

API documentation (e.g., Swagger/OpenAPI) will be added as the project matures.

---

## Roadmap

- Core domain modeling (inventory, suppliers, orders, staff)
- Authentication/Authorization flows and role-based policies
- CRUD endpoints and validation
- Basic reporting and dashboards (finance/operations)
- Observability (metrics, logs, tracing)
- OpenAPI/Swagger documentation
- CI/CD pipeline and deployment guidelines
- Database migrations (e.g., Flyway/Liquibase)

---

## Credits

- Maintainer: @MateoBermudez
- Contributors: @Danieloid3, @EmmanuelArangoV

Thank you to everyone helping build KitchenIQ!

---

## License

TBD. Licensing information will be added as the project approaches a stable release.
