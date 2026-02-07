package br.dev.garage474.mscatalog.domain.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class Collection {

    private UUID id;
    private String name;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
