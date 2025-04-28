# Stage 1: build the User Service JAR
FROM maven:3.8.8-eclipse-temurin-22 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: runtime image
FROM eclipse-temurin:22-jdk
WORKDIR /app
COPY --from=build /app/target/user-service-1.0.0.jar ./app.jar
EXPOSE 8090
CMD ["java", "-jar", "app.jar"]
