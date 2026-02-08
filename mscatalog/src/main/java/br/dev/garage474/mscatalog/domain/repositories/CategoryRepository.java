package br.dev.garage474.mscatalog.domain.repositories;

import br.dev.garage474.mscatalog.domain.entities.Category;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository para o Aggregate Root Category no bounded context mscatalog.
 *
 * Define os contratos para operações CRUD do agregado Category.
 * Esta interface segue o padrão DDD onde cada Aggregate Root possui seu próprio repositório.
 *
 * Responsabilidades:
 * - Persistência de Category (Aggregate Root)
 * - Gerenciamento de hierarquia de categorias (categoria pai e subcategorias)
 * - Consultas de categorias por identificadores e nome
 */
public interface CategoryRepository {

    /**
     * Salva uma nova categoria ou atualiza uma existente.
     */
    Category saveCategory(Category category);

    /**
     * Obtém uma categoria por ID.
     */
    Optional<Category> findCategoryById(UUID id);

    /**
     * Obtém todas as categorias.
     */
    List<Category> findAllCategories(UUID tenantId);

    /**
     * Obtém todas as categorias raíz (sem categoria pai).
     */
    List<Category> findAllRootCategories();

    /**
     * Obtém uma categoria por nome.
     */
    Optional<Category> findCategoryByName(String name);

    /**
     * Obtém todas as subcategorias de uma categoria pai.
     */
    List<Category> findSubcategoriesByCategoryId(UUID categoryId);

    /**
     * Deleta uma categoria por ID (subcategorias não são deletadas em cascata, apenas a própria).
     */
    void deleteCategory(UUID id);

    /**
     * Verifica se uma categoria existe por ID.
     */
    boolean existsCategory(UUID id);

    /**
     * Conta o total de categorias.
     */
    long countCategories();
}

