package br.dev.garage474.mscatalog.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpringDataProductRepository extends JpaRepository<Product, UUID> {
    List<Product> findByTenantId(UUID tenantId);

    Optional<Product> findByIdAndTenantId(UUID id, UUID tenantId);

    void deleteByIdAndTenantId(UUID id, UUID tenantId);
}
