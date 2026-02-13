package br.dev.garage474.mspedido.dto;

import br.dev.garage474.mspedido.models.Order;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * DTO para transferência de dados de um pedido.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDto {

    @JsonProperty("id")
    private UUID id;

    @JsonProperty("cart_id")
    private UUID cartId;

    @JsonProperty("customer_id")
    private UUID customerId;

    @JsonProperty("status")
    private String status;

    @JsonProperty("total_amount")
    private BigDecimal totalAmount;

    @JsonProperty("total_currency")
    private String totalCurrency;

    @JsonProperty("items")
    private List<OrderItemDto> items;

    @JsonProperty("total_items")
    private Integer totalItems;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    public static OrderDto toDto(Order order) {
        return OrderDto.builder()
                .id(order.getId())
                .cartId(order.getCartId())
                .customerId(order.getCustomerId())
                .status(order.getStatus().toString())
                .totalAmount(order.getTotal().getAmount())
                .totalCurrency(order.getTotal().getCurrency())
                .items(OrderItemDto.mapOrderItemsToDto(order.getItems()))
                .totalItems(order.getTotalItems())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }
}

