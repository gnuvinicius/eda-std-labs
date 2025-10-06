package br.dev.garage474.infrastructure.messaging;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class UserEventConsumer {

    @Incoming("user-events")
    public void consumeUserEvent(String message) {

        System.out.println("Received user event: " + message);
    }
}
