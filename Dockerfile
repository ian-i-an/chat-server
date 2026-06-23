FROM amazoncorretto:21-alpine

RUN addgroup -S spring && adduser -S spring -G spring

COPY build/libs/*.jar app.jar

USER spring:spring

ENTRYPOINT ["sh", "-c", "exec java $JVM_OPTS -jar app.jar"]