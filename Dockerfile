#Stage 01: Build
FROM maven:3.9.6-amazoncorretto-21 AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests

#Stage 02: Runtime
FROM amazoncorretto:21
WORKDIR /app
COPY --from=builder /app/target/campusio-0.0.1-SNAPSHOT.jar application.jar
ENTRYPOINT ["java", "-jar", "application.jar"]