FROM openjdk:17-jdk
ARG JAR_FILE=build/libs/withUs-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENV SPRING_PROFIELE "product"
ENTRYPOINT ["java","-spring.profiles.active=${SPRING_PROFIELE}","-jar","/app.jar"]