package br.dev.garage474.msorder.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "order_item_seq", sequenceName = "order_item_seq", initialValue = 1, allocationSize = 1)
    private Long id;

    @Column(name = "product_id")
    private UUID productId;

    @Column(name = "product_name")
    private String productName;

    private Integer quantity;

    @Column(name = "unit_price")
    private BigDecimal unitPrice;

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
