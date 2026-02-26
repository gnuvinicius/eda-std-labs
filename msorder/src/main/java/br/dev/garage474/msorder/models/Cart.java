package br.dev.garage474.msorder.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Entity que representa um Carrinho de Compras.
 * Aggregate root para o contexto de Carrinho.
 */
@Entity
@Table(name = "cart")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cart extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "customer_id")
    private UUID customerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CartStatus status;

    @OneToMany(
        mappedBy = "cart",
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    private List<CartItem> items = new ArrayList<>();

    /**
     * Adiciona um item ao carrinho.
     */
    public void addItem(CartItem cartItem) {
        cartItem.setCart(this);
        this.items.add(cartItem);
    }

    /**
     * Remove um item do carrinho.
     */
    public void removeItem(CartItem cartItem) {
        this.items.remove(cartItem);
        cartItem.setCart(null);
    }

    /**
     * Limpa todos os itens do carrinho.
     */
    public void clearItems() {
        this.items.clear();
    }

    /**
     * Retorna a quantidade total de itens no carrinho.
     */
    public int getTotalItems() {
        return this.items.stream()
            .mapToInt(CartItem::getQuantity)
            .sum();
    }
}

