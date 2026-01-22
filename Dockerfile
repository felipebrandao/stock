# syntax=docker/dockerfile:1

FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

COPY pom.xml .
COPY category ./category
COPY src ./src

RUN mvn -DskipTests clean package

FROM eclipse-temurin:17-jre
WORKDIR /app

# Render injeta PORT em runtime; a app já usa server.port=${PORT:8080}
EXPOSE 8080

COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
