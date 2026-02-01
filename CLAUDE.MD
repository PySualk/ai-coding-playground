# CLAUDE.MD - AI Coding Playground

## Project Overview

This is a mobile-first fullstack application playground for exploring AI-assisted development. The project uses modern technologies for both frontend and backend development.

**For detailed documentation:**
- Frontend (Angular + Ionic): See `frontend/CLAUDE.md`
- Backend (Spring Boot + Kotlin): See `backend/CLAUDE.md`

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

## Architecture Overview

### Frontend
- **Framework**: Angular 20.3.0 with Ionic 8.4.1
- **Language**: TypeScript 5.9.2
- **Details**: See `frontend/CLAUDE.md`

### Backend
- **Framework**: Spring Boot 4.0.2
- **Language**: Kotlin 2.2.21 on Java 21
- **Database**: PostgreSQL 18 with Liquibase migrations
- **Details**: See `backend/CLAUDE.md`

### Environment Variables
Required for local development:
- `CONTEXT7_API_KEY` - Context7 MCP server API key (optional, for documentation features)
- `DATABASE_URI` - PostgreSQL connection string (default: `postgresql://devuser:devpassword@localhost:5432/aiplayground`)

Docker Compose sets these automatically. For local development, backend uses `application.yml` defaults.

## Project Structure

```
ai-coding-playground/
├── README.md                  # Basic project readme
├── TECHNOLOGIES.md           # Detailed technology stack documentation
├── CLAUDE.md                 # This file - shared AI assistant context
├── CODING-GUIDELINES.md      # Git workflow and commit conventions
├── TILT.md                   # Tilt development guide
├── Tiltfile                  # Tilt configuration for live reload
├── docker-compose.yml        # Docker Compose service definitions
├── .mcp.json                 # MCP server configuration
├── frontend/                 # Angular + Ionic application
│   ├── CLAUDE.md             # Frontend-specific AI context
│   └── ... (see frontend/CLAUDE.md for details)
└── backend/                  # Spring Boot + Kotlin application
    ├── CLAUDE.md             # Backend-specific AI context
    └── ... (see backend/CLAUDE.md for details)
```

## What's Implemented

1. **Frontend**: Angular 20.3.0 + Ionic 8.4.1 application with standalone components
   - See `frontend/CLAUDE.md` for details

2. **Backend**: Spring Boot 4.0.2 + Kotlin 2.2.21 with User CRUD API
   - See `backend/CLAUDE.md` for details and API endpoints

3. **CI/CD**: GitHub Actions workflow for automated testing and builds
   - Parallel jobs for frontend (Karma tests) and backend (JUnit 5 + Testcontainers)
   - Runs on push to main, pull requests, and manual dispatch
   - Gradle and npm dependency caching for faster builds

4. **Docker & Tilt**: Multi-stage Dockerfiles and Tilt configuration
   - Docker Compose for service orchestration
   - Tiltfile for local development with live reload
   - See `TILT.md` for details

## What's Not Yet Implemented

1. **Mobile Native**: Capacitor not yet added (web-only for now)
2. **Security**: Spring Security not configured (no authentication/authorization)
3. **Frontend-Backend Integration**: No API integration yet
4. **Kubernetes Deployment**: Production K8s manifests not configured

## Development Workflow

### Option 1: Tilt (Recommended)

Tilt provides a unified development environment with live reload for both frontend and backend using Docker Compose.

```bash
# Start both frontend and backend with live reload
tilt up

# View Tilt UI (opens in browser at http://localhost:10350)

# Stop Tilt
tilt down
```

**Features:**
- Uses Docker Compose (no Kubernetes required)
- Automatic Docker image building
- Live reload for code changes (frontend and backend)
- Unified logs and resource monitoring
- Port forwarding: Frontend (:4200), Backend (:8080), Postgres (:5432)

See `TILT.md` for detailed Tilt usage.

### Option 2: Local Development (Without Docker)

#### Frontend
```bash
cd frontend
npm install && npm start  # Starts on :4200
```
See `frontend/CLAUDE.md` for all frontend commands.

#### Backend
```bash
cd backend
./gradlew bootRun  # Starts on :8080
```
See `backend/CLAUDE.md` for all backend commands and API endpoints.

### Git Workflow
- **Main Branch**: `main`
- **Guidelines**: See `CODING-GUIDELINES.md` for commit conventions, branch naming, and PR templates

