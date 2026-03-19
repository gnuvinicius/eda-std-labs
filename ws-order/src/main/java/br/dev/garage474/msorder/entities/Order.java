package br.dev.garage474.msorder.entities;

import br.dev.garage474.msorder.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Entidade agregada representando um Pedido no sistema de ‘e-commerce’.
 * Implementa o padrão de Agregado do DDD.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "customer_id")
    private UUID customerId;

    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.PENDING;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id")
    private List<OrderItem> items = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "shipping_address_id")
    private Address shippingAddress;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "delivered_at")
    private LocalDateTime deliveredAt;

    @Column(name = "customer_notes")
    private String customerNotes;

    @Column(name = "shipping_cost")
    private BigDecimal shippingCost = BigDecimal.ZERO;

    @Column(name = "total_discount")
    private BigDecimal totalDiscount = BigDecimal.ZERO;

    @Column(name = "last_status")
    @Enumerated(EnumType.STRING)
    private OrderStatus lastStatus;

    @Column(name = "cancellation_reason")
    private String cancellationReason;

    /**
     * Construtor simplificado para criação de pedidos.
     */
    public Order(UUID customerId, List<OrderItem> items, Address shippingAddress, Payment payment) {
        this.customerId = customerId;
        this.items = items;
        this.shippingAddress = shippingAddress;
        this.payment = payment;
        this.createdAt = LocalDateTime.now();
    }

    /**
     * Calcula o subtotal de todos os itens do pedido.
     * @return BigDecimal com o valor do subtotal
     */
    public BigDecimal calculateSubtotal() {
        return items.stream()
                .map(OrderItem::calculateSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Calcula o total do pedido incluindo frete e descontos.
     * @return BigDecimal com o valor total
     */
    public BigDecimal calculateTotal() {
        BigDecimal subtotal = calculateSubtotal();
        BigDecimal total = subtotal.add(shippingCost).subtract(totalDiscount);
        return total.max(BigDecimal.ZERO);
    }

    /**
     * Confirma o pedido, alterando seu status para CONFIRMED.
     */
    public void confirm() {
        validateStatusTransition(OrderStatus.CONFIRMED);
        this.lastStatus = this.status;
        this.status = OrderStatus.CONFIRMED;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Marca o pedido como em processamento.
     */
    public void process() {
        validateStatusTransition(OrderStatus.PROCESSING);
        this.lastStatus = this.status;
        this.status = OrderStatus.PROCESSING;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Marca o pedido como enviado.
     */
    public void ship() {
        validateStatusTransition(OrderStatus.SHIPPED);
        this.lastStatus = this.status;
        this.status = OrderStatus.SHIPPED;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Marca o pedido como entregue.
     */
    public void deliver() {
        validateStatusTransition(OrderStatus.DELIVERED);
        this.lastStatus = this.status;
        this.status = OrderStatus.DELIVERED;
        this.deliveredAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Cancela o pedido.
     * @param reason motivo do cancelamento
     */
    public void cancel(String reason) {
        if (isDelivered()) {
            throw new IllegalStateException("Não é possível cancelar um pedido já entregue");
        }
        this.lastStatus = this.status;
        this.status = OrderStatus.CANCELLED;
        this.cancellationReason = reason;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Marca o pedido como com falha.
     * @param reason motivo da falha
     */
    public void markAsFailed(String reason) {
        this.lastStatus = this.status;
        this.status = OrderStatus.FAILED;
        this.cancellationReason = reason;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Valida se a transição de status é permitida.
     * @param newStatus novo status
     */
    private void validateStatusTransition(OrderStatus newStatus) {
        switch (this.status) {
            case PENDING:
                if (!Arrays.asList(OrderStatus.CONFIRMED, OrderStatus.CANCELLED).contains(newStatus)) {
                    throw new IllegalStateException("Transição inválida de PENDING para " + newStatus);
                }
                break;
            case CONFIRMED:
                if (!Arrays.asList(OrderStatus.PROCESSING, OrderStatus.CANCELLED).contains(newStatus)) {
                    throw new IllegalStateException("Transição inválida de CONFIRMED para " + newStatus);
                }
                break;
            case PROCESSING:
                if (!Arrays.asList(OrderStatus.SHIPPED, OrderStatus.FAILED).contains(newStatus)) {
                    throw new IllegalStateException("Transição inválida de PROCESSING para " + newStatus);
                }
                break;
            case SHIPPED:
                if (!OrderStatus.DELIVERED.equals(newStatus)) {
                    throw new IllegalStateException("Transição inválida de SHIPPED para " + newStatus);
                }
                break;
            default:
                throw new IllegalStateException("Transição a partir de " + this.status + " não permitida");
        }
    }

    /**
     * Verifica se o pedido foi entregue.
     * @return true se o pedido foi entregue, false caso contrário
     */
    public boolean isDelivered() {
        return OrderStatus.DELIVERED.equals(this.status);
    }

    /**
     * Verifica se o pedido foi cancelado.
     * @return true se o pedido foi cancelado, false caso contrário
     */
    public boolean isCancelled() {
        return OrderStatus.CANCELLED.equals(this.status);
    }

    /**
     * Verifica se o pedido pode ser cancelado.
     * @return true se o pedido pode ser cancelado, false caso contrário
     */
    public boolean canBeCancelled() {
        return !isDelivered() && !isCancelled();
    }

    /**
     * Adiciona um novo item ao pedido.
     * @param item item a ser adicionado
     */
    public void addItem(OrderItem item) {
        if (item == null) {
            throw new IllegalArgumentException("Item não pode ser nulo");
        }
        this.items.add(item);
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Remove um item do pedido pelo ID do produto.
     * @param productId ID do produto
     */
    public void removeItem(UUID productId) {
        this.items.removeIf(item -> item.getProductId().equals(productId));
        this.updatedAt = LocalDateTime.now();
    }
}
