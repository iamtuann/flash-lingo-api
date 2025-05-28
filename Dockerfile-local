# Sử dụng OpenJDK 17 làm base image
FROM openjdk:17-jdk-slim

# Đặt thư mục làm việc
WORKDIR /app

# Copy file JAR vào container
COPY target/*.jar app.jar

# Tạo thư mục cho credentials
RUN mkdir -p /app/config

# Đặt environment variable cho Google credentials
#ENV GOOGLE_APPLICATION_CREDENTIALS=/app/config/google-credentials.json

# Expose port
EXPOSE 8081

# Chạy ứng dụng
#ENTRYPOINT ["java", "-jar", "/app/app.jar", "--spring.profiles.active=prod"]
ENTRYPOINT ["java", "-jar", "/app/app.jar"]