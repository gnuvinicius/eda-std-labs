package br.dev.garage474.mscatalog.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Embeddable
public class Money {
    @Column(precision = 19, scale = 4)
    private BigDecimal amount;
    private String currency;
}
