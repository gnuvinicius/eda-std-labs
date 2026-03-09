package br.dev.garage474.mscatalog.models;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class Dimensions {
    private Double width;
    private Double height;
    private Double depth;
}
