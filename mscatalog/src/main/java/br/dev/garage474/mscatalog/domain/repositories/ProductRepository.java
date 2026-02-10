package br.dev.garage474.mscatalog.domain.repositories;

import br.dev.garage474.mscatalog.domain.entities.Product;
import br.dev.garage474.mscatalog.domain.entities.ProductVariant;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository para o Aggregate Root Product no bounded context mscatalog.
 * <p>
 * Define os contratos para operações CRUD do agregado Product e suas entidades filhas (ProductVariant).
 * Esta interface segue o padrão DDD onde cada Aggregate Root possui seu próprio repositório.
 * <p>
 * Responsabilidades:
 * - Persistência do Product (Aggregate Root)
 * - Persistência de ProductVariant (Child Entity)
 * - Garantir a consistência do agregado
 */
public interface ProductRepository {

    // ==================== PRODUCT ====================

    /**
     * Salva um novo produto ou atualiza um existente.
     */
    Product saveProduct(Product product);

    /**
     * Obtém um produto por ID.
     */
    Optional<Product> findProductById(UUID id);

    /**
     * Obtém todos os produtos.
     */
    List<Product> findAllProducts();

    /**
     * Obtém um produto por seu slug único.
     */
    Optional<Product> findProductBySlug(String slug);

    /**
     * Deleta um produto por ID (e suas variantes em cascata).
     */
    void deleteProduct(UUID id);

    /**
     * Verifica se um produto existe por ID.
     */
    boolean existsProduct(UUID id);

    /**
     * Conta o total de produtos.
     */
    long countProducts();

    // ==================== PRODUCT VARIANT ====================

    /**
     * Salva uma nova variante de produto ou atualiza uma existente.
     */
    ProductVariant saveProductVariant(ProductVariant variant);

    /**
     * Obtém uma variante de produto por ID.
     */
    Optional<ProductVariant> findProductVariantById(UUID id);

    /**
     * Obtém todas as variantes de um produto específico.
     */
    List<ProductVariant> findProductVariantsByProductId(UUID productId);

    /**
     * Obtém uma variante por seu código SKU.
     */
    Optional<ProductVariant> findProductVariantBySkuCode(String skuCode);

    /**
     * Obtém uma variante por seu código de barras.
     */
    Optional<ProductVariant> findProductVariantByBarcode(String barcode);

    /**
     * Deleta uma variante de produto por ID.
     */
    void deleteProductVariant(UUID id);

    /**
     * Deleta todas as variantes de um produto específico (útil ao deletar produto).
     */
    void deleteProductVariantsByProductId(UUID productId);

    /**
     * Verifica se uma variante existe por ID.
     */
    boolean existsProductVariant(UUID id);

    /**
     * Conta o total de variantes de um produto.
     */
    long countProductVariantsByProductId(UUID productId);

    // ==================== SHOWCASE / FILTRO ====================

    /**
     * Busca produtos com paginação por tenant, com suporte a filtros de busca, brand e categoria.
     *
     * @param tenantId   ID do tenant
     * @param searchTerm Termo de busca (busca em name e description)
     * @param brandId    ID da brand para filtro (opcional, null para ignorar)
     * @param categoryId ID da categoria para filtro (opcional, null para ignorar)
     * @param page       Número da página (0-based)
     * @param size       Tamanho da página
     * @return Página com produtos que atendem aos critérios
     */
    ShowcasePageable findProductsByTenantWithFilters(
            UUID tenantId,
            String searchTerm,
            UUID brandId,
            UUID categoryId,
            int page,
            int size
    );

    // ==================== DTO for Showcase ====================

    /**
     * DTO interno para encapsular resultado paginado do Showcase.
     * Utilizado entre repositório e use case.
     */
    record ShowcasePageable(
            List<Product> content,
            int page,
            int size,
            int totalPages,
            long totalElements
    ) {
    }
}

