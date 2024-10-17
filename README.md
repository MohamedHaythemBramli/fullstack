# Spring Boot Application

This is a Spring Boot application designed with a set of features to handle data, validation, caching, security, file storage, monitoring, and more.

## Features

- **Data Management**: Supports JDBC, JPA, and MySQL with Flyway for database migrations.
- **Validation**: Bean validation with Hibernate Validator.
- **Web**: RESTful APIs using Spring Web.
- **Caching**: Integrated Redis support.
- **File Storage**: MinIO integration for object storage.
- **Monitoring & Tracing**: Micrometer with Prometheus and Brave for distributed tracing, Loki for log aggregation.
- **Security**: JWT-based security with Spring Security.
- **API Documentation**: Integrated OpenAPI with SpringDoc.
- **Email Support**: Configured for sending emails using Spring Mail.
- **Template Engine**: Thymeleaf for rendering server-side views.


## Deployment 
- git clone https://github.com/MohamedHaythemBramli/fullstack.git
- cd fullstack
- mvn clean install
- docker-compose up -d --build

# Next Tasks
- **Spring Batch**: Process JSON, XML files, and data from REST APIs.
- **Spring Integration**: Poll directories for new files and trigger processing.
- **Apache Kafka**: Produce messages to Kafka after processing.
- **AWS S3**: Upload processed files to an S3 bucket using HttpHeaders.
