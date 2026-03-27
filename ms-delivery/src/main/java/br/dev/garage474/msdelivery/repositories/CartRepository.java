package br.dev.garage474.msdelivery.repositories;

import br.dev.garage474.msdelivery.models.Cart;
import br.dev.garage474.msdelivery.models.CartStatus;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, UUID> {

    @Query("select c FROM Cart c WHERE c.customerId = :customerId AND c.status = :status")
    Optional<Cart> findByCustomerId(UUID customerId, CartStatus status);
}

