package br.dev.garage474.mscatalog.domain.repository;

import br.dev.garage474.mscatalog.domain.entity.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Interface de repositório para o Aggregate Root Product no bounded context mscatalog.
 *
 * Define os contratos para operações CRUD do agregado Product e suas entidades relacionadas,
 * incluindo:
 * - Product (Aggregate Root)
 * - ProductVariant (Child Entity)
 * - Brand (Entity referenciada)
 * - Category (Entity referenciada)
 * - Attribute e AttributeValue (Entities para especificações de variantes)
 */
public interface CatalogRepository {

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
     * Deleta um produto por ID.
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
     * Deleta todas as variantes de um produto específico.
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

    // ==================== BRAND ====================

    /**
     * Salva uma nova marca ou atualiza uma existente.
     */
    Brand saveBrand(Brand brand);

    /**
     * Obtém uma marca por ID.
     */
    Optional<Brand> findBrandById(UUID id);

    /**
     * Obtém todas as marcas.
     */
    List<Brand> findAllBrands();

    /**
     * Obtém uma marca por nome.
     */
    Optional<Brand> findBrandByName(String name);

    /**
     * Deleta uma marca por ID.
     */
    void deleteBrand(UUID id);

    /**
     * Verifica se uma marca existe por ID.
     */
    boolean existsBrand(UUID id);

    /**
     * Conta o total de marcas.
     */
    long countBrands();

    // ==================== CATEGORY ====================

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
    List<Category> findAllCategories();

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
     * Deleta uma categoria por ID.
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

    // ==================== ATTRIBUTE ====================

    /**
     * Salva um novo atributo ou atualiza um existente.
     */
    Attribute saveAttribute(Attribute attribute);

    /**
     * Obtém um atributo por ID.
     */
    Optional<Attribute> findAttributeById(UUID id);

    /**
     * Obtém todos os atributos.
     */
    List<Attribute> findAllAttributes();

    /**
     * Obtém um atributo por nome.
     */
    Optional<Attribute> findAttributeByName(String name);

    /**
     * Deleta um atributo por ID.
     */
    void deleteAttribute(UUID id);

    /**
     * Verifica se um atributo existe por ID.
     */
    boolean existsAttribute(UUID id);

    /**
     * Conta o total de atributos.
     */
    long countAttributes();

    // ==================== ATTRIBUTE VALUE ====================

    /**
     * Salva um novo valor de atributo ou atualiza um existente.
     */
    AttributeValue saveAttributeValue(AttributeValue attributeValue);

    /**
     * Obtém um valor de atributo por ID.
     */
    Optional<AttributeValue> findAttributeValueById(UUID id);

    /**
     * Obtém todos os valores de um atributo específico.
     */
    List<AttributeValue> findAttributeValuesByAttributeId(UUID attributeId);

    /**
     * Deleta um valor de atributo por ID.
     */
    void deleteAttributeValue(UUID id);

    /**
     * Deleta todos os valores de um atributo específico.
     */
    void deleteAttributeValuesByAttributeId(UUID attributeId);

    /**
     * Verifica se um valor de atributo existe por ID.
     */
    boolean existsAttributeValue(UUID id);

    /**
     * Conta o total de valores de um atributo.
     */
    long countAttributeValuesByAttributeId(UUID attributeId);
}
