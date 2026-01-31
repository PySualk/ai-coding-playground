# CLAUDE.MD - AI Coding Playground

## Project Overview

This is a mobile-first fullstack application playground for exploring AI-assisted development. The project uses modern technologies for both frontend and backend development.

## Quick Start

### Prerequisites
- Docker Desktop (required for Tilt and database)
- Node.js 22+ (for local frontend development)
- Java 21+ (for local backend development)
- Tilt CLI (optional, for live reload development)

### Initial Setup
```bash
# 1. Start database
docker-compose up -d postgres

# 2. Start all services with Tilt (recommended)
tilt up

# OR run services locally
cd backend && ./gradlew bootRun  # Terminal 1
cd frontend && npm install && npm start  # Terminal 2
```

Database migrations run automatically on backend startup via Liquibase.

## Architecture

### Frontend
- **Framework**: Angular 20.3.0
- **UI Framework**: Ionic 8.4.1
- **Language**: TypeScript 5.9.2
- **Build Tool**: Angular CLI 20.3.15 with application builder
- **Testing**: Karma 6.4.0 with Jasmine 5.9.0
- **Package Manager**: npm 11.7.0
- **Status**: Ionic 8 integrated with standalone components
- **Note**: Capacitor not yet added (web-only for now)

### Backend
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
- `CONTEXT7_API_KEY` - Context7 MCP server API key (optional, for documentation features)
- `DATABASE_URI` - PostgreSQL connection string (default: `postgresql://devuser:devpassword@localhost:5432/aiplayground`)

Docker Compose sets these automatically. For local development, backend uses `application.yml` defaults.

## Current Project Structure

```
ai-coding-playground/
├── README.md                  # Basic project readme
├── TECHNOLOGIES.md           # Detailed technology stack documentation
├── CLAUDE.MD                 # This file - AI assistant context
├── CODING-GUIDELINES.md      # Git workflow and commit conventions for AI
├── TILT.md                   # Tilt development guide
├── Tiltfile                  # Tilt configuration for live reload
├── docker-compose.yml        # Docker Compose service definitions
├── .dockerignore             # Docker build exclusions
├── .mcp.json                 # MCP server configuration
├── frontend/                 # Angular frontend application
│   ├── Dockerfile            # Multi-stage Docker build
│   ├── nginx.conf            # Production nginx configuration
│   ├── .dockerignore         # Frontend Docker exclusions
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
│       ├── styles.css        # Global styles (Ionic CSS imports)
│       └── app/
│           ├── app.ts        # Root component (standalone)
│           ├── app.html      # Root component template (ion-app)
│           ├── app.css       # Root component styles
│           ├── app.config.ts # Application configuration (Ionic providers)
│           ├── app.routes.ts # Routing configuration
│           ├── app.spec.ts   # Root component tests
│           └── pages/        # Ionic pages
│               └── home/     # Home page
│                   ├── home.page.ts   # Home page component
│                   ├── home.page.html # Home page template
│                   └── home.page.css  # Home page styles
└── backend/
    ├── Dockerfile                    # Multi-stage Docker build
    ├── .dockerignore                 # Backend Docker exclusions
    ├── build.gradle.kts              # Gradle build configuration
    ├── settings.gradle.kts           # Gradle settings
    ├── gradle/                       # Gradle wrapper files
    ├── gradlew                       # Gradle wrapper script (Unix)
    ├── gradlew.bat                   # Gradle wrapper script (Windows)
    └── src/
        ├── main/kotlin/org/sualk/aiplayground/
        │   └── AiplaygroundApplication.kt    # Main Spring Boot application
        └── test/kotlin/org/sualk/aiplayground/
            └── AiplaygroundApplicationTests.kt
```

## Key Dependencies

### Frontend
- Angular 20.3.0 (standalone components, signals, vite-based build)
- Ionic 8.4.1 (mobile-first UI framework)
- TypeScript 5.9.2
- Karma/Jasmine for testing

See `frontend/package.json` for complete dependency list.

