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
 * DTO para criação de um carrinho.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCartDto {

    @NotNull(message = "customer_id é obrigatório")
    @JsonProperty("customer_id")
    private UUID customerId;
}

