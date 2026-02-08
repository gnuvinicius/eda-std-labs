package br.dev.garage474.mscatalog.adapters.in.web.dto;

import java.math.BigDecimal;

/**
 * DTO para requisição de criação/atualização de ProductVariant.
 */
public record CreateProductVariantRequest(
    String skuCode,
    String barcode,
    BigDecimal price,
    String priceCurrency,
    BigDecimal promotionalPrice,
    String promotionalPriceCurrency,
    Double weight,
    Double height,
    Double width,
    Double depth
) {}

