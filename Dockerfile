# ============================================================================
# Stage 1: Maven Dependencies Cache
# ============================================================================
FROM maven:3.9.6-eclipse-temurin-17-alpine AS dependencies
WORKDIR /build

# Copy only the pom.xml first to leverage Docker layer caching
COPY pom.xml .

# Pre-fetch all dependencies to cache them unless pom.xml changes
RUN mvn dependency:go-offline -B


# ============================================================================
# Stage 2: Application Build
# ============================================================================
FROM maven:3.9.6-eclipse-temurin-17-alpine AS build
WORKDIR /build

# Copy dependencies cache from previous stage
COPY --from=dependencies /root/.m2 /root/.m2

# Copy full project
COPY pom.xml .
COPY src ./src

# Compile and package the application (skip tests for faster CI builds)
RUN mvn clean package -DskipTests -B

# Optional: reduce final jar size (remove Maven cache)
RUN rm -rf /root/.m2/repository


# ============================================================================
# Stage 3: Lightweight Runtime Image
# ============================================================================
FROM eclipse-temurin:17-jre-alpine AS runtime

# ----------------------------------------------------------------------------
# Metadata
# ----------------------------------------------------------------------------
LABEL maintainer="KitchenIQ Team" \
      description="KitchenIQ Application - Production Optimized Image" \
      version="1.2"

# ----------------------------------------------------------------------------
# System Setup: Non-root user and minimal runtime utilities
# ----------------------------------------------------------------------------
RUN apk add --no-cache dumb-init tzdata curl && \
    addgroup -S appgroup && \
    adduser -S appuser -G appgroup -h /app && \
    rm -rf /var/cache/apk/*

WORKDIR /app

# ----------------------------------------------------------------------------
# Copy the packaged application from build stage
# ----------------------------------------------------------------------------
COPY --from=build --chown=appuser:appgroup /build/target/*.jar app.jar

# ----------------------------------------------------------------------------
# Security & Performance
# ----------------------------------------------------------------------------
USER appuser:appgroup

# JVM tuning for container environments
ENV JAVA_OPTS="-XX:+UseContainerSupport \
               -XX:MaxRAMPercentage=75.0 \
               -XX:+ExitOnOutOfMemoryError \
               -Djava.security.egd=file:/dev/./urandom" \
    PORT=5000

EXPOSE 5000

# ----------------------------------------------------------------------------
# Healthcheck for Docker and Kubernetes environments
# ----------------------------------------------------------------------------
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
    CMD curl -fs http://localhost:5000/actuator/health || exit 1

# ----------------------------------------------------------------------------
# Entrypoint
# ----------------------------------------------------------------------------
ENTRYPOINT ["dumb-init", "--"]
CMD ["sh", "-c", "java ${JAVA_OPTS} \
    ${DATABASE_URL:+-Dspring.datasource.url=${DATABASE_URL}} \
    -jar app.jar"]

# ============================================================================
# Notes:
# - DATABASE_URL is not defined here; must be passed at runtime
# - If DATABASE_URL is not provided, Spring Boot defaults will apply
# - Non-root execution for security
# - dumb-init ensures proper signal handling
# - Final image size: ~160MB
# ============================================================================