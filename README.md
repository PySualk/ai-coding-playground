# AI Coding Playground

A mobile-first fullstack application playground for exploring AI-assisted development, built with Angular 20, Ionic 8, Spring Boot 4, and Kotlin.

## Table of Contents

- [Overview](#overview)
- [Prerequisites](#prerequisites)
- [Quick Start](#quick-start)
- [Development Workflows](#development-workflows)
- [Project Structure](#project-structure)
- [Documentation](#documentation)

## Overview

This project demonstrates modern fullstack development with:

- **Frontend**: Angular 20.3.0 + Ionic 8.4.1 (mobile-first UI)
- **Backend**: Spring Boot 4.0.2 + Kotlin 2.2.21
- **Database**: PostgreSQL 18
- **DevOps**: Docker + Tilt for unified development environment

## Prerequisites

### Required Tools

Choose one of the following development approaches:

#### Option 1: Tilt Development (Recommended)

For containerized development with live reload:

- **[Docker Desktop](https://www.docker.com/products/docker-desktop/)** (or compatible container runtime)
  - macOS: `brew install --cask docker`
  - Windows: Download installer from docker.com
  - Linux: Follow [official Docker installation guide](https://docs.docker.com/engine/install/)

- **[Tilt](https://tilt.dev/)**
  - macOS: `brew install tilt`
  - Linux: `curl -fsSL https://raw.githubusercontent.com/tilt-dev/tilt/master/scripts/install.sh | bash`
  - Windows: `scoop install tilt`
  - Verify: `tilt version`

- **[Git](https://git-scm.com/)**
  - macOS: `brew install git` or use Xcode Command Line Tools
  - Linux: `sudo apt-get install git` (Debian/Ubuntu) or `sudo yum install git` (RHEL/CentOS)
  - Windows: Download from [git-scm.com](https://git-scm.com/download/win)
  - Verify: `git --version`

#### Option 2: Local Development (Without Docker)

For native local development:

- **[Node.js 22.x](https://nodejs.org/)** with npm 11.7.0+
  - macOS: `brew install node@22`
  - Linux: Use [nvm](https://github.com/nvm-sh/nvm) - `nvm install 22`
  - Windows: Download from [nodejs.org](https://nodejs.org/)
  - Verify: `node --version` and `npm --version`

- **[Java 21 (JDK)](https://adoptium.net/)**
  - macOS: `brew install openjdk@21`
  - Linux: `sudo apt-get install openjdk-21-jdk` (Debian/Ubuntu)
  - Windows: Download from [Adoptium](https://adoptium.net/)
  - Verify: `java --version`

- **[Git](https://git-scm.com/)** (see above)

- **[Google Chrome](https://www.google.com/chrome/)** (for Karma/Jasmine tests)
  - macOS: `brew install --cask google-chrome`
  - Linux/Windows: Download from [google.com/chrome](https://www.google.com/chrome/)

### Optional Tools

- **[Docker Compose](https://docs.docker.com/compose/)** (included with Docker Desktop)
  - For running services without Tilt
  - Linux standalone install: `sudo apt-get install docker-compose-plugin`

- **[PostgreSQL 18](https://www.postgresql.org/)** (if not using Docker)
  - macOS: `brew install postgresql@18`
  - Linux: Follow [official PostgreSQL guide](https://www.postgresql.org/download/)
  - Windows: Download from [postgresql.org](https://www.postgresql.org/download/windows/)

## Quick Start

### Using Tilt (Recommended)

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd ai-coding-playground
   ```

2. **Start all services**
   ```bash
   tilt up
   ```

3. **Access the application**
   - Frontend: http://localhost:4200
   - Backend: http://localhost:8080
   - Tilt UI: http://localhost:10350

4. **Stop services**
   ```bash
   tilt down
   ```

### Using Local Development

#### Frontend

```bash
cd frontend
npm install
npm start
```

Visit http://localhost:4200

#### Backend

```bash
cd backend
./gradlew bootRun
```

Backend runs on http://localhost:8080

## Development Workflows

### Tilt Development

Tilt provides unified development with live reload for all services:

```bash
# Start development environment
tilt up

# View logs for all services
tilt logs

# View logs for specific service
tilt logs frontend
tilt logs backend
tilt logs postgres

# Restart a service
tilt trigger backend

# Stop everything
tilt down
```

**Features:**
- Automatic Docker image building
- Live reload on code changes
- Unified logs and monitoring
- Database included (PostgreSQL 18)

### Local Development

#### Frontend Commands

```bash
cd frontend

# Install dependencies
npm install

# Development server (http://localhost:4200)
npm start

# Production build
npm run build

# Run tests
npm test

# Build and watch for changes
npm run watch
```

#### Backend Commands

```bash
cd backend

# Build the project
./gradlew build

# Run the application
./gradlew bootRun

# Run tests
./gradlew test

# Run specific test
./gradlew test --tests SpecificTest

# Clean and rebuild
./gradlew clean build
```

### Docker Compose (Without Tilt)

```bash
# Start all services
docker-compose up

# Start in background
docker-compose up -d

# View logs
docker-compose logs -f

# Stop services
docker-compose down

# Rebuild images
docker-compose build
```

## Project Structure

```
ai-coding-playground/
├── frontend/           # Angular 20 + Ionic 8 application
│   ├── src/           # Source code
│   ├── Dockerfile     # Multi-stage Docker build
│   └── package.json   # npm dependencies
├── backend/           # Spring Boot 4 + Kotlin application
│   ├── src/          # Source code
│   ├── Dockerfile    # Multi-stage Docker build
│   └── build.gradle.kts
├── docker-compose.yml # Service orchestration
├── Tiltfile          # Tilt configuration
└── README.md         # This file
```

## Documentation

- **[CLAUDE.md](CLAUDE.md)** - Complete project context and architecture
- **[TECHNOLOGIES.md](TECHNOLOGIES.md)** - Detailed technology stack
- **[CODING-GUIDELINES.md](CODING-GUIDELINES.md)** - Git workflow and commit conventions
- **[TILT.md](TILT.md)** - Tilt development guide

## Technology Stack

### Frontend
- Angular 20.3.0 (standalone components)
- Ionic 8.4.1 (mobile-first UI framework)
- TypeScript 5.9.2
- Karma + Jasmine (testing)

### Backend
- Spring Boot 4.0.2
- Kotlin 2.2.21
- Java 21
- Gradle with Kotlin DSL

### Database
- PostgreSQL 18
- Spring Data JPA (planned)
- Liquibase (planned)

### DevOps
- Docker + Docker Compose
- Tilt (unified development)
- Multi-stage Docker builds

## Contributing

See [CODING-GUIDELINES.md](CODING-GUIDELINES.md) for:
- Git workflow and branching strategy
- Commit message format
- Pull request guidelines

## License

[Add your license here]

## Support

For issues and questions:
- Create an issue in the repository
- See documentation in project markdown files
