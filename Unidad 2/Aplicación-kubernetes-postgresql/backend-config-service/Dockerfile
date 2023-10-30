FROM openjdk:17
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} backend-config-service-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/backend-config-service-0.0.1-SNAPSHOT.jar"]