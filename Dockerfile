# Simple Dockerfile for Railway
FROM maven:3.9.4-openjdk-17-slim AS build

WORKDIR /app

# Copy and build
COPY . .
RUN mvn clean package -DskipTests

# Runtime
FROM openjdk:17-jre-slim

WORKDIR /app

# Copy jar file
COPY --from=build /app/target/*.jar app.jar

# Expose port (Railway will inject PORT env var)
EXPOSE 8080

# Run application
CMD ["java", "-jar", "app.jar"]