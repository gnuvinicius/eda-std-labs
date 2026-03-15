package br.dev.garage474.msdelivery.repositories;

import br.dev.garage474.msdelivery.models.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}

