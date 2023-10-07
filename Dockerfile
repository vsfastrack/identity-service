FROM maven:3.6.3 AS maven
LABEL MAINTAINER="vaibhav.saxena_@outlook.com"

WORKDIR /usr/src/app
COPY . /usr/src/app
# Compile and package the application to an executable JAR
RUN mvn package

# For Java 8,
FROM adoptopenjdk:8-jre-hotspot

ARG JAR_FILE=identityService-0.0.1-SNAPSHOT.jar

WORKDIR /opt/app

# Copy the spring-boot-api-tutorial.jar from the maven stage to the /opt/app directory of the current stage.
COPY --from=maven /usr/src/app/target/${JAR_FILE} /opt/app/

ENTRYPOINT ["java","-jar","identityService-0.0.1-SNAPSHOT.jar"]