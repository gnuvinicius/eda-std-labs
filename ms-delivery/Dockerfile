FROM eclipse-temurin:21-jre-alpine

RUN addgroup -S appgroup && adduser -S appuser -G appgroup

USER appuser

COPY target/ms-delivery.jar /app/msdelivery.jar

ENV SPRING_PROFILES_ACTIVE=prod \
    SPRING_PROFILES_ACTIVE=prod \
    SPRING_DATASOURCE_URL=jdbc:postgresql://192.168.122.79:5432/msdelivery_db \
    SPRING_DATASOURCE_USERNAME=postgres \
    SPRING_DATASOURCE_PASSWORD=2AkByM4NfHFkeJz \
    RABBITMQ_HOST=rabbitmq \
    RABBITMQ_PORT=5672 \
    RABBITMQ_USERNAME=garage_user \
    RABBITMQ_PASSWORD=garage_password \
    RABBITMQ_VHOST=/ \
    ORDER_EVENTS_EXCHANGE=order.events.exchange \
    ORDER_CREATED_QUEUE=order.created.v1.queue \
    ORDER_CREATED_ROUTING_KEY=order.created.v1 \
    ORDER_EVENTS_DLX=order.events.dlx \
    ORDER_CREATED_DLQ=order.created.v1.dlq \
    ORDER_CREATED_DLQ_ROUTING_KEY=order.created.v1.dlq \
    RABBITMQ_PUBLISH_ENABLED=true

EXPOSE 8080

ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "/app/msdelivery.jar"]

