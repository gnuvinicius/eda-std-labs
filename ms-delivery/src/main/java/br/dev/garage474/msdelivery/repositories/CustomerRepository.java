package br.dev.garage474.msdelivery.repositories;

import br.dev.garage474.msdelivery.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {

    boolean existsByEmail(String email);

    boolean existsByDocument(String document);
}

