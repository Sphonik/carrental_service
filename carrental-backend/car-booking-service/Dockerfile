# Stage 1: build the Car-Booking Service JAR
FROM eclipse-temurin:22-jdk AS build

RUN apt-get update && \
    apt-get install -y maven && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: runtime image
FROM eclipse-temurin:22-jdk-slim
WORKDIR /app
COPY --from=build /app/target/car-booking-service-1.0.0.jar ./app.jar
EXPOSE 8100
ENTRYPOINT ["java", "-jar", "app.jar"]
