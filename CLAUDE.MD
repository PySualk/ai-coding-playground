# CLAUDE.MD - AI Coding Playground

## Project Overview

This is a mobile-first fullstack application playground for exploring AI-assisted development. The project uses modern technologies for both frontend and backend development.

## Architecture

### Frontend (In Progress)
- **Framework**: Angular 21.1.0
- **Language**: TypeScript 5.9.2
- **Build Tool**: Angular CLI 21.1.1 with application builder
- **Testing**: Vitest 4.0.8
- **Package Manager**: npm 11.7.0
- **Status**: Bootstrap complete with basic Angular standalone application
- **Note**: Ionic Framework and Capacitor not yet added

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
├── frontend/                 # Angular frontend application
│   ├── angular.json          # Angular CLI configuration
│   ├── package.json          # npm dependencies and scripts
│   ├── tsconfig.json         # TypeScript configuration
│   ├── tsconfig.app.json     # App-specific TypeScript config
│   ├── tsconfig.spec.json    # Test-specific TypeScript config
│   ├── public/               # Static assets
│   │   └── favicon.ico
│   └── src/
│       ├── main.ts           # Application entry point
│       ├── index.html        # HTML template
│       ├── styles.css        # Global styles
│       └── app/
│           ├── app.ts        # Root component (standalone)
│           ├── app.html      # Root component template
│           ├── app.css       # Root component styles
│           ├── app.config.ts # Application configuration
│           ├── app.routes.ts # Routing configuration
│           └── app.spec.ts   # Root component tests
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

### Frontend Angular
- `@angular/core` ^21.1.0 - Angular core framework
- `@angular/common` ^21.1.0 - Common Angular directives and pipes
- `@angular/router` ^21.1.0 - Angular routing
- `@angular/forms` ^21.1.0 - Forms handling
- `rxjs` ~7.8.0 - Reactive extensions
- `@angular/cli` ^21.1.1 - Angular CLI (dev)
- `vitest` ^4.0.8 - Testing framework (dev)
- `jsdom` ^27.1.0 - DOM implementation for testing (dev)

### Backend Spring Boot
- `spring-boot-starter-webmvc` - RESTful web services
- `kotlin-reflect` - Kotlin reflection support
- `jackson-module-kotlin` - JSON serialization for Kotlin
- `spring-boot-starter-webmvc-test` - Testing utilities
- `kotlin-test-junit5` - Kotlin testing with JUnit 5

## What's Implemented

1. **Frontend Bootstrap**
   - Angular 21 standalone application initialized
   - TypeScript 5.9 configuration
   - Vitest testing framework configured
   - Basic app component with Angular Signals
   - Routing infrastructure in place
   - Prettier configured for code formatting
   - Application builder with development/production configs

2. **Backend Bootstrap**
   - Spring Boot 4 application initialized
   - Kotlin configuration with strict compiler options
   - Basic project structure following Spring Boot conventions
   - Test setup with JUnit 5

## What's Not Yet Implemented

1. **Frontend Mobile**: Ionic Framework and Capacitor not yet added
2. **Frontend State**: NgRx or advanced state management not configured
3. **Frontend PWA**: PWA capabilities not yet enabled
4. **Database**: PostgreSQL not configured, no JPA entities
5. **API Endpoints**: No REST controllers defined
6. **Security**: Spring Security not configured
7. **Docker**: No Docker Compose for local development
8. **Testing**: Limited test coverage
9. **API Documentation**: SpringDoc/Swagger not configured
10. **Frontend-Backend Integration**: No API integration yet

## Development Workflow

### Frontend Development
```bash
# Install dependencies
cd frontend
npm install

# Start dev server (http://localhost:4200)
npm start

# Build for production
npm run build

# Run tests
npm test

# Build and watch for changes
npm run watch
```

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

1. **Angular Version**: Using Angular 21 (latest stable)
2. **Angular Architecture**: Standalone components (no NgModules)
3. **Frontend Testing**: Vitest instead of Jasmine/Karma
4. **TypeScript**: Version 5.9.2
5. **Code Formatting**: Prettier configured with 100 char line width
6. **Java Version**: Using Java 24 (latest) via toolchain configuration
7. **Kotlin**: Version 2.2.21 with strict compiler options enabled
8. **Build Tool**: Gradle with Kotlin DSL preferred over Maven
9. **Spring Boot**: Using latest stable version 4.0.2
10. **Mobile-First**: Project designed with mobile as primary target

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
- **Frontend Location**: All frontend code is in `frontend/`
- **Frontend Architecture**: Use standalone components, not NgModules
- **Frontend Testing**: Vitest configured, use it for unit tests
- **Frontend Formatting**: Prettier configured - run on save or via npm scripts
- **Backend Location**: All backend code is in `backend/aiplayground/`
- **Package Naming**: Use `org.sualk.aiplayground` for all Kotlin/Java classes
- **Build Commands**: Use `./gradlew` wrapper for backend, `npm` for frontend
- **Testing**: Vitest for frontend, JUnit 5 for backend
- **Code Style**: Follow Angular/TypeScript conventions for frontend, Kotlin conventions for backend

## Common Commands

```bash
# Frontend - Development
cd frontend
npm install                    # Install dependencies
npm start                      # Start dev server on :4200
npm run build                  # Production build
npm test                       # Run Vitest tests
npm run watch                  # Build and watch for changes

# Backend - Build and run
cd backend/aiplayground
./gradlew clean build          # Clean and build
./gradlew bootRun              # Run Spring Boot app

# Backend - Testing
./gradlew test                 # Run all tests
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

### Frontend
1. Adding Ionic Framework 8 for mobile UI components
2. Configuring Capacitor for native mobile capabilities
3. Implementing NgRx for complex state management
4. Adding PWA capabilities with @angular/pwa
5. Creating reusable component library
6. Setting up E2E tests with Playwright
7. Adding ESLint for code quality
8. Integrating with backend API

### Backend
1. Adding Spring Data JPA and PostgreSQL configuration
2. Setting up Docker Compose for local PostgreSQL
3. Implementing REST controllers with proper error handling
4. Adding Spring Security with JWT
5. Setting up Liquibase for database migrations
6. Implementing proper logging with structured logs
7. Adding health checks and monitoring (Spring Actuator)
8. Configuring CORS for frontend integration
