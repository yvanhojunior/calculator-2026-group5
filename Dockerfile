#Build the application
FROM maven:3.9.6-eclipse-temurin-22 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests
#Run the application
FROM eclipse-temurin:22-jre
WORKDIR /app
COPY --from=build /app/target/calculator-cucumber-0.5.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]