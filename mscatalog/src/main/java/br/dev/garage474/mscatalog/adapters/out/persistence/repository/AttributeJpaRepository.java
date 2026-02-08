package br.dev.garage474.mscatalog.adapters.out.persistence.repository;

import br.dev.garage474.mscatalog.adapters.out.persistence.entity.AttributeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface AttributeJpaRepository extends JpaRepository<AttributeEntity, UUID> {

    Optional<AttributeEntity> findByName(String name);

    Optional<AttributeEntity> findByIdAndTenantId(UUID id, UUID tenantId);

    void deleteByIdAndTenantId(UUID id, UUID tenantId);
}

