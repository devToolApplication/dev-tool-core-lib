FROM eclipse-temurin:17-jdk AS build

WORKDIR /app

# CÁC DÒNG ÍT THAY ĐỔI ĐẶT TRÊN
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn
RUN ./mvnw dependency:go-offline -B

# DÒNG NÀY THƯỜNG XUYÊN THAY ĐỔI → ĐẶT SAU
COPY src src
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:17-jre
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
