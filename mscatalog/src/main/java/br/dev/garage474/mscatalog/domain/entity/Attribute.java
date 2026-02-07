package br.dev.garage474.mscatalog.domain.entity;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
public class Attribute {

    private UUID id;

    private String name;
}
