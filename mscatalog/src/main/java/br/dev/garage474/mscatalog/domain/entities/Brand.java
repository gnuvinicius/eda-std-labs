package br.dev.garage474.mscatalog.domain.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class Brand extends Entity {

    private UUID id;
    private String name;
}
