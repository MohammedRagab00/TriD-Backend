FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /app
COPY target/*.jar app.jar

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/app.jar app.jar

EXPOSE 8080
CMD ["java", "-jar", "app.jar"]

# ---- ---- ---- if we don't have .jar file ---- ---- ----
#FROM maven:3.9.9-eclipse-temurin-17 AS build
#WORKDIR /build
#COPY src /build/src
#COPY pom.xml /build/pom.xml
#RUN mvn clean package

#FROM eclipse-temurin:17-jre-alpine
#COPY --from=build /build/target/*.jar /apt/app.jar
#CMD ["java", "-jar", "/apt/app.jar"]