package br.dev.garage474.domain.repository;

import br.dev.garage474.domain.model.Subscription;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class SubscriptionRepository implements PanacheRepository<Subscription> {

    public List<Subscription> findByUserId(Long userId) {
        return list("userId", userId);
    }
}
