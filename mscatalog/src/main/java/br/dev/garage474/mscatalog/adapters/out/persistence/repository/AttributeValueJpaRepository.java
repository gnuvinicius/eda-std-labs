package br.dev.garage474.mscatalog.adapters.out.persistence.repository;

import br.dev.garage474.mscatalog.adapters.out.persistence.entity.AttributeValueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AttributeValueJpaRepository extends JpaRepository<AttributeValueEntity, UUID> {

    List<AttributeValueEntity> findByAttributeEntityId(UUID attributeId);

    void deleteByAttributeEntityId(UUID attributeId);

    long countByAttributeEntityId(UUID attributeId);

    Optional<AttributeValueEntity> findByIdAndTenantId(UUID id, UUID tenantId);

    void deleteByIdAndTenantId(UUID id, UUID tenantId);
}

