FROM openjdk:17
ARG JAR_FILE=target/validations-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE} springboot-template-api-v1.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/springboot-template-api-v1.jar"]