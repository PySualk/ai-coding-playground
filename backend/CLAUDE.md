# Backend - Spring Boot + Kotlin Application

## Architecture

- **Framework**: Spring Boot 4.0.2
- **Language**: Kotlin 2.2.21
- **JDK**: Java 21 (toolchain configured)
- **Build Tool**: Gradle with Kotlin DSL
- **Package**: `org.sualk.aiplayground`
- **Status**: User CRUD API implemented with layered architecture
- **Architecture**: domain/ (entities, repositories) → application/ (services, controllers, DTOs, exceptions, handlers)

### Database

- **Primary**: PostgreSQL 18
- **ORM**: Spring Data JPA with Hibernate
- **Migrations**: Liquibase
- **Status**: Configured with User entity and migrations

### Environment Variables

Required for local development:
- `DATABASE_URI` - PostgreSQL connection string (default: `postgresql://devuser:devpassword@localhost:5432/aiplayground`)

Docker Compose sets these automatically. For local development, uses `application.yml` defaults.

## Project Structure

```
backend/
├── Dockerfile                    # Multi-stage Docker build
├── .dockerignore                 # Backend Docker exclusions
├── build.gradle.kts              # Gradle build configuration
├── settings.gradle.kts           # Gradle settings
├── gradle/                       # Gradle wrapper files
├── gradlew                       # Gradle wrapper script (Unix)
├── gradlew.bat                   # Gradle wrapper script (Windows)
└── src/
    ├── main/
    │   ├── kotlin/org/sualk/aiplayground/
    │   │   ├── AiplaygroundApplication.kt    # Main Spring Boot application
    │   │   ├── domain/                       # Entities and repositories
    │   │   │   ├── User.kt
    │   │   │   └── UserRepository.kt
    │   │   └── application/                  # Services, controllers, DTOs
    │   │       ├── services/
    │   │       │   ├── UserService.kt
    │   │       │   └── UserServiceImpl.kt
    │   │       ├── controllers/
    │   │       │   └── UserController.kt
    │   │       ├── dto/
    │   │       │   ├── CreateUserRequest.kt
    │   │       │   ├── UpdateUserRequest.kt
    │   │       │   └── UserResponse.kt
    │   │       ├── exceptions/
    │   │       │   ├── ResourceNotFoundException.kt
    │   │       │   └── ErrorResponse.kt
    │   │       └── handlers/
    │   │           └── GlobalExceptionHandler.kt
    │   └── resources/
    │       ├── application.yml               # Application configuration
    │       └── db/changelog/                 # Liquibase migrations
    │           ├── db.changelog-master.yml
    │           └── migrations/
    │               └── 001_create_users_table.yml
    └── test/kotlin/org/sualk/aiplayground/
        ├── AiplaygroundApplicationTests.kt   # Integration tests
        ├── application/
        │   ├── services/
        │   │   └── UserServiceImplTest.kt    # Unit tests
        │   └── controllers/
        │       └── UserControllerIntegrationTest.kt  # Integration tests
        └── domain/
            └── UserRepositoryTest.kt         # Repository tests
```

## Key Dependencies

- Spring Boot 4.0.2
- Kotlin 2.2.21
- Spring Data JPA + Hibernate
- Liquibase (database migrations)
- PostgreSQL driver
- MockK (Kotlin testing)
- Testcontainers (integration tests)

**Critical**: Use `com.fasterxml.jackson.module:jackson-module-kotlin` (not `tools.jackson.module`)

See `build.gradle.kts` for complete dependency list.

## What's Implemented

- Spring Boot 4 application initialized
- Kotlin configuration with strict compiler options
- Basic project structure following Spring Boot conventions
- Test setup with JUnit 5
- PostgreSQL 18 database with Liquibase migrations
- User entity with JPA annotations and audit fields
- UserRepository with Spring Data JPA
- User CRUD REST API at /api/users (create, read, update, soft delete)
- DTOs for request/response separation
- Global exception handling with consistent error responses
- Comprehensive testing (10 unit tests + 14 integration tests)

## API Endpoints

### User CRUD
Base URL: `http://localhost:8080/api/users`

