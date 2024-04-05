# Use a Maven image as the build environment
FROM maven:3-openjdk-17 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the entire project to the container
COPY . .

# Build the Maven project, skipping tests
RUN mvn clean package -DskipTests

# Use a smaller base image for the runtime environment
FROM openjdk:17.0.1-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file built in the previous stage to the container
COPY --from=build /app/target/car-rental-0.0.1-SNAPSHOT.jar ./app.jar

# Expose the port on which your Spring Boot app will run (change as needed)
EXPOSE 8080

# Default to using the development environment properties
ARG SPRING_PROFILES_ACTIVE=development

# Copy the application.properties file based on the SPRING_PROFILES_ACTIVE build argument
COPY src/main/resources/application-${SPRING_PROFILES_ACTIVE}.properties ./application.properties

# Command to run the Spring Boot application when the container starts
CMD ["java", "-jar", "app.jar"]
