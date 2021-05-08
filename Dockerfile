FROM amazoncorretto:11-alpine

ARG JAR_FILE=../target/pokemon-app.jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar", "-Dspring.profiles.active=docker", "/app.jar"]
