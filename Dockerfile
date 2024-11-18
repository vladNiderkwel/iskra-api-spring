FROM amazoncorretto:17
ARG JAR_FILE=target/*.jar
COPY /build/libs/iskra-api-spring-0.5.jar app.jar
ADD /src/main/images /src/main/images
ENTRYPOINT ["java", "-jar", "/app.jar"]