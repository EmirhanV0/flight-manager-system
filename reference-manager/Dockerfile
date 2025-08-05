# =============================================================================
# UYS Reference Manager Service - Dockerfile
# Multi-stage build for optimized production image
# =============================================================================

# Build Stage
FROM maven:3.9.5-eclipse-temurin-21 AS build

# Set working directory
WORKDIR /app

# Copy Maven configuration files
COPY pom.xml .

# Download dependencies (cached layer)
RUN mvn dependency:go-offline -B

# Copy source code
COPY src/ src/

# Build application
RUN mvn clean package -DskipTests

# Runtime Stage
FROM eclipse-temurin:21-jre-alpine

# Install necessary packages
RUN apk add --no-cache curl

# Create app user
RUN addgroup -g 1001 -S uys && adduser -S uys -G uys

# Set working directory
WORKDIR /app

# Copy built JAR from build stage
COPY --from=build /app/target/reference-manager-*.jar app.jar

# Create necessary directories
RUN mkdir -p /app/logs && chown -R uys:uys /app

# Switch to non-root user
USER uys

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# JVM options for production
ENV JAVA_OPTS="-Xms512m -Xmx1024m -XX:+UseG1GC -XX:+UseContainerSupport"

# Run application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"] 