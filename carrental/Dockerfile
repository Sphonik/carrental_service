FROM eclipse-temurin:22-jdk

# Maven’ı manuel olarak kuracağız çünkü Maven+Java22 base image yok
RUN apt-get update && apt-get install -y maven

WORKDIR /app
COPY . .

RUN mvn clean package -DskipTests

EXPOSE 8080
CMD ["java", "-jar", "target/car-rental-service-1.0.0.jar"]
