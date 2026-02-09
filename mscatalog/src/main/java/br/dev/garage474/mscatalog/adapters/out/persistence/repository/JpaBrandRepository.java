package br.dev.garage474.mscatalog.adapters.out.persistence.repository;

import br.dev.garage474.mscatalog.adapters.out.persistence.entity.BrandEntity;
import br.dev.garage474.mscatalog.domain.entities.Brand;
import br.dev.garage474.mscatalog.domain.repositories.BrandRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementação JPA do repositório {@link BrandRepository}.
 *
 * Responsável pela persistência do agregado Brand.
 */
@Repository
public class JpaBrandRepository implements BrandRepository {

    private final BrandJpaRepository brandJpaRepository;

    public JpaBrandRepository(BrandJpaRepository brandJpaRepository) {
        this.brandJpaRepository = brandJpaRepository;
    }

    @Override
    public Brand saveBrand(Brand brand) {
        // If brand has an id, try to load existing entity and update it to avoid detached/unsaved-value issues
        BrandEntity entity;
        if (brand.getId() != null) {
            // try to find by id and tenantId to ensure we don't accidentally create duplicates
            Optional<BrandEntity> existing = brandJpaRepository.findByIdAndTenantId(brand.getId(), brand.getTenantId());
            if (existing.isPresent()) {
                entity = existing.get();
                entity.setName(brand.getName());
                // ensure tenantId remains set (should already be set)
                entity.setTenantId(brand.getTenantId());
            } else {
                // Not found -> create new entity but preserve tenantId
                entity = convertBrandToDomain(brand);
            }
        } else {
            entity = convertBrandToDomain(brand);
        }

        BrandEntity savedEntity = brandJpaRepository.save(entity);
        return convertBrandToEntity(savedEntity);
    }

    @Override
    public Optional<Brand> findBrandById(UUID id) {
        return brandJpaRepository.findById(id).map(this::convertBrandToEntity);
    }

    @Override
    public List<Brand> findAllBrands(UUID tenantId) {
        return brandJpaRepository.findAllByTenantId(tenantId).stream()
                .map(this::convertBrandToEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Brand> findBrandByName(String name) {
        return brandJpaRepository.findByName(name).map(this::convertBrandToEntity);
    }

    @Override
    public void deleteBrand(UUID id) {
        brandJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsBrand(UUID id) {
        return brandJpaRepository.existsById(id);
    }

    @Override
    public long countBrands() {
        return brandJpaRepository.count();
    }

    // ==================== CONVERSION METHODS ====================

    private Brand convertBrandToEntity(BrandEntity entity) {
        Brand brand = new Brand();
        brand.setId(entity.getId());
        brand.setName(entity.getName());
        brand.setTenantId(entity.getTenantId());
        return brand;
    }

    private BrandEntity convertBrandToDomain(Brand brand) {
        BrandEntity entity = new BrandEntity();
        // If domain provided an id, set it on entity so JPA knows it's an update candidate
        if (brand.getId() != null) {
            entity.setId(brand.getId());
        }
        entity.setName(brand.getName());
        entity.setTenantId(brand.getTenantId());
        return entity;
    }
}
