# -*- mode: Python -*-

# Backend (Spring Boot)
docker_build(
    'ai-playground-backend',
    context='./backend',
    dockerfile='./backend/Dockerfile',
    live_update=[
        sync('./backend/src', '/app/src'),
        run(
            'cd /app && ./gradlew classes',
            trigger=['./backend/src/main/kotlin', './backend/src/main/resources']
        ),
    ],
)

docker_compose('./docker-compose.yml')
