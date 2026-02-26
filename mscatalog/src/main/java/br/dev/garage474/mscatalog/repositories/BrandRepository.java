package br.dev.garage474.mscatalog.repositories;

import br.dev.garage474.mscatalog.models.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BrandRepository extends JpaRepository<Brand, UUID> {

    Optional<Brand> findById(UUID id);

    Page<Brand> findAll(Pageable pageable);
}
