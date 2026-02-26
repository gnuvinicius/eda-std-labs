package br.dev.garage474.msorder.repositories;

import br.dev.garage474.msorder.models.Order;
import br.dev.garage474.msorder.models.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository para a entidade Order.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

    /**
     * Lista todos os pedidos.
     */
    Page<Order> findBy(Pageable pageable);

    /**
     * Lista todos os pedidos de um cliente.
     */
    Page<Order> findByCustomerId(UUID customerId, Pageable pageable);

    /**
     * Busca um pedido por id.
     */
    Optional<Order> findById(UUID id);

    /**
     * Busca um pedido por cart_id.

     */
    Optional<Order> findByCartId(UUID cartId);

    /**
     * Lista pedidos por status.
     */
    Page<Order> findByStatus(OrderStatus status, Pageable pageable);
}

