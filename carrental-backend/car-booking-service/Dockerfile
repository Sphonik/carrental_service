# Dockerfile for Car Booking Service

FROM openjdk:22-slim

# Maven kur
RUN apt-get update && apt-get install -y maven

# Çalışma dizini
WORKDIR /app

# Proje dosyalarını kopyala
COPY . .

EXPOSE 8100

CMD ["java", "-jar", "target/car-booking-service-1.0.0.jar"]
