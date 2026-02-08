package br.dev.garage474.mscatalog.applications.usecase;

import br.dev.garage474.mscatalog.adapters.in.web.dto.ProductResponse;
import br.dev.garage474.mscatalog.domain.entities.Brand;
import br.dev.garage474.mscatalog.domain.entities.Category;
import br.dev.garage474.mscatalog.domain.entities.Product;
import br.dev.garage474.mscatalog.domain.repositories.BrandRepository;
import br.dev.garage474.mscatalog.domain.repositories.CategoryRepository;
import br.dev.garage474.mscatalog.domain.repositories.ProductRepository;
import br.dev.garage474.mscatalog.domain.services.ProductService;
import br.dev.garage474.mscatalog.domain.vo.Tags;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Use Case para criar um novo produto.
 * <p>
 * Responsabilidades:
 * - Receber comando de criação
 * - Validar regras de negócio (via ProductService)
 * - Persistir o produto
 * - Retornar resposta
 * <p>
 * Arquitetura: Clean Architecture - Application Layer
 */
@Log4j2
@Service
public class CreateProductUseCase {

    private final ProductService productService;
    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;

    public CreateProductUseCase(
            ProductService productService,
            ProductRepository productRepository,
            BrandRepository brandRepository,
            CategoryRepository categoryRepository) {
        this.productService = productService;
        this.productRepository = productRepository;
        this.brandRepository = brandRepository;
        this.categoryRepository = categoryRepository;
    }

    /**
     * Executa o caso de uso de criação de produto.
     *
     * @param command Comando com dados do produto
     * @return Resposta com dados do produto criado
     */
    @Transactional
    public ProductResponse execute(CreateProductCommand command) {
        try {
            // 1. Validar regras de negócio através do ProductService
            productService.validateProductCreation(command.name(), command.slug());

            // 2. Buscar Brand (se fornecida)
            Brand brand = null;
            if (command.brandId() != null) {
                brand = brandRepository.findBrandById(command.brandId())
                        .orElseThrow(() -> new IllegalArgumentException("Brand não encontrada: " + command.brandId()));
            }

            // 3. Buscar Category (se fornecida)
            Category category = null;
            if (command.categoryId() != null) {
                category = categoryRepository.findCategoryById(command.categoryId())
                        .orElseThrow(() -> new IllegalArgumentException("Category não encontrada: " + command.categoryId()));
            }

            // 4. Criar entidade Product
            Product product = new Product();
            product.setId(UUID.randomUUID());
            product.setName(command.name());
            product.setDescription(command.description());
            product.setSlug(command.slug());
            product.setBrand(brand);
            product.setCategory(category);
            product.setTags(command.tags() != null ? new Tags(command.tags()) : null);

            // 5. Persistir
            Product savedProduct = productRepository.saveProduct(product);

            // 6. Converter e retornar como resposta
            return convertToResponse(savedProduct);
        } catch (Exception e) {
            log.error("erro ao cadastrar novo produto: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Converte Product (Domain Entity) para ProductResponse (DTO).
     */
    private ProductResponse convertToResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getSlug(),
                product.getBrand() != null ?
                        new ProductResponse.BrandResponse(product.getBrand().getId(), product.getBrand().getName()) : null,
                product.getCategory() != null ?
                        new ProductResponse.CategoryResponse(product.getCategory().getId(), product.getCategory().getName()) : null,
                product.getTags() != null ? product.getTags().values() : null
        );
    }

    /**
     * Comando para criar produto.
     * <p>
     * Record imutável com os dados necessários para criar um produto.
     */
    public record CreateProductCommand(
            UUID tenantId,
            String name,
            String description,
            String slug,
            UUID brandId,
            UUID categoryId,
            List<String> tags
    ) {
    }
}

