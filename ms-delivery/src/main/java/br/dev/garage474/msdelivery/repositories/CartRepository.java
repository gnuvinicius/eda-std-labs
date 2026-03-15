package br.dev.garage474.msdelivery.repositories;

import br.dev.garage474.msdelivery.models.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, UUID> {
}