## Key Decisions & Context

1. **Development Environment**: Tilt with Docker Compose (no Kubernetes)
2. **Containerization**: Multi-stage Docker builds for optimized images
3. **Mobile-First**: Ionic 8 provides mobile-first UI components
4. **Architecture**: Fullstack with clear separation (frontend/ and backend/)

**Frontend-specific decisions**: See `frontend/CLAUDE.md`
**Backend-specific decisions**: See `backend/CLAUDE.md`

## Planned Features

Based on `TECHNOLOGIES.md`, the project plans to include:

- Mobile-first UI with Ionic Framework
- Progressive Web App capabilities
- Native mobile features (camera, geolocation, push notifications)
- JWT authentication with Spring Security
- API documentation with Swagger
- Comprehensive E2E testing with Playwright

See `frontend/CLAUDE.md` and `backend/CLAUDE.md` for detailed roadmaps.

## Notes for AI Assistants

### Project Organization
- **Frontend**: All code in `frontend/` - See `frontend/CLAUDE.md` for Angular/Ionic specifics
- **Backend**: All code in `backend/` - See `backend/CLAUDE.md` for Spring Boot/Kotlin specifics
- **Documentation**: This file contains shared info; consult specific CLAUDE.md files for details

### Development Environment
- **Tilt**: Use Tilt with Docker Compose for live reload (no Kubernetes)
- **Ports**: Frontend :4200, Backend :8080, Postgres :5432, Tilt UI :10350
- **Live Reload**: File changes sync automatically via Docker volumes (frontend) and Tilt (backend)
- **CI Environment**: GitHub Actions with parallel jobs for frontend and backend

### Guidelines & Standards
- **Coding Guidelines**: Follow git workflow, commit message format, and PR guidelines in `CODING-GUIDELINES.md`
- **Technology Stack**: See `TECHNOLOGIES.md` for full tech stack details

### MCP Servers
4 servers configured in `.mcp.json`:
- `context7` - Library documentation and code examples (requires CONTEXT7_API_KEY)
- `angular-cli` - Angular-specific documentation and best practices
- `playwright` - Browser automation and E2E testing
- `postgres` - Database inspection and queries (connects to local PostgreSQL)

**Context7 Usage**: Always use Context7 MCP proactively when needing library/API documentation, code generation, setup or configuration steps without requiring explicit user request

### Testing Requirements
- **Testcontainers**: Integration tests require Docker running
- **CI Flags**: Gradle needs `--no-daemon` flag, Karma needs `--no-watch --no-progress --browsers=ChromeHeadless`

## Common Commands

```bash
# Tilt - Development (Recommended)
tilt up                        # Start all services with live reload
tilt down                      # Stop all services
tilt logs                      # View logs for all services
tilt logs backend              # View logs for specific service
tilt logs frontend

# Docker Compose - Without Tilt
docker-compose up              # Start all services
docker-compose down            # Stop all services
docker-compose build           # Rebuild images
docker-compose logs -f         # Follow logs

# Docker - Debug
docker exec -it backend /bin/sh    # Shell into backend container
docker exec -it frontend /bin/sh   # Shell into frontend container
docker ps                          # List running containers

# Git workflow
# See CODING-GUIDELINES.md for detailed workflow
```

**Frontend commands**: See `frontend/CLAUDE.md`
**Backend commands**: See `backend/CLAUDE.md`

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
- Check with: `lsof -i :<port>`
- Kill process: `kill -9 $(lsof -t -i:<port>)`

### Database Connection Failed
- Ensure postgres container is healthy: `docker-compose ps`
- Wait for healthcheck: postgres must be ready before backend starts
- Check logs: `docker-compose logs postgres`

### Tilt Issues
- Clear Tilt state: `tilt down && rm -rf ~/.tilt-dev`
- Check Tilt UI at http://localhost:10350 for detailed logs

**Frontend-specific issues**: See `frontend/CLAUDE.md`
**Backend-specific issues**: See `backend/CLAUDE.md`

## Future Additions

When implementing new features:
- **Frontend roadmap**: See `frontend/CLAUDE.md`
- **Backend roadmap**: See `backend/CLAUDE.md`

Key priorities:
1. Frontend-Backend integration
2. JWT authentication with Spring Security
3. Capacitor for native mobile (iOS/Android)
4. API documentation with Swagger
5. E2E testing with Playwright
