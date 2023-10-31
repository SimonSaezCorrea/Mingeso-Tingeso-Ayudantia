FROM openjdk:17
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} backend-estudiantes-service-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/backend-estudiantes-service-0.0.1-SNAPSHOT.jar"]