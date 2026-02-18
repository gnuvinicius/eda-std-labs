package br.dev.garage474.msorder.repositories;

import br.dev.garage474.msorder.models.Cart;
import br.dev.garage474.msorder.models.CartStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository para a entidade Cart.
 */
@Repository
public interface CartRepository extends JpaRepository<Cart, UUID> {

    /**
     * Busca um carrinho ativo por tenant e cliente.
     */
    Optional<Cart> findByTenantIdAndCustomerIdAndStatus(UUID tenantId, UUID customerId, CartStatus status);

    /**
     * Lista todos os carrinhos de um tenant.
     */
    Page<Cart> findByTenantId(UUID tenantId, Pageable pageable);

    /**
     * Lista todos os carrinhos de um cliente e tenant.
     */
    Page<Cart> findByTenantIdAndCustomerId(UUID tenantId, UUID customerId, Pageable pageable);

    /**
     * Busca um carrinho por id e tenant.
     */
    Optional<Cart> findByIdAndTenantId(UUID id, UUID tenantId);

    /**
     * Lista carrinhos abandonados por tenant.
     */
    List<Cart> findByTenantIdAndStatus(UUID tenantId, CartStatus status);
}

