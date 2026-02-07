package br.dev.garage474.mscatalog.adapter.out.persistence.repository;

import br.dev.garage474.mscatalog.adapter.out.persistence.entity.*;
import br.dev.garage474.mscatalog.adapter.out.persistence.vo.Dimensions;
import br.dev.garage474.mscatalog.adapter.out.persistence.vo.Money;
import br.dev.garage474.mscatalog.adapter.out.persistence.vo.Tags;
import br.dev.garage474.mscatalog.domain.entity.*;
import br.dev.garage474.mscatalog.domain.repository.CatalogRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementação JPA da interface {@link CatalogRepository}.
 *
 * Esta classe é responsável por gerenciar a persistência de todas as entidades do bounded context mscatalog,
 * incluindo o agregado Product e suas entidades relacionadas (ProductVariant, Brand, Category, Attribute, AttributeValue).
 *
 * Utiliza múltiplos JpaRepositories para executar operações CRUD no banco de dados.
 */
@Repository
public class JpaCatalogRepository implements CatalogRepository {

    private final ProductJpaRepository productJpaRepository;
    private final ProductVariantJpaRepository productVariantJpaRepository;
    private final BrandJpaRepository brandJpaRepository;
    private final CategoryJpaRepository categoryJpaRepository;
    private final AttributeJpaRepository attributeJpaRepository;
    private final AttributeValueJpaRepository attributeValueJpaRepository;

    public JpaCatalogRepository(
            ProductJpaRepository productJpaRepository,
            ProductVariantJpaRepository productVariantJpaRepository,
            BrandJpaRepository brandJpaRepository,
            CategoryJpaRepository categoryJpaRepository,
            AttributeJpaRepository attributeJpaRepository,
            AttributeValueJpaRepository attributeValueJpaRepository) {
        this.productJpaRepository = productJpaRepository;
        this.productVariantJpaRepository = productVariantJpaRepository;
        this.brandJpaRepository = brandJpaRepository;
        this.categoryJpaRepository = categoryJpaRepository;
        this.attributeJpaRepository = attributeJpaRepository;
        this.attributeValueJpaRepository = attributeValueJpaRepository;
    }

    // ==================== PRODUCT ====================

    @Override
    public Product saveProduct(Product product) {
        ProductEntity entity = convertProductToDomain(product);
        ProductEntity savedEntity = productJpaRepository.save(entity);
        return convertProductToEntity(savedEntity);
    }

    @Override
    public Optional<Product> findProductById(UUID id) {
        return productJpaRepository.findById(id)
                .map(this::convertProductToEntity);
    }

