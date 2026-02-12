package br.dev.garage474.mscatalog.repositories;

import br.dev.garage474.mscatalog.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

    Optional<Product> findByIdAndTenantId(UUID id, UUID tenantId);

    Page<Product> findAllByTenantId(UUID tenantId, Pageable pageable);

    List<Product> findAllByTenantIdAndCategoryId(UUID tenantId, UUID categoryId);

    List<Product> findAllByTenantIdAndBrandId(UUID tenantId, UUID brandId);

    boolean existsByIdAndTenantId(UUID id, UUID tenantId);
}
