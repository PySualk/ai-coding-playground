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

k8s_yaml('''
apiVersion: v1
kind: Pod
metadata:
  name: backend
  labels:
    app: backend
spec:
  containers:
  - name: backend
    image: ai-playground-backend
    ports:
    - containerPort: 8080
---
apiVersion: v1
kind: Service
metadata:
  name: backend
spec:
  selector:
    app: backend
  ports:
  - port: 8080
    targetPort: 8080
''')

k8s_resource(
    'backend',
    port_forwards=['8080:8080'],
    labels=['backend'],
)

# Frontend (Angular)
docker_build(
    'ai-playground-frontend',
    context='./frontend',
    dockerfile='./frontend/Dockerfile',
    target='development',
    live_update=[
        sync('./frontend/src', '/app/src'),
        sync('./frontend/angular.json', '/app/angular.json'),
        sync('./frontend/tsconfig.json', '/app/tsconfig.json'),
    ],
)

k8s_yaml('''
apiVersion: v1
kind: Pod
metadata:
  name: frontend
  labels:
    app: frontend
spec:
  containers:
  - name: frontend
    image: ai-playground-frontend
    ports:
    - containerPort: 4200
---
apiVersion: v1
kind: Service
metadata:
  name: frontend
spec:
  selector:
    app: frontend
  ports:
  - port: 4200
    targetPort: 4200
''')

k8s_resource(
    'frontend',
    port_forwards=['4200:4200'],
    labels=['frontend'],
)
