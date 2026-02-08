package br.dev.garage474.mscatalog.adapters.out.persistence.repository;

import br.dev.garage474.mscatalog.adapters.out.persistence.entity.ProductEntity;
import br.dev.garage474.mscatalog.adapters.out.persistence.entity.ProductVariantEntity;
import br.dev.garage474.mscatalog.adapters.out.persistence.vo.Dimensions;
import br.dev.garage474.mscatalog.adapters.out.persistence.vo.Money;
import br.dev.garage474.mscatalog.adapters.out.persistence.vo.Tags;
import br.dev.garage474.mscatalog.domain.entities.Product;
import br.dev.garage474.mscatalog.domain.entities.ProductVariant;
import br.dev.garage474.mscatalog.domain.repositories.ProductRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementação JPA do repositório {@link ProductRepository}.
 *
 * Responsável pela persistência do agregado Product e suas entidades filhas (ProductVariant).
 */
@Repository
public class JpaProductRepository implements ProductRepository {

    private final ProductJpaRepository productJpaRepository;
    private final ProductVariantJpaRepository productVariantJpaRepository;
    private final BrandJpaRepository brandJpaRepository;
    private final CategoryJpaRepository categoryJpaRepository;

    public JpaProductRepository(
            ProductJpaRepository productJpaRepository,
            ProductVariantJpaRepository productVariantJpaRepository,
            BrandJpaRepository brandJpaRepository,
            CategoryJpaRepository categoryJpaRepository) {
        this.productJpaRepository = productJpaRepository;
        this.productVariantJpaRepository = productVariantJpaRepository;
        this.brandJpaRepository = brandJpaRepository;
        this.categoryJpaRepository = categoryJpaRepository;
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
        return productJpaRepository.findById(id).map(this::convertProductToEntity);
    }

    @Override
    public List<Product> findAllProducts() {
        return productJpaRepository.findAll().stream()
                .map(this::convertProductToEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Product> findProductBySlug(String slug) {
        return productJpaRepository.findBySlug(slug).map(this::convertProductToEntity);
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
        return productVariantJpaRepository.findById(id).map(this::convertProductVariantToEntity);
    }

    @Override
    public List<ProductVariant> findProductVariantsByProductId(UUID productId) {
        return productVariantJpaRepository.findByProductEntityId(productId).stream()
                .map(this::convertProductVariantToEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ProductVariant> findProductVariantBySkuCode(String skuCode) {
        return productVariantJpaRepository.findBySkuCode(skuCode).map(this::convertProductVariantToEntity);
    }

    @Override
    public Optional<ProductVariant> findProductVariantByBarcode(String barcode) {
        return productVariantJpaRepository.findByBarcode(barcode).map(this::convertProductVariantToEntity);
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

    // ==================== CONVERSION METHODS ====================

    private Product convertProductToEntity(ProductEntity entity) {
        Product product = new Product();
        product.setId(entity.getId());
        product.setName(entity.getName());
        product.setDescription(entity.getDescription());
        product.setSlug(entity.getSlug());

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

        if (entity.getPrice() != null) {
            variant.setPrice(new br.dev.garage474.mscatalog.domain.vo.Money(entity.getPrice().amount(), entity.getPrice().currency()));
        }
        if (entity.getPromotionalPrice() != null) {
            variant.setPromotionalPrice(new br.dev.garage474.mscatalog.domain.vo.Money(entity.getPromotionalPrice().amount(), entity.getPromotionalPrice().currency()));
        }

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

    private br.dev.garage474.mscatalog.domain.entities.Brand convertBrandToEntity(
            br.dev.garage474.mscatalog.adapters.out.persistence.entity.BrandEntity entity) {
        br.dev.garage474.mscatalog.domain.entities.Brand brand = new br.dev.garage474.mscatalog.domain.entities.Brand();
        brand.setId(entity.getId());
        brand.setName(entity.getName());
        return brand;
    }

    private br.dev.garage474.mscatalog.domain.entities.Category convertCategoryToEntity(
            br.dev.garage474.mscatalog.adapters.out.persistence.entity.CategoryEntity entity) {
        br.dev.garage474.mscatalog.domain.entities.Category category = new br.dev.garage474.mscatalog.domain.entities.Category();
        category.setId(entity.getId());
        category.setName(entity.getName());
        return category;
    }
}

