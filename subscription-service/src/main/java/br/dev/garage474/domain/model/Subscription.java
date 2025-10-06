package br.dev.garage474.domain.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter
@Table(name = "subscriptions")
public class Subscription extends PanacheEntity {

    private Long userId;
    private String planName;
    private final Instant startDate = Instant.now();
    private Instant endDate;

    @Setter
    private EnumStatus status = EnumStatus.ACTIVE;

    public enum EnumStatus {
        ACTIVE,
        CANCELED,
        EXPIRED
    }

    public static Subscription of(Long userId, String planName, Instant endDate) {
        Subscription sub = new Subscription();
        sub.userId = userId;
        sub.planName = planName;
        sub.endDate = endDate;
        return sub;
    }
}
