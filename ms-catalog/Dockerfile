FROM eclipse-temurin:21-jre-alpine

RUN addgroup -S appgroup && adduser -S appuser -G appgroup

USER appuser

COPY target/mscatalog-0.0.1-SNAPSHOT.jar /app/mscatalog.jar

ENV SPRING_PROFILES_ACTIVE=prod \
    SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/mscatalog_db \
    SPRING_DATASOURCE_USERNAME=postgres \
    SPRING_DATASOURCE_PASSWORD=postgres \
    SPRING_RABBITMQ_HOST=rabbitmq \
    SPRING_RABBITMQ_PORT=5672 \
    SPRING_RABBITMQ_USERNAME=garage_user \
    SPRING_RABBITMQ_PASSWORD=garage_password \
    SPRING_RABBITMQ_VIRTUAL_HOST=/

EXPOSE 8080

ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "/app/mscatalog.jar"]