### Backend
- Spring Boot 4.0.2
- Kotlin 2.2.21
- Spring Data JPA + Hibernate
- Liquibase (database migrations)
- PostgreSQL driver
- MockK (Kotlin testing)
- Testcontainers (integration tests)

**Critical**: Use `com.fasterxml.jackson.module:jackson-module-kotlin` (not `tools.jackson.module`)

See `backend/build.gradle.kts` for complete dependency list.

## What's Implemented

1. **Frontend Bootstrap**
   - Angular 20.3.0 standalone application initialized
   - Ionic 8.4.1 integrated with standalone components
   - TypeScript 5.9.2 configuration
   - Karma/Jasmine testing framework configured
   - Home page with Ionic UI components
   - Routing infrastructure with lazy loading
   - Prettier configured for code formatting (100 char line width)
   - Application builder with development/production configs
   - Modern @angular/build system (vite-based)
   - Ionic global styles and theming configured

2. **Backend Bootstrap**
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

3. **CI/CD**
   - GitHub Actions workflow for automated testing and builds
   - Parallel jobs for frontend (Karma tests) and backend (JUnit 5 + Testcontainers)
   - Runs on push to main, pull requests, and manual dispatch
   - Gradle and npm dependency caching for faster builds
   - Test results and build artifacts uploaded

4. **Docker & Tilt Setup**
   - Multi-stage Dockerfile for backend (Gradle build + JRE runtime)
   - Multi-stage Dockerfile for frontend (development + production builds)
   - Docker Compose configuration for service orchestration
   - Tiltfile for local development with live reload (uses Docker Compose)
   - Nginx configuration for production frontend serving
   - Docker ignore files for optimized builds

## API Endpoints

### User CRUD (implemented)
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

1. **Mobile Native**: Capacitor not yet added (web-only for now)
2. **Frontend State**: NgRx or advanced state management not configured
3. **Frontend PWA**: PWA capabilities not yet enabled
4. **Security**: Spring Security not configured (no authentication/authorization)
5. **API Documentation**: SpringDoc/Swagger not configured
6. **Frontend-Backend Integration**: No API integration yet
7. **Ionic Features**: Many Ionic components not yet utilized
8. **Kubernetes Deployment**: Production K8s manifests not configured

## Development Workflow

### Option 1: Tilt (Recommended)

Tilt provides a unified development environment with live reload for both frontend and backend using Docker Compose.

```bash
# Start both frontend and backend with live reload
tilt up

# View Tilt UI (opens in browser)
# http://localhost:10350

# Stop Tilt
tilt down
```

**Features:**
- Uses Docker Compose (no Kubernetes required)
- Automatic Docker image building
- Live reload for code changes (frontend and backend)
- Unified logs and resource monitoring
- Port forwarding: Frontend (4200), Backend (8080)

### Option 2: Local Development (Without Docker)

#### Frontend Development
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

#### Backend Development
```bash
# Build the project
cd backend
./gradlew build

# Run the application
./gradlew bootRun

# Run tests
./gradlew test
```

### Git Workflow
- **Main Branch**: `main`
- **Guidelines**: See CODING-GUIDELINES.md for commit conventions, branch naming, and PR templates

## Key Decisions & Context

1. **Angular Version**: Using Angular 20.3.0 (latest stable)
2. **Ionic Version**: Using Ionic 8.4.1 (latest stable)
3. **Angular Architecture**: Standalone components (no NgModules) - default in Angular 20
4. **Ionic Integration**: Standalone Ionic components with provideIonicAngular
5. **Frontend Testing**: Karma/Jasmine (Angular default)
6. **TypeScript**: Version 5.9.2
7. **Code Formatting**: Prettier configured with 100 char line width, single quotes
8. **Angular Build**: Modern @angular/build (vite-based) instead of webpack
9. **Java Version**: Using Java 21 (LTS) via toolchain configuration
10. **Kotlin**: Version 2.2.21 with strict compiler options enabled
11. **Build Tool**: Gradle with Kotlin DSL preferred over Maven
12. **Spring Boot**: Using latest stable version 4.0.2
13. **Development Environment**: Tilt with Docker Compose (no Kubernetes)
14. **Containerization**: Multi-stage Docker builds for optimized images
15. **Mobile-First**: Ionic 8 provides mobile-first UI components

