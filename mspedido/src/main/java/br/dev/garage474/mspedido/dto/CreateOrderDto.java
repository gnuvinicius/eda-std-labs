package br.dev.garage474.mspedido.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * DTO para criação de um pedido a partir do carrinho.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOrderDto {

    @NotNull(message = "cart_id é obrigatório")
    @JsonProperty("cart_id")
    private UUID cartId;

    @NotNull(message = "customer_id é obrigatório")
    @JsonProperty("customer_id")
    private UUID customerId;
}

