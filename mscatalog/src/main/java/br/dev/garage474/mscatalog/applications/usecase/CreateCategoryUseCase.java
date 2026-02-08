package br.dev.garage474.mscatalog.applications.usecase;

import br.dev.garage474.mscatalog.adapters.in.web.dto.CategoryResponse;
import br.dev.garage474.mscatalog.domain.entities.Category;
import br.dev.garage474.mscatalog.domain.repositories.CategoryRepository;
import br.dev.garage474.mscatalog.domain.services.CategoryService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Use Case para criar uma nova categoria.
 * <p>
 * Responsabilidades:
 * - Receber comando de criação
 * - Validar regras de negócio (via CategoryService)
 * - Persistir a categoria
 * - Retornar resposta
 * <p>
 * Arquitetura: Clean Architecture - Application Layer
 */
@Log4j2
@Service
public class CreateCategoryUseCase {

    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;

    public CreateCategoryUseCase(
            CategoryService categoryService,
            CategoryRepository categoryRepository) {
        this.categoryService = categoryService;
        this.categoryRepository = categoryRepository;
    }

    /**
     * Executa o caso de uso de criação de categoria.
     *
     * @param command Comando com dados da categoria
     * @return Resposta com dados da categoria criada
     */
    @Transactional
    public CategoryResponse execute(CreateCategoryCommand command) {
        try {
            // 1. Validar regras de negócio através do CategoryService
            categoryService.validateCategoryCreation(command.name());

            // 2. Buscar categoria pai (se fornecida)
            Category parentCategory = null;
            if (command.parentCategoryId() != null) {
                parentCategory = categoryRepository.findCategoryById(command.parentCategoryId())
                        .orElseThrow(() -> new IllegalArgumentException("Categoria pai não encontrada: " + command.parentCategoryId()));
            }

            // 3. Criar entidade Category
            Category category = new Category();
            category.setName(command.name());
            category.setParent(parentCategory);
            category.setTenantId(command.tenantId());

            // 4. Persistir
            Category savedCategory = categoryRepository.saveCategory(category);

            // 5. Converter e retornar como resposta
            return convertToResponse(savedCategory);
        } catch (Exception e) {
            log.error("erro ao cadastrar nova categoria: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Converte Category (Domain Entity) para CategoryResponse (DTO).
     */
    private CategoryResponse convertToResponse(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getParent() != null ? convertToResponse(category.getParent()) : null,
                category.getSubCategories() != null ?
                        category.getSubCategories().stream()
                                .map(this::convertToResponse)
                                .collect(Collectors.toList()) : null
        );
    }

    /**
     * Comando para criar categoria.
     * <p>
     * Record imutável com os dados necessários para criar uma categoria.
     */
    public record CreateCategoryCommand(
            UUID tenantId,
            String name,
            UUID parentCategoryId
    ) {
    }
}

