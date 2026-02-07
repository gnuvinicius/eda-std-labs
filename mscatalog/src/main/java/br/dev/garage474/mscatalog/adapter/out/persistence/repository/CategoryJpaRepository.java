package br.dev.garage474.mscatalog.adapter.out.persistence.repository;

import br.dev.garage474.mscatalog.adapter.out.persistence.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryJpaRepository extends JpaRepository<CategoryEntity, UUID> {

    Optional<CategoryEntity> findByName(String name);

    List<CategoryEntity> findByParentIsNull();

    List<CategoryEntity> findByParentId(UUID parentId);

    Optional<CategoryEntity> findByIdAndTenantId(UUID id, UUID tenantId);

    void deleteByIdAndTenantId(UUID id, UUID tenantId);
}

