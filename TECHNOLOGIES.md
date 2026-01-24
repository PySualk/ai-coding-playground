# Technology Stack - Mobile-First Fullstack Application

## Frontend (Mobile-First)

### Core Framework
- Angular 20
- TypeScript 5+
- RxJS

### Mobile UI Framework
- Ionic Framework 8 (Angular integration)
- Ionic CLI

### State Management
- NgRx (complex state)
- Angular Signals (simple state)
- RxJS BehaviorSubjects (lightweight)

### Progressive Web App
- @angular/pwa
- Workbox

### Mobile Capabilities (Capacitor)
- Camera
- Geolocation
- Push notifications
- Storage
- Network status
- Device info

## Backend

### Runtime & Framework
- Kotlin 2.2
- Spring Boot 4
- JDK 25

### Spring Boot Dependencies
- Spring Web (RESTful APIs)
- Spring Data JPA
- Spring Security
- Spring Validation
- Spring Boot Actuator
- Spring Boot DevTools

### API Architecture
- RESTful APIs
- Spring WebSocket (optional)

### Authentication
- Spring Security with JWT
- OAuth 2.0 Resource Server (optional)

### Build Tool
- Gradle (Kotlin DSL)

## Database

### Primary Database
- PostgreSQL 18

### ORM
- Spring Data JPA with Hibernate

### Database Migration
- Liquibase

## Development Tools

### Version Control
- Git with GitHub

### Package Management
- npm or yarn (frontend)
- Gradle (backend)

### Code Quality
**Frontend**
- ESLint
- Prettier

**Backend**
- Detekt (Kotlin linter)
- ktlint

### Testing
**Frontend**
- Jest (unit testing)
- Playwright (E2E testing)
- Ionic testing utilities

**Backend**
- JUnit 5
- MockK (Kotlin mocking)
- Spring Boot Test
- Testcontainers (PostgreSQL integration tests)
- RestAssured or MockMvc (API testing)

### API Development & Testing
- Postman or Insomnia
- SpringDoc OpenAPI (Swagger UI)

## Development Environment

### Local Development
- Docker Compose (PostgreSQL 18)

### Environment Management
- Spring Profiles (dev, test)
- Angular/Ionic environments
- Capacitor configuration

### Hot Reload
- Ionic CLI dev server (`ionic serve`)
- Ionic Live Reload
- Spring Boot DevTools

### Mobile Testing
- Browser DevTools (mobile emulation)
- Ionic Live Reload
- Android Studio (Android emulator)
- Xcode (iOS simulator)
- Playwright (cross-browser E2E)