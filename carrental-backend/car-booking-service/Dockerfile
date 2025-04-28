# Dockerfile for Car Booking Service

FROM openjdk:22-slim

WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

COPY --from=build /app/target/*.jar app.jar
EXPOSE 8100
ENTRYPOINT ["java", "-jar", "app.jar"]