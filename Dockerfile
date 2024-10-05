FROM openjdk:21-slim

WORKDIR /app

COPY target/*.jar /app/myapp.jar

ENTRYPOINT ["java", "-jar", "/app/myapp.jar"]