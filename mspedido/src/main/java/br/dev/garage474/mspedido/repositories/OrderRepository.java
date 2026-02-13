package br.dev.garage474.mspedido.repositories;

import br.dev.garage474.mspedido.models.Order;
import br.dev.garage474.mspedido.models.OrderStatus;
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
     * Lista todos os pedidos de um tenant.
     */
    Page<Order> findByTenantId(UUID tenantId, Pageable pageable);

    /**
     * Lista todos os pedidos de um cliente e tenant.
     */
    Page<Order> findByTenantIdAndCustomerId(UUID tenantId, UUID customerId, Pageable pageable);

    /**
     * Busca um pedido por id e tenant.
     */
    Optional<Order> findByIdAndTenantId(UUID id, UUID tenantId);

    /**
     * Busca um pedido por cart_id e tenant.
     */
    Optional<Order> findByCartIdAndTenantId(UUID cartId, UUID tenantId);

    /**
     * Lista pedidos por status e tenant.
     */
    Page<Order> findByTenantIdAndStatus(UUID tenantId, OrderStatus status, Pageable pageable);
}

