package br.dev.garage474.msestoque.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpringDataProdutoRepository extends JpaRepository<ProdutoEntity, UUID> {
    List<ProdutoEntity> findByTenantId(UUID tenantId);

    Optional<ProdutoEntity> findByIdAndTenantId(UUID id, UUID tenantId);

    void deleteByIdAndTenantId(UUID id, UUID tenantId);
}
