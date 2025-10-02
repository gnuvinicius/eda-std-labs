package com.clubedeassinaturas.messaging;

import com.clubedeassinaturas.entity.OutboxEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import java.util.List;

@ApplicationScoped
public class OutboxPublisher {

    @Channel("outbox-events")
    Emitter<String> emitter;

    @Transactional
    public void publishPendingEvents() {
        List<OutboxEvent> events = OutboxEvent.list("published", false);
        for (OutboxEvent event : events) {
            emitter.send(event.getPayload());
            event.setPublished(true);
        }
    }
}
