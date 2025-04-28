# Stage 1: build the Car-Booking Service JAR
FROM maven:3.8.8-eclipse-temurin-22 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: runtime image
FROM eclipse-temurin:22-jdk
WORKDIR /app
COPY --from=build /app/target/car-booking-service-1.0.0.jar ./app.jar
EXPOSE 8100
CMD ["java", "-jar", "app.jar"]
