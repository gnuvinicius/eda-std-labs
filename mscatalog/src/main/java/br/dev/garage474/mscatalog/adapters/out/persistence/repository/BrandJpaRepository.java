package br.dev.garage474.mscatalog.adapters.out.persistence.repository;

import br.dev.garage474.mscatalog.adapters.out.persistence.entity.BrandEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BrandJpaRepository extends JpaRepository<BrandEntity, UUID> {

    Optional<BrandEntity> findByName(String name);

    Optional<BrandEntity> findByIdAndTenantId(UUID id, UUID tenantId);

    List<BrandEntity> findAllByTenantId(UUID tenantId);

    void deleteByIdAndTenantId(UUID id, UUID tenantId);
}

