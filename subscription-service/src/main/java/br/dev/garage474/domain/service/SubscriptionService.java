package br.dev.garage474.domain.service;

import br.dev.garage474.domain.model.Subscription;
import br.dev.garage474.domain.repository.SubscriptionRepository;
import br.dev.garage474.infrastructure.messaging.SubscriptionEventProducer;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class SubscriptionService {

    @Inject
    private SubscriptionRepository repository;

    @Inject
    private SubscriptionEventProducer eventProducer;

    @Transactional
    public Subscription create(Subscription subscription) throws JsonProcessingException {
        repository.persist(subscription);
        eventProducer.publishSubscriptionCreated(subscription);
        return subscription;
    }

    public List<Subscription> listByUser(Long userId) {
        return repository.findByUserId(userId);
    }

    public void cancel(Long id) {
        repository.findByIdOptional(id)
                .ifPresent(subscription -> {
                    subscription.setStatus(Subscription.EnumStatus.CANCELED);
                    repository.persist(subscription);
                    eventProducer.publishSubscriptionCanceled(subscription);
                });
    }
}
