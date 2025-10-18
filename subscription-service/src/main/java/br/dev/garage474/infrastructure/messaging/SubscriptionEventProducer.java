package br.dev.garage474.infrastructure.messaging;


import br.dev.garage474.domain.model.Subscription;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cloudevents.CloudEvent;
import io.cloudevents.core.builder.CloudEventBuilder;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@ApplicationScoped
public class SubscriptionEventProducer {

    @Channel("subscription-events")
    Emitter<CloudEvent> emitter;

    private static final ObjectMapper mapper = new ObjectMapper();

    public void publishSubscriptionCreated(Subscription sub) throws JsonProcessingException {
        String s = mapper.writeValueAsString(sub);

        CloudEvent event = CloudEventBuilder.v1()
                .withId(UUID.randomUUID().toString())
                .withType("user.created")
                .withSource(URI.create("user-service"))
                .withData("application/json", s.getBytes(StandardCharsets.UTF_8))
                .build();

        emitter.send(event);
    }

    public void publishSubscriptionCanceled(Subscription sub) {
        CloudEvent event = CloudEventBuilder.v1().withId(UUID.randomUUID().toString())
                .build();


        emitter.send(event);
    }
}
