package br.dev.garage474.mspedido.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * DTO para criação de um item do carrinho.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCartItemDto {

    @NotNull(message = "product_id é obrigatório")
    @JsonProperty("product_id")
    private UUID productId;

    @NotNull(message = "product_variant_id é obrigatório")
    @JsonProperty("product_variant_id")
    private UUID productVariantId;

    @NotNull(message = "quantity é obrigatório")
    @Positive(message = "quantity deve ser maior que zero")
    @JsonProperty("quantity")
    private Integer quantity;

    @NotNull(message = "unit_price_amount é obrigatório")
    @JsonProperty("unit_price_amount")
    private BigDecimal unitPriceAmount;

    @NotNull(message = "unit_price_currency é obrigatório")
    @JsonProperty("unit_price_currency")
    private String unitPriceCurrency;
}

