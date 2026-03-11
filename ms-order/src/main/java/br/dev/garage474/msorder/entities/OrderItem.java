package br.dev.garage474.msorder.entities;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

    @NotNull(message = "ID do produto é obrigatório")
    private UUID productId;

    @NotBlank(message = "Nome do produto é obrigatório")
    private String productName;

    @NotNull(message = "Quantidade é obrigatória")
    @PositiveOrZero(message = "Quantidade deve ser maior que zero")
    private Integer quantity;

    @NotNull(message = "Preço unitário é obrigatório")
    @PositiveOrZero(message = "Preço unitário deve ser maior ou igual a zero")
    private BigDecimal unitPrice;

    @PositiveOrZero(message = "Desconto deve ser maior ou igual a zero")
    private BigDecimal discount;

    /**
     * Calcula o subtotal do item (quantidade * preço unitário - desconto).
     * @return BigDecimal com o valor do subtotal
     */
    public BigDecimal calculateSubtotal() {
        if (discount == null) {
            return unitPrice.multiply(new BigDecimal(quantity));
        }
        return unitPrice.multiply(new BigDecimal(quantity)).subtract(discount);
    }
}
