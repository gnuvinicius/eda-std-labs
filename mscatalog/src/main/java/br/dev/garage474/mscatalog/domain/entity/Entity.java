package br.dev.garage474.mscatalog.domain.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public abstract class Entity {

    private UUID tenantId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
