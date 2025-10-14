# Docker - KitchenIQ Usage Guide

## Optimization Features

### Implemented Techniques:

1. **Multi-Stage Build (3 stages)**
   - Stage 1: Maven dependencies cache
   - Stage 2: Application compilation
   - Stage 3: Final runtime image

2. **Ultra-Lightweight Base Image**
   - `eclipse-temurin:17-jre-alpine` (~165 MB)
   - JRE only (no full JDK)
   - Alpine Linux (minimalist distribution)

3. **Security**
   - Non-root user (`appuser:appgroup`)
   - No superuser permissions
   - Files owned by application user

4. **Layer Optimization**
   - JAR unpacked into separate layers
   - Dependencies (lib/) in independent layer
   - Better Docker cache usage

5. **JVM Performance**
   - `UseContainerSupport`: Automatic container limits detection
   - `MaxRAMPercentage=75.0`: Uses maximum 75% of available RAM
   - `ExitOnOutOfMemoryError`: Automatic restart on OOM

6. **Healthcheck**
   - Automatic container health verification
   - Endpoint: `/actuator/health`

7. **Signal Handling**
   - `dumb-init` as PID 1
   - Proper signal handling (SIGTERM, SIGINT)

---

## Usage Commands

### Build the image
```bash
docker build -t kitcheniq:latest .
```

### Build with specific name and tag

```bash
docker build -t kitcheniq:1.0.0 .
```

### View image size

```bash
docker images kitcheniq
```

### Run container (basic)

```bash
docker run -d \
  --name kitcheniq-app \
  -p 5000:5000 \
  kitcheniq:latest
```

### Run with environment variables from .env file

```bash
docker run -d \
  --name kitcheniq-app \
  -p 5000:5000 \
  --env-file .env \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e JAVA_OPTS="-Xmx512m -Xms256m" \
  --dns=8.8.8.8 \
  kitcheniq:latest
```

### Run with resource limits

```bash
docker run -d \
  --name kitcheniq-app \
  -p 5000:5000 \
  --memory="512m" \
  --cpus="1.0" \
  --env-file .env \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e JAVA_OPTS="-Xmx512m -Xms256m" \
  --dns=8.8.8.8 \
  kitcheniq:latest
```

### View logs in real-time

```bash
docker logs -f kitcheniq-app
```

### View logs with timestamp

```bash
docker logs -f --timestamps kitcheniq-app
```

### Health check status

```bash
docker inspect --format='{{json .State.Health}}' kitcheniq-app | jq
```

### View container statistics

```bash
docker stats kitcheniq-app
```

### Execute command inside container (debugging)

```bash
docker exec -it kitcheniq-app sh
```

### Stop container

```bash
docker stop kitcheniq-app
```

### Restart container

```bash
docker restart kitcheniq-app
```

### Remove container

```bash
docker rm kitcheniq-app
```

### Stop and remove in one command

```bash
docker stop kitcheniq-app && docker rm kitcheniq-app
```

---

## Size Analysis

### View layer sizes

```bash
docker history kitcheniq:latest
```

### View detailed image information

```bash
docker inspect kitcheniq:latest
```

### Expected size comparison:

* **Image with full JDK**: ~350-450 MB
* **Optimized image (JRE Alpine)**: ~165-200 MB
* **Reduction**: ~50-60%

---

## Development Workflow

### 1. Make code changes

```bash
# Edit files in src/
```

### 2. Rebuild the image

```bash
docker build -t kitcheniq:latest .
```

### 3. Stop previous container and run new one

```bash
docker stop kitcheniq-app || true
docker rm kitcheniq-app || true
docker run -d \
  --name kitcheniq-app \
  -p 5000:5000 \
  --env-file .env \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e JAVA_OPTS="-Xmx512m -Xms256m" \
  --dns=8.8.8.8 \
  kitcheniq:latest
```

### 4. Check logs

```bash
docker logs -f kitcheniq-app
```

---

## Applied Best Practices

1. Multi-stage build to reduce size
2. Alpine image (minimalist)
3. Non-root user for security
4. `.dockerignore` to reduce context
5. Maven dependencies cache
6. Unpacked JAR for better cache
7. Integrated health check
8. Signal handling with dumb-init
9. Configurable environment variables
10. JVM memory optimization

---

## Security

* **Does not run as root**: User `appuser` with non-privileged UID/GID
* **Minimum permissions**: Read-only application files
* **Official base image**: Eclipse Temurin (Adoptium/Eclipse Foundation)
* **No secrets in image**: Use environment variables or Docker secrets
* **Resource limits**: Can be applied with `--memory` and `--cpus`

---

## Troubleshooting

### Container does not start

```bash
# View detailed logs
docker logs kitcheniq-app

# View container events
docker events --filter container=kitcheniq-app
```

### Healthcheck fails

```bash
# Check that /actuator/health endpoint is available
docker exec kitcheniq-app wget -q -O- http://localhost:5000/actuator/health

# Or from the host
curl http://localhost:5000/actuator/health
```

### Memory issues

### Increase memory limit

```bash
docker run -d \
  --name kitcheniq-app \
  -p 5000:5000 \
  --memory="1g" \
  --env-file .env \
  -e SPRING_PROFILES_ACTIVE=prod \
  --dns=8.8.8.8 \
  kitcheniq:latest
```

### Or adjust JVM options

```bash
docker run -d \
  --name kitcheniq-app \
  -p 5000:5000 \
  --env-file .env \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e JAVA_OPTS="-Xmx768m -Xms256m" \
  --dns=8.8.8.8 \
  kitcheniq:latest
```

### Clean up Docker resources

```bash
# Remove unused images
docker image prune -a

# Remove stopped containers
docker container prune

# Clean everything (careful!)
docker system prune -a --volumes
```

---

## Final Result

Final image: **~200-270 MB** (depending on dependencies)

* Secure (non-root user)
* Fast (unpacked JAR)
* Efficient (optimized cache)
* Monitorable (integrated health check)
* Production-ready (signal handling)