FROM openjdk:21

ARG JAR_FILE=./target/api_project*.jar

COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]