## Planned Features

Based on TECHNOLOGIES.md, the project plans to include:

- Mobile-first UI with Ionic Framework
- Progressive Web App capabilities
- Native mobile features (camera, geolocation, push notifications)
- RESTful API architecture
- JWT authentication with Spring Security
- PostgreSQL database with JPA/Hibernate
- API documentation with Swagger
- Comprehensive testing (Karma/Jasmine, Playwright, JUnit 5, Testcontainers)

## Notes for AI Assistants

- **Backend Package Structure**: Use layered architecture: domain/ → application/ → infrastructure/
- **Jackson Dependency**: Must use `com.fasterxml.jackson.module:jackson-module-kotlin` (not `tools.jackson.module`)
- **UserRepository**: Methods like `findByEmail()` return `Optional<T>`, not nullable `T?`
- **MockMvc Setup**: Without `@AutoConfigureMockMvc`, use `MockMvcBuilders.webAppContextSetup(webApplicationContext).build()`
- **Testcontainers**: Integration tests require Docker running - will fail in environments without Docker
- **Kotlin Unit Testing**: Use MockK (`io.mockk:mockk`) for Kotlin-friendly mocking with Spring Boot
- **Frontend Testing**: Angular tests using `IonRouterOutlet` require `provideRouter([])` in test configuration
- **Backend Testing**: Spring Boot tests with database require `@Testcontainers` annotation and dynamic property configuration
- **CI Environment**: Gradle needs `--no-daemon` flag, Karma needs `--no-watch --no-progress --browsers=ChromeHeadless`
- **Coding Guidelines**: Follow git workflow, commit message format, and PR guidelines in CODING-GUIDELINES.md
- **Technology Stack**: See TECHNOLOGIES.md for full tech stack details
- **Frontend Location**: All frontend code is in `frontend/`
- **Frontend Architecture**: Use standalone components (default in Angular 20), not NgModules
- **Ionic Components**: Import Ionic components from `@ionic/angular/standalone`
- **Frontend Testing**: Karma/Jasmine configured, use for unit tests
- **Frontend Formatting**: Prettier configured - run on save or via npm scripts
- **Angular Version**: Using Angular 20 with modern features (signals, control flow, etc.)
- **Ionic Version**: Using Ionic 8 with web components and mobile-first design
- **Backend Location**: All backend code is in `backend/`
- **Package Naming**: Use `org.sualk.aiplayground` for all Kotlin/Java classes
- **Build Commands**: Use `./gradlew` wrapper for backend, `npm` for frontend
- **Development Environment**: Tilt with Docker Compose for live reload (no Kubernetes)
- **Containerization**: Docker with multi-stage builds for frontend and backend
- **Testing**: Karma/Jasmine for frontend, JUnit 5 for backend
- **Code Style**: Follow Angular/TypeScript conventions for frontend, Kotlin conventions for backend
- **MCP Servers**: 4 servers configured in `.mcp.json`:
  - `context7` - Library documentation and code examples (requires CONTEXT7_API_KEY)
  - `angular-cli` - Angular-specific documentation and best practices
  - `playwright` - Browser automation and E2E testing
  - `postgres` - Database inspection and queries (connects to local PostgreSQL)
- **Context7 Usage**: Always use Context7 MCP proactively when needing library/API documentation, code generation, setup or configuration steps without requiring explicit user request
- **Port Forwarding**: Frontend :4200, Backend :8080, Postgres :5432, Tilt UI :10350
- **Live Reload**: File changes sync automatically via Docker volumes (frontend) and Tilt (backend)

## Common Commands

