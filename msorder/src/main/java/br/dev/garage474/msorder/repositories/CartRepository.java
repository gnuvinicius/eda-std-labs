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
     * Busca um carrinho ativo por cliente.
     */
    Optional<Cart> findByCustomerIdAndStatus(UUID customerId, CartStatus status);

    /**
     * Lista todos os carrinhos de um cliente.
     */
    Page<Cart> findByCustomerId(UUID customerId, Pageable pageable);

    /**
     * Busca um carrinho por id.
     */
    Optional<Cart> findById(UUID id);

    /**
     * Lista carrinhos abandonados por status.
     */
    List<Cart> findByStatus(CartStatus status);
}

