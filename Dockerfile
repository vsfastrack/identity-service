# Use the official OpenJDK 8 base image
FROM adoptopenjdk:8-jre-hotspot

# Set the working directory in the container
WORKDIR /app

# Copy the project files into the container
COPY pom.xml .
COPY src ./src

# Specify how to run the application
CMD ["java", "-jar", "target/identityService-0.0.1-SNAPSHOT.jar"]