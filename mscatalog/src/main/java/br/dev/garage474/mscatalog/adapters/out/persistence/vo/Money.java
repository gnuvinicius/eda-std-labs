package br.dev.garage474.mscatalog.adapters.out.persistence.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.math.BigDecimal;

@Embeddable
public record Money(
    @Column(precision = 19, scale = 4)
    BigDecimal amount,
    String currency
) {
    public static Money of(BigDecimal amount, String currency) {
        return new Money(amount, currency);
    }
}

