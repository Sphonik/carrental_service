# Java 22 JDK kullan
FROM eclipse-temurin:22-jdk

# Maven kur
RUN apt-get update && apt-get install -y maven

# Çalışma dizini
WORKDIR /app

# Proje dosyalarını kopyala
COPY . .

# 8090 portunu aç
EXPOSE 8090

# JAR'ı çalıştır
CMD ["java", "-jar", "target/user-service-1.0.0.jar"]
