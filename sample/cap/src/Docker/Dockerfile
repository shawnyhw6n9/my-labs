FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG JAR_FILE
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-Xmx512m","-Xss512k","-jar","/app.jar","-Dspring.profiles.active=dev"]