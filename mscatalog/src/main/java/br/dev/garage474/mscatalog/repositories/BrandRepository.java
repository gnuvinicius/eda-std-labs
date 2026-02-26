package br.dev.garage474.mscatalog.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.dev.garage474.mscatalog.models.Brand;

public interface BrandRepository extends JpaRepository<Brand, UUID> {

    Optional<Brand> findById(UUID id);
}
