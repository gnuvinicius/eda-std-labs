package br.dev.garage474.mscatalog.domain.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AttributeValue {

    private UUID id;
    private String value;
    private Attribute attribute;
}
