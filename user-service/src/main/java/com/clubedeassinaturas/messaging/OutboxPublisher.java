package com.clubedeassinaturas.messaging;

import com.clubedeassinaturas.entity.OutboxEvent;
import io.cloudevents.CloudEvent;
import io.cloudevents.core.builder.CloudEventBuilder;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class OutboxPublisher {

    @Channel("user-events")
    Emitter<CloudEvent> emitter;

    @Transactional
    public void publishPendingEvents() {
        List<OutboxEvent> events = OutboxEvent.list("published", false);
        for (OutboxEvent event : events) {
            CloudEvent cloudEvent = CloudEventBuilder.v1()
                    .withId(UUID.randomUUID().toString())
                    .withType("user.created")
                    .withSource(URI.create("user-service"))
                    .withData("application/json", event.getPayload().getBytes(StandardCharsets.UTF_8))
                    .build();

            emitter.send(cloudEvent);
            event.setPublished(true);
        }
    }

    public void publishUserDisabledEvent(String userJson) {
        CloudEvent cloudEvent = CloudEventBuilder.v1()
                .withId(UUID.randomUUID().toString())
                .withType("user.disabled")
                .withSource(URI.create("user-service"))
                .withData("application/json", userJson.getBytes(StandardCharsets.UTF_8))
                .build();

        emitter.send(cloudEvent);
    }
}
