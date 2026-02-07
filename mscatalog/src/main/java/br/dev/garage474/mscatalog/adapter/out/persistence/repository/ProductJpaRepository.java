package br.dev.garage474.mscatalog.adapter.out.persistence.repository;

import br.dev.garage474.mscatalog.adapter.out.persistence.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductJpaRepository extends JpaRepository<ProductEntity, UUID> {

    List<ProductEntity> findByTenantId(UUID tenantId);

    Optional<ProductEntity> findByIdAndTenantId(UUID id, UUID tenantId);

    Optional<ProductEntity> findBySlug(String slug);

    void deleteByIdAndTenantId(UUID id, UUID tenantId);
}

