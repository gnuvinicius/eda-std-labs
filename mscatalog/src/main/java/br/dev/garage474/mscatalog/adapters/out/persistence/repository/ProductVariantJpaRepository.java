package br.dev.garage474.mscatalog.adapters.out.persistence.repository;

import br.dev.garage474.mscatalog.adapters.out.persistence.entity.ProductVariantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductVariantJpaRepository extends JpaRepository<ProductVariantEntity, UUID> {

    List<ProductVariantEntity> findByProductEntityId(UUID productId);

    Optional<ProductVariantEntity> findBySkuCode(String skuCode);

    Optional<ProductVariantEntity> findByBarcode(String barcode);

    void deleteByProductEntityId(UUID productId);

    long countByProductEntityId(UUID productId);

    Optional<ProductVariantEntity> findByIdAndTenantId(UUID id, UUID tenantId);

    void deleteByIdAndTenantId(UUID id, UUID tenantId);
}

