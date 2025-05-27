# Multi-stage build để giảm kích thước image
FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app

# Copy pom.xml trước để tận dụng Docker layer caching
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code và build
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM openjdk:17-jdk-slim

# Cài đặt các package cần thiết
RUN apt-get update && apt-get install -y \
    curl \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Copy JAR file từ build stage
COPY --from=build /app/target/*.jar app.jar

# Tạo user non-root để chạy ứng dụng (bảo mật)
RUN addgroup --system spring && adduser --system spring --ingroup spring
RUN chown -R spring:spring /app
USER spring

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:${PORT:-8080}/actuator/health || exit 1

# Expose port (Render sẽ set PORT environment variable)
EXPOSE ${PORT:-8080}

# Chạy ứng dụng
ENTRYPOINT ["java", "-Dserver.port=${PORT:-8080}", "-jar", "/app/app.jar"]