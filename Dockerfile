FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /build
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN ./mvnw -B dependency:go-offline --fail-never -Dmaven.wagon.http.retryHandler.count=3 -Dmaven.wagon.http.pool=false
COPY src ./src
RUN ./mvnw -B clean package -DskipTests -Dmaven.wagon.http.retryHandler.count=3 -Dmaven.wagon.http.pool=false

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /build/target/*.jar app.jar

RUN addgroup --system javauser && adduser -S -s /bin/false -G javauser javauser
USER javauser

EXPOSE 8080
ENTRYPOINT ["java", "-Xms256m", "-Xmx512m", "-jar", "/app/app.jar"]