FROM openjdk:17
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} backend-gateway-service-0.0.1-SNAPSHOT.jar.jar
ENTRYPOINT ["java","-jar","/backend-gateway-service-0.0.1-SNAPSHOT.jar.jar"]