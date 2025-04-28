# Dockerfile for Car Booking Service

FROM openjdk:22-slim

WORKDIR /app

# !!! Burada doğru JAR adını kullanıyoruz
COPY target/car-booking-service-1.0.0.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]

EXPOSE 8081
