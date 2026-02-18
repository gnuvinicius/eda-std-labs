package br.dev.garage474.msorder.dto;

import br.dev.garage474.msorder.models.Cart;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * DTO para transferência de dados de um carrinho.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDto {

    @JsonProperty("id")
    private UUID id;

    @JsonProperty("customer_id")
    private UUID customerId;

    @JsonProperty("status")
    private String status;

    @JsonProperty("items")
    private List<CartItemDto> items;

    @JsonProperty("total_items")
    private Integer totalItems;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    public static CartDto mapToDto(Cart cart) {
        return CartDto.builder()
                .id(cart.getId())
                .customerId(cart.getCustomerId())
                .status(cart.getStatus().toString())
                .items(CartItemDto.mapCartItemsToDto(cart.getItems()))
                .totalItems(cart.getTotalItems())
                .createdAt(cart.getCreatedAt())
                .updatedAt(cart.getUpdatedAt())
                .build();
    }
}

