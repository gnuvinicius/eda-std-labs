package br.dev.garage474.mscatalog.domain.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class Brand {

    private UUID id;
    private String name;
}
