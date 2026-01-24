# CLAUDE.MD - AI Coding Playground

## Project Overview

This is a mobile-first fullstack application playground for exploring AI-assisted development. The project uses modern technologies for both frontend and backend development.

## Architecture

### Frontend (Planned)
- **Framework**: Angular 20 with Ionic Framework 8
- **Language**: TypeScript 5+
- **Mobile**: Progressive Web App with Capacitor
- **State Management**: NgRx, Angular Signals, RxJS
- **Status**: Not yet implemented

### Backend (In Progress)
- **Framework**: Spring Boot 4.0.2
- **Language**: Kotlin 2.2.21
- **JDK**: Java 24 (toolchain configured)
- **Build Tool**: Gradle with Kotlin DSL
- **Package**: `org.sualk.aiplayground`
- **Status**: Bootstrap complete with basic Spring Boot structure

### Database (Planned)
- **Primary**: PostgreSQL 18
- **ORM**: Spring Data JPA with Hibernate
- **Migrations**: Liquibase
- **Status**: Not yet configured

## Current Project Structure

```
ai-coding-playground/
├── README.md                  # Basic project readme
├── TECHNOLOGIES.md           # Detailed technology stack documentation
├── CLAUDE.MD                 # This file - AI assistant context
└── backend/
    └── aiplayground/
        ├── build.gradle.kts          # Gradle build configuration
        ├── settings.gradle.kts       # Gradle settings
        ├── gradle/                   # Gradle wrapper files
        ├── gradlew                   # Gradle wrapper script (Unix)
        ├── gradlew.bat              # Gradle wrapper script (Windows)
        └── src/
            ├── main/kotlin/org/sualk/aiplayground/
            │   └── AiplaygroundApplication.kt    # Main Spring Boot application
            └── test/kotlin/org/sualk/aiplayground/
                └── AiplaygroundApplicationTests.kt
```

## Current Dependencies

### Backend Spring Boot
- `spring-boot-starter-webmvc` - RESTful web services
- `kotlin-reflect` - Kotlin reflection support
- `jackson-module-kotlin` - JSON serialization for Kotlin
- `spring-boot-starter-webmvc-test` - Testing utilities
- `kotlin-test-junit5` - Kotlin testing with JUnit 5

## What's Implemented

1. **Backend Bootstrap**
   - Spring Boot 4 application initialized
   - Kotlin configuration with strict compiler options
   - Basic project structure following Spring Boot conventions
   - Test setup with JUnit 5

## What's Not Yet Implemented

1. **Frontend**: Angular/Ionic application not started
2. **Database**: PostgreSQL not configured, no JPA entities
3. **API Endpoints**: No REST controllers defined
4. **Security**: Spring Security not configured
5. **Docker**: No Docker Compose for local development
6. **Testing**: Limited test coverage
7. **API Documentation**: SpringDoc/Swagger not configured

## Development Workflow

### Backend Development
```bash
# Build the project
cd backend/aiplayground
./gradlew build

# Run the application
./gradlew bootRun

# Run tests
./gradlew test
```

### Git Workflow
- **Main Branch**: `main`
- **Current Status**: Clean working directory
- **Recent Commits**:
  - `a7a392d` - bootstrap spring boot backend
  - `512a05f` - first draft of technology document
  - `5bda200` - Initial commit

## Key Decisions & Context

1. **Java Version**: Using Java 24 (latest) via toolchain configuration
2. **Kotlin**: Version 2.2.21 with strict compiler options enabled
3. **Build Tool**: Gradle with Kotlin DSL preferred over Maven
4. **Spring Boot**: Using latest stable version 4.0.2
5. **Mobile-First**: Project designed with mobile as primary target

## Planned Features

Based on TECHNOLOGIES.md, the project plans to include:

- Mobile-first UI with Ionic Framework
- Progressive Web App capabilities
- Native mobile features (camera, geolocation, push notifications)
- RESTful API architecture
- JWT authentication with Spring Security
- PostgreSQL database with JPA/Hibernate
- API documentation with Swagger
- Comprehensive testing (Jest, Playwright, JUnit 5, Testcontainers)

## Notes for AI Assistants

- **Technology Stack**: See TECHNOLOGIES.md for full tech stack details
- **Backend Location**: All backend code is in `backend/aiplayground/`
- **Package Naming**: Use `org.sualk.aiplayground` for all Kotlin/Java classes
- **Build Commands**: Use `./gradlew` wrapper, not system Gradle
- **Frontend**: Not yet started - refer to TECHNOLOGIES.md for planned stack
- **Testing**: JUnit 5 is configured, use Kotlin test utilities
- **Code Style**: Follow Kotlin conventions, strict compiler options enabled

## Common Commands

```bash
# Backend - Build and run
cd backend/aiplayground
./gradlew clean build
./gradlew bootRun

# Backend - Testing
./gradlew test
./gradlew test --tests SpecificTest

# Backend - Check dependencies
./gradlew dependencies

# Git workflow
git status
git add .
git commit -m "message"
```

## Environment Setup

- **Platform**: macOS (Darwin 25.2.0)
- **Working Directory**: `/Users/wil/workspace/github/ai-coding-playground`
- **Repository**: Git initialized with GitHub remote (implied)

## Future Additions

When implementing new features, consider:

1. Adding Spring Data JPA and PostgreSQL configuration
2. Setting up Docker Compose for local PostgreSQL
3. Implementing REST controllers with proper error handling
4. Adding Spring Security with JWT
5. Setting up Liquibase for database migrations
6. Creating Angular/Ionic frontend application
7. Implementing proper logging with structured logs
8. Adding health checks and monitoring (Spring Actuator)
