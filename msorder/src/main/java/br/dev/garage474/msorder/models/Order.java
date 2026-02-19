package br.dev.garage474.msorder.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Entity que representa um Pedido.
 * Aggregate root para o contexto de Pedidos.
 */
@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order extends BaseEntity {

    @Id
    private UUID id;

    @Column(name = "cart_id")
    private UUID cartId;

    @Column(name = "customer_id", nullable = false)
    private UUID customerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "total_amount")),
            @AttributeOverride(name = "currency", column = @Column(name = "total_currency"))
    })
    private Money total;

    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<OrderItem> items = new ArrayList<>();

    /**
     * Adiciona um item ao pedido.
     */
    public void addItem(OrderItem orderItem) {
        orderItem.setOrder(this);
        this.items.add(orderItem);
    }

    /**
     * Remove um item do pedido.
     */
    public void removeItem(OrderItem orderItem) {
        this.items.remove(orderItem);
        orderItem.setOrder(null);
    }

    /**
     * Calcula o total do pedido.
     */
    public void calculateTotal() {
        BigDecimal totalAmount = this.items.stream()
                .map(item -> item.getUnitPrice().getAmount()
                        .multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        String currency = this.items.isEmpty() ? "BRL" :
                this.items.get(0).getUnitPrice().getCurrency();

        this.total = new Money(totalAmount, currency);
    }

    /**
     * Retorna a quantidade total de itens no pedido.
     */
    public int getTotalItems() {
        return this.items.stream()
                .mapToInt(OrderItem::getQuantity)
                .sum();
    }

    public void cancel() {
        this.status = OrderStatus.CANCELLED;
        this.updatedAt = LocalDateTime.now();
    }
}

