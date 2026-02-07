package br.dev.garage474.mscatalog.infrastructure.repository;

import br.dev.garage474.mscatalog.domain.CatalogRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

@Repository
public class JpaCatalogRepository implements CatalogRepository {

    private final SpringDataProductRepository repository;

    public JpaCatalogRepository(SpringDataProductRepository repository) {
        this.repository = repository;
    }

    public List<Product> findAllByTenant(UUID tenantId) {
        // Mocked as infrastructure is not the focus now
        return List.of();
    }

    public Optional<Product> findByIdAndTenant(UUID id, UUID tenantId) {
        return Optional.empty();
    }

    @Transactional
    public Product save(Product product) {
        return product;
    }

    @Transactional
    public void deleteByIdAndTenant(UUID id, UUID tenantId) {
    }
}
