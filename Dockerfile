# Build stage
FROM eclipse-temurin:21-jdk-alpine as build

WORKDIR /app

# Các file ít thay đổi → cache tốt
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn
RUN ./mvnw dependency:go-offline -B

# Các file hay thay đổi → đặt sau
COPY src src
RUN ./mvnw clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
