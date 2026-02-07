package br.dev.garage474.mscatalog.adapter.out.persistence;

import br.dev.garage474.mscatalog.domain.repository.CatalogRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

@Repository
public class JpaCatalogRepository implements CatalogRepository {

    private final ProductJpaRepository repository;

    public JpaCatalogRepository(ProductJpaRepository repository) {
        this.repository = repository;
    }

    public List<ProductEntity> findAllByTenant(UUID tenantId) {
        // Mocked as infrastructure is not the focus now
        return List.of();
    }

    public Optional<ProductEntity> findByIdAndTenant(UUID id, UUID tenantId) {
        return Optional.empty();
    }

    @Transactional
    public ProductEntity save(ProductEntity productEntity) {
        return productEntity;
    }

    @Transactional
    public void deleteByIdAndTenant(UUID id, UUID tenantId) {
    }
}
