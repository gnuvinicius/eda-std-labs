package br.dev.garage474.msorder.dto;

import br.dev.garage474.msorder.models.CartItem;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * DTO para transferência de dados de um item do carrinho.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemDto {

    @JsonProperty("id")
    private UUID id;

    @JsonProperty("product_id")
    private UUID productId;

    @JsonProperty("product_variant_id")
    private UUID productVariantId;

    @JsonProperty("quantity")
    private Integer quantity;

    @JsonProperty("unit_price_amount")
    private BigDecimal unitPriceAmount;

    @JsonProperty("unit_price_currency")
    private String unitPriceCurrency;

    public static List<CartItemDto> mapCartItemsToDto(List<CartItem> items) {
        return items.stream()
                .map(item -> CartItemDto.builder()
                        .id(item.getId())
                        .productId(item.getProductId())
                        .productVariantId(item.getProductVariantId())
                        .quantity(item.getQuantity())
                        .unitPriceAmount(item.getUnitPrice().getAmount())
                        .unitPriceCurrency(item.getUnitPrice().getCurrency())
                        .build())
                .collect(Collectors.toList());
    }
}

