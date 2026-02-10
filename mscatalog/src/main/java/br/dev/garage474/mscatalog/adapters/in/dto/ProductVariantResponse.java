package br.dev.garage474.mscatalog.adapters.in.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * DTO para resposta de ProductVariant.
 * Utilizado como saída nos endpoints REST.
 */
public record ProductVariantResponse(
    UUID id,
    UUID productId,
    String skuCode,
    String barcode,
    MoneyResponse price,
    MoneyResponse promotionalPrice,
    DimensionsResponse dimensions
) implements Serializable {

    public record MoneyResponse(
        BigDecimal amount,
        String currency
    ) {}

    public record DimensionsResponse(
        Double weight,
        Double height,
        Double width,
        Double depth
    ) {}
}

