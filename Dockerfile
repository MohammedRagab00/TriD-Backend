# Use official Java image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy jar file into container
COPY target/*.jar app.jar

# Expose port
EXPOSE 8080

# Run application
CMD ["java", "-jar", "app.jar"]
