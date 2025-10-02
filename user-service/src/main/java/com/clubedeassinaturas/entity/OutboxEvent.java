package com.clubedeassinaturas.entity;

import java.time.Instant;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
public class OutboxEvent extends PanacheEntity {

    private String type;
    private String payload;

    @Setter
    private boolean published = false;
    private final Instant createdAt = Instant.now();

    protected OutboxEvent() {
    }

    private OutboxEvent(String type, String payload) {
        this.type = type;
        this.payload = payload;
    }

    public static OutboxEvent of(String type, String payload) {
        return new OutboxEvent(type, payload);
    }

}
