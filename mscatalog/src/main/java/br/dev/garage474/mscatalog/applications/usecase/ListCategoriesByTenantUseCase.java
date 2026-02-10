package br.dev.garage474.mscatalog.applications.usecase;

import br.dev.garage474.mscatalog.adapters.in.dto.CategoryResponse;
import br.dev.garage474.mscatalog.domain.entities.Category;
import br.dev.garage474.mscatalog.domain.repositories.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Use Case para listar categorias por tenant.
 *
 * Responsabilidades:
 * - Receber query de listagem
 * - Buscar categorias do tenant
 * - Converter e retornar respostas
 *
 * Arquitetura: Clean Architecture - Application Layer
 */
@Service
public class ListCategoriesByTenantUseCase {

    private final CategoryRepository categoryRepository;

    public ListCategoriesByTenantUseCase(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * Executa o caso de uso de listagem de categorias.
     *
     * @param query Query com dados de filtro
     * @return Lista de categorias do tenant
     */
    @Transactional(readOnly = true)
    public List<CategoryResponse> execute(ListCategoriesQuery query) {
        // 1. Buscar categorias do tenant (por enquanto todos)
        List<Category> categories = categoryRepository.findAllCategories(query.tenantId);

        // 2. Converter e retornar como resposta
        return categories.stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
    }

    /**
     * Converte Category (Domain Entity) para CategoryResponse (DTO).
     */
    private CategoryResponse convertToResponse(Category category) {
        return new CategoryResponse(
            category.getId(),
            category.getName(),
            category.getParent() != null ? convertParentOrSubCategoryToResponse(category.getParent()) : null,
            category.getSubCategories() != null ?
                category.getSubCategories().stream()
                    .map(this::convertParentOrSubCategoryToResponse)
                    .collect(Collectors.toList()) : null
        );
    }

    private CategoryResponse convertParentOrSubCategoryToResponse(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
            null, // Não precisamos do pai do pai
            null  // Não precisamos das subcategorias do pai
        );
    }

    /**
     * Query para listar categorias.
     *
     * Record imutável com os dados necessários para listar categorias.
     */
    public record ListCategoriesQuery(
        UUID tenantId
    ) {}
}

