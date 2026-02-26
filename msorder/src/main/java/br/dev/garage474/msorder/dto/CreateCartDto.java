package br.dev.garage474.msorder.dto;

import br.dev.garage474.msorder.models.Cart;
import br.dev.garage474.msorder.models.CartStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

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

    public Cart mapToEntity() {
        Cart cart = new Cart();
        cart.setCustomerId(this.customerId);
        cart.setStatus(CartStatus.ACTIVE);
        return cart;
    }
}

