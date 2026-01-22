FROM eclipse-temurin:17-jdk AS build
WORKDIR /app
COPY mvnw mvnw
COPY mvnw.cmd mvnw.cmd
COPY .mvn .mvn
COPY pom.xml pom.xml
RUN ./mvnw -q -DskipTests dependency:go-offline
COPY src src
COPY category category
RUN ./mvnw -q -DskipTests clean package


FROM eclipse-temurin:17-jre
WORKDIR /app
ENV PORT=8080
COPY --from=build /app/target/*.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
