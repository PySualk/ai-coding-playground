# Tilt Development Guide

This project uses [Tilt](https://tilt.dev) for local development with live reload support for both frontend and backend services.

## Prerequisites

1. **Docker Desktop** (or compatible container runtime)
   - [Download Docker Desktop](https://www.docker.com/products/docker-desktop/)
   - Ensure Docker is running before starting Tilt

2. **Tilt**
   ```bash
   # macOS
   brew install tilt-dev/tap/tilt

   # Linux
   curl -fsSL https://raw.githubusercontent.com/tilt-dev/tilt/master/scripts/install.sh | bash

   # Windows
   # Download from https://github.com/tilt-dev/tilt/releases
   ```

## Quick Start

1. **Start Tilt**
   ```bash
   tilt up
   ```

   This will:
   - Build Docker images for frontend and backend
   - Start both services with live reload
   - Open the Tilt UI in your browser (http://localhost:10350)

2. **Access Your Services**
   - **Frontend**: http://localhost:4200
   - **Backend API**: http://localhost:8080
   - **Tilt UI**: http://localhost:10350

3. **Make Changes**
   - Edit files in `frontend/src/` or `backend/src/`
   - Tilt will automatically detect changes and reload
   - Watch the Tilt UI for build status and logs

4. **Stop Tilt**
   ```bash
   tilt down
   ```

## Tilt UI Features

The Tilt UI (http://localhost:10350) provides:

- **Resource Overview**: Status of frontend and backend services
- **Logs**: Real-time logs for each service
- **Build History**: Track build times and issues
- **Alerts**: Notifications for errors or warnings

## How It Works

This setup uses **Docker Compose** with Tilt (no Kubernetes required).

### Backend (Spring Boot)

- **Build**: Multi-stage Docker build using Gradle
- **Live Reload**: Changes to `.kt` files trigger recompilation
- **Port**: 8080
- **Container**: Plain Docker container managed by Compose

### Frontend (Angular)

- **Build**: Uses Angular dev server in Docker
- **Live Reload**: Volume mounts sync file changes automatically
- **Port**: 4200
- **Container**: Plain Docker container managed by Compose

## File Structure

```
.
├── Tiltfile                 # Tilt configuration
├── docker-compose.yml       # Docker Compose services
├── backend/
│   ├── Dockerfile           # Multi-stage build (Gradle + JRE)
│   └── .dockerignore        # Ignore build artifacts
└── frontend/
    ├── Dockerfile           # Multi-stage build (dev + prod)
    ├── nginx.conf           # Production nginx config
    └── .dockerignore        # Ignore node_modules
```

## Troubleshooting

### Docker Not Running
```bash
# Check Docker status
docker ps

# Start Docker Desktop if needed
```

### Port Already in Use
```bash
# Find process using port 4200 or 8080
lsof -ti:4200
lsof -ti:8080

# Kill the process if needed
kill -9 <PID>
```

### Clean Rebuild
```bash
# Stop Tilt
tilt down

# Remove all containers and images
docker system prune -a

# Restart Tilt
tilt up
```

### View Logs
```bash
# All services
tilt logs

# Specific service
tilt logs frontend
tilt logs backend
```

## Advanced Usage

### Running Specific Services

```bash
# Run only frontend
tilt up frontend

# Run only backend
tilt up backend
```

### Debugging

```bash
# View resource details
tilt describe backend

# Interactive shell in container
docker exec -it backend /bin/sh
docker exec -it frontend /bin/sh
```

## Production Builds

For production deployment, use the multi-stage builds:

```bash
# Frontend production build
docker build -t ai-playground-frontend:prod --target production ./frontend

# Backend production build
docker build -t ai-playground-backend:prod ./backend

# Run production containers
docker run -p 80:80 ai-playground-frontend:prod
docker run -p 8080:8080 ai-playground-backend:prod
```

## Next Steps

- Configure database (PostgreSQL) in docker-compose.yml
- Add environment variables for configuration
- Add health checks and readiness probes
- Set up production deployment configuration
