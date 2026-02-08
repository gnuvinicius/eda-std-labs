package br.dev.garage474.mscatalog.adapters.out.persistence.repository;

import br.dev.garage474.mscatalog.adapters.out.persistence.entity.CategoryEntity;
import br.dev.garage474.mscatalog.domain.entities.Category;
import br.dev.garage474.mscatalog.domain.repositories.CategoryRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;

/**
 * Implementação JPA do repositório {@link CategoryRepository}.
 *
 * Responsável pela persistência do agregado Category com suporte a hierarquia.
 */
@Repository
public class JpaCategoryRepository implements CategoryRepository {

    private final CategoryJpaRepository categoryJpaRepository;

    public JpaCategoryRepository(CategoryJpaRepository categoryJpaRepository) {
        this.categoryJpaRepository = categoryJpaRepository;
    }

    @Override
    public Category saveCategory(Category category) {
        CategoryEntity entity = convertCategoryToDomain(category);
        CategoryEntity savedEntity = categoryJpaRepository.save(entity);
        return convertCategoryToEntity(savedEntity);
    }

    @Override
    public Optional<Category> findCategoryById(UUID id) {
        return categoryJpaRepository.findById(id).map(this::convertCategoryToEntity);
    }

    @Override
    public List<Category> findAllCategories(UUID tenantId) {
        return categoryJpaRepository.findAllByTenantId(tenantId).stream()
                .map(this::convertCategoryToEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<Category> findAllRootCategories() {
        return categoryJpaRepository.findByParentIsNull().stream()
                .map(this::convertCategoryToEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Category> findCategoryByName(String name) {
        return categoryJpaRepository.findByName(name).map(this::convertCategoryToEntity);
    }

    @Override
    public List<Category> findSubcategoriesByCategoryId(UUID categoryId) {
        return categoryJpaRepository.findByParentId(categoryId).stream()
                .map(this::convertCategoryToEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteCategory(UUID id) {
        categoryJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsCategory(UUID id) {
        return categoryJpaRepository.existsById(id);
    }

    @Override
    public long countCategories() {
        return categoryJpaRepository.count();
    }

    // ==================== CONVERSION METHODS ====================

    // Use a cache to avoid infinite recursion when entities reference each other (parent/subcategories).
    private Category convertCategoryToEntity(CategoryEntity entity) {
        return convertCategoryToEntity(entity, new HashMap<>());
    }

    private Category convertCategoryToEntity(CategoryEntity entity, Map<UUID, Category> cache) {
        if (entity == null) return null;

        UUID id = entity.getId();
        if (id != null && cache.containsKey(id)) {
            return cache.get(id);
        }

        Category category = new Category();
        category.setId(entity.getId());
        category.setName(entity.getName());

        // Put in cache BEFORE converting relations to break cycles
        if (id != null) {
            cache.put(id, category);
        }

        // Convert parent (may return an already-cached instance)
        if (entity.getParent() != null) {
            category.setParent(convertCategoryToEntity(entity.getParent(), cache));
        }

        // Convert subcategories (each will consult/insert into the same cache)
        if (entity.getSubCategories() != null) {
            category.setSubCategories(entity.getSubCategories().stream()
                    .map(e -> convertCategoryToEntity(e, cache))
                    .collect(Collectors.toList()));
        }

        return category;
    }

    private CategoryEntity convertCategoryToDomain(Category category) {
        CategoryEntity entity = new CategoryEntity();
        entity.setName(category.getName());
        entity.setTenantId(category.getTenantId());

        if (category.getParent() != null && category.getParent().getId() != null) {
            entity.setParent(categoryJpaRepository.findById(category.getParent().getId()).orElse(null));
        }

        return entity;
    }
}
