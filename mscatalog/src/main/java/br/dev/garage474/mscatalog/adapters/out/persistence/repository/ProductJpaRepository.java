package br.dev.garage474.mscatalog.adapters.out.persistence.repository;

import br.dev.garage474.mscatalog.adapters.out.persistence.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductJpaRepository extends JpaRepository<ProductEntity, UUID> {

    List<ProductEntity> findByTenantId(UUID tenantId);

    Optional<ProductEntity> findByIdAndTenantId(UUID id, UUID tenantId);

    Optional<ProductEntity> findBySlug(String slug);

    void deleteByIdAndTenantId(UUID id, UUID tenantId);

    /**
     * Busca produtos com filtros opcionais por tenant.
     * Utilizado para o Showcase com suporte a busca por termo, brand e categoria.
     */
    @Query("SELECT p FROM ProductEntity p " +
           "WHERE p.tenantId = :tenantId " +
           "AND (LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "     OR LOWER(p.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
           "AND (:brandId IS NULL OR p.brandEntity.id = :brandId) " +
           "AND (:categoryId IS NULL OR p.categoryEntity.id = :categoryId)")
    Page<ProductEntity> findProductsByTenantWithFilters(
        @Param("tenantId") UUID tenantId,
        @Param("searchTerm") String searchTerm,
        @Param("brandId") UUID brandId,
        @Param("categoryId") UUID categoryId,
        Pageable pageable
    );
}