```bash
# Create user
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"name":"John Doe","email":"john@example.com"}'

# Get all users
curl http://localhost:8080/api/users

# Get user by ID
curl http://localhost:8080/api/users/1

# Update user
curl -X PUT http://localhost:8080/api/users/1 \
  -H "Content-Type: application/json" \
  -d '{"name":"Jane Doe","email":"jane@example.com"}'

# Soft delete user
curl -X DELETE http://localhost:8080/api/users/1
```

**Note**: Soft delete sets `deletedAt` timestamp; deleted users excluded from queries.

## What's Not Yet Implemented

1. **Security**: Spring Security not configured (no authentication/authorization)
2. **API Documentation**: SpringDoc/Swagger not configured
3. **Monitoring**: Spring Actuator not enabled
4. **Logging**: Structured logging not configured
5. **Caching**: Redis or other caching not configured
6. **CORS**: CORS configuration for frontend integration not set up

## Development Commands

```bash
# Build the project
./gradlew build

# Run the application
./gradlew bootRun

# Run all tests
./gradlew test

# Run specific test class
./gradlew test --tests "UserServiceImplTest"

# Clean and build
./gradlew clean build

# Check dependencies
./gradlew dependencies

# Run with debug (port 5005)
./gradlew bootRun --debug-jvm
```

## Key Decisions & Context

1. **Java Version**: Using Java 21 (LTS) via toolchain configuration
2. **Kotlin**: Version 2.2.21 with strict compiler options enabled
3. **Build Tool**: Gradle with Kotlin DSL preferred over Maven
4. **Spring Boot**: Using latest stable version 4.0.2
5. **Database**: PostgreSQL 18 for production-grade features
6. **Migrations**: Liquibase for version-controlled schema changes
7. **Architecture**: Layered architecture (domain → application → infrastructure)
8. **Testing**: JUnit 5 + MockK + Testcontainers

## Notes for AI Assistants

- **Location**: All backend code is in `backend/`
- **Package Naming**: Use `org.sualk.aiplayground` for all Kotlin/Java classes
- **Architecture**: Use layered architecture: domain/ → application/ → infrastructure/
- **Jackson Dependency**: Must use `com.fasterxml.jackson.module:jackson-module-kotlin` (not `tools.jackson.module`)
- **UserRepository**: Methods like `findByEmail()` return `Optional<T>`, not nullable `T?`
- **MockMvc Setup**: Without `@AutoConfigureMockMvc`, use `MockMvcBuilders.webAppContextSetup(webApplicationContext).build()`
- **Testcontainers**: Integration tests require Docker running - will fail in environments without Docker
- **Kotlin Unit Testing**: Use MockK (`io.mockk:mockk`) for Kotlin-friendly mocking with Spring Boot
- **Backend Testing**: Spring Boot tests with database require `@Testcontainers` annotation and dynamic property configuration
- **Build Commands**: Use `./gradlew` wrapper for all operations
- **Code Style**: Follow Kotlin conventions for backend
- **CI Environment**: Gradle needs `--no-daemon` flag for GitHub Actions
- **MCP Servers**:
  - `postgres` - Database inspection and queries (connects to local PostgreSQL)
- **Port**: Application runs on :8080
- **Database Migrations**: Run automatically on startup via Liquibase

## Common Issues & Solutions

### Port Conflicts
- Backend API uses port 8080
- Check with: `lsof -i :8080`
- Kill process if needed: `kill -9 $(lsof -t -i:8080)`

### Database Connection Failed
- Ensure postgres container is healthy: `docker-compose ps`
- Wait for healthcheck: postgres must be ready before backend starts
- Check logs: `docker-compose logs postgres`
- Verify connection string in `application.yml`

### Testcontainers Failing
- Integration tests require Docker running
- Grant Docker socket access: `/var/run/docker.sock`
- CI uses `--no-daemon` flag for Gradle

### Gradle Issues
```bash
# Clear Gradle cache
./gradlew clean

# Refresh dependencies
./gradlew build --refresh-dependencies

# Stop Gradle daemon
./gradlew --stop
```

## Future Additions

When implementing new backend features, consider:

1. Adding Spring Security with JWT authentication
2. Implementing proper logging with structured logs
3. Adding health checks and monitoring (Spring Actuator)
4. Configuring CORS for frontend integration
5. Adding SpringDoc/Swagger for API documentation
6. Implementing additional domain entities and relationships
7. Adding caching with Redis
8. Implementing background job processing
9. Adding validation with Bean Validation API
10. Implementing pagination and sorting for list endpoints