    @Override
    public List<Product> findAllProducts() {
        return productJpaRepository.findAll().stream()
                .map(this::convertProductToEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Product> findProductBySlug(String slug) {
        return productJpaRepository.findBySlug(slug)
                .map(this::convertProductToEntity);
    }

    @Override
    public void deleteProduct(UUID id) {
        productJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsProduct(UUID id) {
        return productJpaRepository.existsById(id);
    }

    @Override
    public long countProducts() {
        return productJpaRepository.count();
    }

    // ==================== PRODUCT VARIANT ====================

    @Override
    public ProductVariant saveProductVariant(ProductVariant variant) {
        ProductVariantEntity entity = convertProductVariantToDomain(variant);
        ProductVariantEntity savedEntity = productVariantJpaRepository.save(entity);
        return convertProductVariantToEntity(savedEntity);
    }

    @Override
    public Optional<ProductVariant> findProductVariantById(UUID id) {
        return productVariantJpaRepository.findById(id)
                .map(this::convertProductVariantToEntity);
    }

    @Override
    public List<ProductVariant> findProductVariantsByProductId(UUID productId) {
        return productVariantJpaRepository.findByProductEntityId(productId).stream()
                .map(this::convertProductVariantToEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ProductVariant> findProductVariantBySkuCode(String skuCode) {
        return productVariantJpaRepository.findBySkuCode(skuCode)
                .map(this::convertProductVariantToEntity);
    }

    @Override
    public Optional<ProductVariant> findProductVariantByBarcode(String barcode) {
        return productVariantJpaRepository.findByBarcode(barcode)
                .map(this::convertProductVariantToEntity);
    }

    @Override
    public void deleteProductVariant(UUID id) {
        productVariantJpaRepository.deleteById(id);
    }

    @Override
    public void deleteProductVariantsByProductId(UUID productId) {
        productVariantJpaRepository.deleteByProductEntityId(productId);
    }

    @Override
    public boolean existsProductVariant(UUID id) {
        return productVariantJpaRepository.existsById(id);
    }

    @Override
    public long countProductVariantsByProductId(UUID productId) {
        return productVariantJpaRepository.countByProductEntityId(productId);
    }

    // ==================== BRAND ====================

    @Override
    public Brand saveBrand(Brand brand) {
        BrandEntity entity = convertBrandToDomain(brand);
        BrandEntity savedEntity = brandJpaRepository.save(entity);
        return convertBrandToEntity(savedEntity);
    }

    @Override
    public Optional<Brand> findBrandById(UUID id) {
        return brandJpaRepository.findById(id)
                .map(this::convertBrandToEntity);
    }

    @Override
    public List<Brand> findAllBrands() {
        return brandJpaRepository.findAll().stream()
                .map(this::convertBrandToEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Brand> findBrandByName(String name) {
        return brandJpaRepository.findByName(name)
                .map(this::convertBrandToEntity);
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

    // ==================== CATEGORY ====================

    @Override
    public Category saveCategory(Category category) {
        CategoryEntity entity = convertCategoryToDomain(category);
        CategoryEntity savedEntity = categoryJpaRepository.save(entity);
        return convertCategoryToEntity(savedEntity);
    }

    @Override
    public Optional<Category> findCategoryById(UUID id) {
        return categoryJpaRepository.findById(id)
                .map(this::convertCategoryToEntity);
    }

    @Override
    public List<Category> findAllCategories() {
        return categoryJpaRepository.findAll().stream()
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
        return categoryJpaRepository.findByName(name)
                .map(this::convertCategoryToEntity);
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

    // ==================== ATTRIBUTE ====================

    @Override
    public Attribute saveAttribute(Attribute attribute) {
        AttributeEntity entity = convertAttributeToDomain(attribute);
        AttributeEntity savedEntity = attributeJpaRepository.save(entity);
        return convertAttributeToEntity(savedEntity);
    }

    @Override
    public Optional<Attribute> findAttributeById(UUID id) {
        return attributeJpaRepository.findById(id)
                .map(this::convertAttributeToEntity);
    }

    @Override
    public List<Attribute> findAllAttributes() {
        return attributeJpaRepository.findAll().stream()
                .map(this::convertAttributeToEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Attribute> findAttributeByName(String name) {
        return attributeJpaRepository.findByName(name)
                .map(this::convertAttributeToEntity);
    }

    @Override
    public void deleteAttribute(UUID id) {
        attributeJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsAttribute(UUID id) {
        return attributeJpaRepository.existsById(id);
    }

    @Override
    public long countAttributes() {
        return attributeJpaRepository.count();
    }

    // ==================== ATTRIBUTE VALUE ====================

    @Override
    public AttributeValue saveAttributeValue(AttributeValue attributeValue) {
        AttributeValueEntity entity = convertAttributeValueToDomain(attributeValue);
        AttributeValueEntity savedEntity = attributeValueJpaRepository.save(entity);
        return convertAttributeValueToEntity(savedEntity);
    }

    @Override
    public Optional<AttributeValue> findAttributeValueById(UUID id) {
        return attributeValueJpaRepository.findById(id)
                .map(this::convertAttributeValueToEntity);
    }

    @Override
    public List<AttributeValue> findAttributeValuesByAttributeId(UUID attributeId) {
        return attributeValueJpaRepository.findByAttributeEntityId(attributeId).stream()
                .map(this::convertAttributeValueToEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAttributeValue(UUID id) {
        attributeValueJpaRepository.deleteById(id);
    }

    @Override
    public void deleteAttributeValuesByAttributeId(UUID attributeId) {
        attributeValueJpaRepository.deleteByAttributeEntityId(attributeId);
    }

    @Override
    public boolean existsAttributeValue(UUID id) {
        return attributeValueJpaRepository.existsById(id);
    }

    @Override
    public long countAttributeValuesByAttributeId(UUID attributeId) {
        return attributeValueJpaRepository.countByAttributeEntityId(attributeId);
    }

    // ==================== CONVERSION METHODS ====================

    private Product convertProductToEntity(ProductEntity entity) {
        Product product = new Product();
        product.setId(entity.getId());
        product.setName(entity.getName());
        product.setDescription(entity.getDescription());
        product.setSlug(entity.getSlug());

        // Converte Tags de persistence para domain/vo
        if (entity.getTags() != null) {
            product.setTags(new br.dev.garage474.mscatalog.domain.vo.Tags(entity.getTags().values()));
        }

        if (entity.getBrandEntity() != null) {
            product.setBrand(convertBrandToEntity(entity.getBrandEntity()));
        }
        if (entity.getCategoryEntity() != null) {
            product.setCategory(convertCategoryToEntity(entity.getCategoryEntity()));
        }
        if (entity.getVariants() != null) {
            product.setVariants(entity.getVariants().stream()
                    .map(this::convertProductVariantToEntity)
                    .collect(Collectors.toList()));
        }

        return product;
    }

    private ProductEntity convertProductToDomain(Product product) {
        ProductEntity entity = new ProductEntity();
        entity.setId(product.getId());
        entity.setName(product.getName());
        entity.setDescription(product.getDescription());
        entity.setSlug(product.getSlug());

        // Converte Tags de domain/vo para persistence
        if (product.getTags() != null) {
            entity.setTags(new Tags(product.getTags().values()));
        }

        if (product.getBrand() != null && product.getBrand().getId() != null) {
            entity.setBrandEntity(brandJpaRepository.findById(product.getBrand().getId()).orElse(null));
        }
        if (product.getCategory() != null && product.getCategory().getId() != null) {
            entity.setCategoryEntity(categoryJpaRepository.findById(product.getCategory().getId()).orElse(null));
        }

        return entity;
    }

    private ProductVariant convertProductVariantToEntity(ProductVariantEntity entity) {
        ProductVariant variant = new ProductVariant();
        variant.setId(entity.getId());
        variant.setSkuCode(entity.getSkuCode());
        variant.setBarcode(entity.getBarcode());

        // Converte Money de persistence para domain/vo
        if (entity.getPrice() != null) {
            variant.setPrice(new br.dev.garage474.mscatalog.domain.vo.Money(entity.getPrice().amount(), entity.getPrice().currency()));
        }
        if (entity.getPromotionalPrice() != null) {
            variant.setPromotionalPrice(new br.dev.garage474.mscatalog.domain.vo.Money(entity.getPromotionalPrice().amount(), entity.getPromotionalPrice().currency()));
        }

        // Converte Dimensions de persistence para domain/vo
        if (entity.getDimensions() != null) {
            variant.setDimensions(new br.dev.garage474.mscatalog.domain.vo.Dimensions(
                    entity.getDimensions().weight(),
                    entity.getDimensions().height(),
                    entity.getDimensions().width(),
                    entity.getDimensions().depth()
            ));
        }

        if (entity.getProductEntity() != null) {
            variant.setProduct(convertProductToEntity(entity.getProductEntity()));
        }

        return variant;
    }

    private ProductVariantEntity convertProductVariantToDomain(ProductVariant variant) {
        ProductVariantEntity entity = new ProductVariantEntity();
        entity.setId(variant.getId());
        entity.setSkuCode(variant.getSkuCode());
        entity.setBarcode(variant.getBarcode());

        // Converte Money de domain/vo para persistence
        if (variant.getPrice() != null) {
            entity.setPrice(new Money(
                    variant.getPrice().amount(),
                    variant.getPrice().currency()
            ));
        }
        if (variant.getPromotionalPrice() != null) {
            entity.setPromotionalPrice(new Money(
                    variant.getPromotionalPrice().amount(),
                    variant.getPromotionalPrice().currency()
            ));
        }

        // Converte Dimensions de domain/vo para persistence
        if (variant.getDimensions() != null) {
            entity.setDimensions(new Dimensions(
                    variant.getDimensions().weight(),
                    variant.getDimensions().height(),
                    variant.getDimensions().width(),
                    variant.getDimensions().depth()
            ));
        }

        if (variant.getProduct() != null && variant.getProduct().getId() != null) {
            entity.setProductEntity(productJpaRepository.findById(variant.getProduct().getId()).orElse(null));
        }

        return entity;
    }

    private Brand convertBrandToEntity(BrandEntity entity) {
        Brand brand = new Brand();
        brand.setId(entity.getId());
        brand.setName(entity.getName());
        return brand;
    }

    private BrandEntity convertBrandToDomain(Brand brand) {
        BrandEntity entity = new BrandEntity();
        entity.setId(brand.getId());
        entity.setName(brand.getName());
        return entity;
    }

    private Category convertCategoryToEntity(CategoryEntity entity) {
        Category category = new Category();
        category.setId(entity.getId());
        category.setName(entity.getName());

        if (entity.getParent() != null) {
            category.setParent(convertCategoryToEntity(entity.getParent()));
        }
        if (entity.getSubCategories() != null) {
            category.setSubCategories(entity.getSubCategories().stream()
                    .map(this::convertCategoryToEntity)
                    .collect(Collectors.toList()));
        }

        return category;
    }

    private CategoryEntity convertCategoryToDomain(Category category) {
        CategoryEntity entity = new CategoryEntity();
        entity.setId(category.getId());
        entity.setName(category.getName());

        if (category.getParent() != null && category.getParent().getId() != null) {
            entity.setParent(categoryJpaRepository.findById(category.getParent().getId()).orElse(null));
        }

        return entity;
    }

    private Attribute convertAttributeToEntity(AttributeEntity entity) {
        Attribute attribute = new Attribute();
        attribute.setId(entity.getId());
        attribute.setName(entity.getName());
        return attribute;
    }

    private AttributeEntity convertAttributeToDomain(Attribute attribute) {
        AttributeEntity entity = new AttributeEntity();
        entity.setId(attribute.getId());
        entity.setName(attribute.getName());
        return entity;
    }

    private AttributeValue convertAttributeValueToEntity(AttributeValueEntity entity) {
        AttributeValue attributeValue = new AttributeValue();
        attributeValue.setId(entity.getId());
        attributeValue.setValue(entity.getValue());

        if (entity.getAttributeEntity() != null) {
            attributeValue.setAttribute(convertAttributeToEntity(entity.getAttributeEntity()));
        }

        return attributeValue;
    }

    private AttributeValueEntity convertAttributeValueToDomain(AttributeValue attributeValue) {
        AttributeValueEntity entity = new AttributeValueEntity();
        entity.setId(attributeValue.getId());
        entity.setValue(attributeValue.getValue());

        if (attributeValue.getAttribute() != null && attributeValue.getAttribute().getId() != null) {
            entity.setAttributeEntity(attributeJpaRepository.findById(attributeValue.getAttribute().getId()).orElse(null));
        }

        return entity;
    }
}


