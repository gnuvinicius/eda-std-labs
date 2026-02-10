package br.dev.garage474.mscatalog.adapters.in.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * DTO para resposta de Showcase de Produtos.
 * Utilizado para respostas ao frontend/mobile de forma otimizada.
 * Contém apenas os dados essenciais para exibição em lista e detalhe.
 */
public record ShowcaseProductResponse(
        UUID id,
        String name,
        String description,
        String slug,
        BrandResponse brand,
        CategoryResponse category,
        List<String> tags,
        ProductPriceResponse price,
        List<ProductVariantResponse> variants
) implements Serializable {

    public record BrandResponse(
            UUID id,
            String name
    ) {
    }

    public record CategoryResponse(
            UUID id,
            String name
    ) {
    }

    public record ProductPriceResponse(
            MoneyResponse basePrice,
            MoneyResponse lowestVariantPrice,
            MoneyResponse lowestPromotionalPrice
    ) implements Serializable {

        public record MoneyResponse(
                BigDecimal amount,
                String currency
        ) {
        }
    }

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
        ) {
        }

        public record DimensionsResponse(
                Double weight,
                Double height,
                Double width,
                Double depth
        ) {
        }
    }
}


