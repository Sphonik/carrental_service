# Stage 1: build the User Service JAR
FROM openjdk:22-jdk AS build

RUN apt-get update \
 && apt-get install -y maven \
 && rm -rf /var/lib/apt/lists/*

WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: runtime image
FROM openjdk:22-jdk-slim
WORKDIR /app
COPY --from=build /app/target/user-service-1.0.0.jar ./app.jar

EXPOSE 8090
ENTRYPOINT ["java", "-jar", "app.jar"]
