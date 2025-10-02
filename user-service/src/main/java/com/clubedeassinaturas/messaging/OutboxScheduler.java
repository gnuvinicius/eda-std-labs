package com.clubedeassinaturas.messaging;

import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class OutboxScheduler {

    @Inject
    OutboxPublisher publisher;

    @Scheduled(every = "10s")
    void processOutbox() {
        publisher.publishPendingEvents();
    }

}