```bash
# GitHub Actions - CI Pipeline
# Workflow file: .github/workflows/ci.yml
# Triggers: push to main, PRs to main, manual dispatch
# Backend: Java 21, Gradle with --no-daemon flag
# Frontend: Node 22, Karma with --no-watch --no-progress --browsers=ChromeHeadless

# Tilt - Development (Recommended)
tilt up                        # Start all services with live reload
tilt down                      # Stop all services
tilt logs                      # View logs for all services
tilt logs backend              # View logs for specific service

# Frontend - Local Development
cd frontend
npm install                    # Install dependencies
npm start                      # Start dev server on :4200
npm run build                  # Production build
npm test                       # Run Karma/Jasmine tests
npm run watch                  # Build and watch for changes

# Backend - Local Development
cd backend
./gradlew clean build          # Clean and build
./gradlew bootRun              # Run Spring Boot app
./gradlew test                 # Run all tests
./gradlew test --tests SpecificTest
./gradlew test --tests "UserServiceImplTest"  # Run specific test class
./gradlew dependencies         # Check dependencies

# Docker Compose - Without Tilt
docker-compose up                # Start all services
docker-compose down              # Stop all services
docker-compose build             # Rebuild images
docker-compose logs              # View logs
docker-compose logs -f backend   # Follow backend logs

# Docker - Manual build (not needed with Tilt or Docker Compose)
docker build -t ai-playground-backend ./backend
docker build -t ai-playground-frontend ./frontend
docker build -t ai-playground-frontend:prod --target production ./frontend

# Docker - Debug
docker exec -it backend /bin/sh    # Shell into backend container
docker exec -it frontend /bin/sh   # Shell into frontend container

# Git workflow - See CODING-GUIDELINES.md for detailed workflow
```

## Environment Setup

- **Platform**: macOS (Darwin 25.2.0)
- **Working Directory**: `/Users/wil/workspace/github/ai-coding-playground`
- **Repository**: Git initialized with GitHub remote
- **Container Runtime**: Docker Desktop (or compatible)
- **Development Tool**: Tilt with Docker Compose (no Kubernetes required)
- **Node Version**: 22 (in Docker)
- **Java Version**: 21 (in Docker)

## Troubleshooting

### Docker Issues
```bash
# Check if Docker is running
docker ps

# Restart Docker services
docker-compose down && docker-compose up -d

# Clean rebuild
docker-compose down -v && docker-compose build --no-cache
```

### Port Conflicts
- Frontend :4200, Backend :8080, Postgres :5432 must be available
- Check with: `lsof -i :4200` or `lsof -i :8080`

### Database Connection Failed
- Ensure postgres container is healthy: `docker-compose ps`
- Wait for healthcheck: postgres must be ready before backend starts
- Check logs: `docker-compose logs postgres`

### Testcontainers Failing
- Integration tests require Docker running
- Grant Docker socket access: `/var/run/docker.sock`
- CI uses `--no-daemon` flag for Gradle

### Tilt Issues
- Clear Tilt state: `tilt down && rm -rf ~/.tilt-dev`
- Check Tilt UI at http://localhost:10350 for detailed logs

## Future Additions

When implementing new features, consider:

### Frontend
1. Adding Capacitor for native mobile capabilities (iOS/Android)
2. Implementing Signal-based state management or NgRx
3. Adding PWA capabilities with @angular/pwa
4. Creating more Ionic UI components and pages
5. Setting up E2E tests with Playwright
6. Adding ESLint for code quality
7. Integrating with backend API
8. Implementing Ionic theming and dark mode
9. Adding native device features (camera, geolocation, etc.) via Capacitor
10. Creating reusable Ionic component library

### Backend
1. Adding Spring Security with JWT authentication
2. Implementing proper logging with structured logs
3. Adding health checks and monitoring (Spring Actuator)
4. Configuring CORS for frontend integration
5. Adding SpringDoc/Swagger for API documentation
6. Implementing additional domain entities and relationships
7. Adding caching with Redis
8. Implementing background job processing
