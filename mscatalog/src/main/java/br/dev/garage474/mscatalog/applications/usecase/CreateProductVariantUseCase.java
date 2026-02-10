package br.dev.garage474.mscatalog.applications.usecase;

import br.dev.garage474.mscatalog.adapters.in.dto.ProductVariantResponse;
import br.dev.garage474.mscatalog.domain.entities.Product;
import br.dev.garage474.mscatalog.domain.entities.ProductVariant;
import br.dev.garage474.mscatalog.domain.repositories.ProductRepository;
import br.dev.garage474.mscatalog.domain.services.ProductVariantService;
import br.dev.garage474.mscatalog.domain.vo.Dimensions;
import br.dev.garage474.mscatalog.domain.vo.Money;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Use Case para criar uma nova variante de produto.
 *
 * Responsabilidades:
 * - Receber comando de criação
 * - Validar regras de negócio (via ProductVariantService)
 * - Persistir a variante
 * - Retornar resposta
 *
 * Arquitetura: Clean Architecture - Application Layer
 */
@Service
public class CreateProductVariantUseCase {

    private final ProductVariantService productVariantService;
    private final ProductRepository productRepository;

    public CreateProductVariantUseCase(
            ProductVariantService productVariantService,
            ProductRepository productRepository) {
        this.productVariantService = productVariantService;
        this.productRepository = productRepository;
    }

    /**
     * Executa o caso de uso de criação de variante de produto.
     *
     * @param command Comando com dados da variante
     * @return Resposta com dados da variante criada
     */
    @Transactional
    public ProductVariantResponse execute(CreateProductVariantCommand command) {
        // 1. Validar que o produto existe
        Product product = productRepository.findProductById(command.productId())
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado: " + command.productId()));

        // 2. Validar regras de negócio através do ProductVariantService
        productVariantService.validateProductVariantCreation(
                command.skuCode(),
                command.barcode(),
                command.price(),
                command.promotionalPrice(),
                command.weight(),
                command.height(),
                command.width(),
                command.depth()
        );

        // 3. Criar entidade ProductVariant
        ProductVariant variant = new ProductVariant();
        variant.setId(UUID.randomUUID());
        variant.setSkuCode(command.skuCode());
        variant.setBarcode(command.barcode());
        variant.setProduct(product);

        // 4. Criar Value Object Money para preço
        if (command.price() != null) {
            variant.setPrice(new Money(command.price(), command.priceCurrency()));
        }

        // 5. Criar Value Object Money para preço promocional (se fornecido)
        if (command.promotionalPrice() != null) {
            variant.setPromotionalPrice(new Money(command.promotionalPrice(), command.promotionalPriceCurrency()));
        }

        // 6. Criar Value Object Dimensions (se fornecido)
        if (command.weight() != null || command.height() != null || command.width() != null || command.depth() != null) {
            variant.setDimensions(new Dimensions(
                    command.weight() != null ? command.weight() : 0.0,
                    command.height() != null ? command.height() : 0.0,
                    command.width() != null ? command.width() : 0.0,
                    command.depth() != null ? command.depth() : 0.0
            ));
        }

        // 7. Persistir
        ProductVariant savedVariant = productRepository.saveProductVariant(variant);

        // 8. Converter e retornar como resposta
        return convertToResponse(savedVariant);
    }

    /**
     * Converte ProductVariant (Domain Entity) para ProductVariantResponse (DTO).
     */
    private ProductVariantResponse convertToResponse(ProductVariant variant) {
        ProductVariantResponse.MoneyResponse priceResponse = null;
        if (variant.getPrice() != null) {
            priceResponse = new ProductVariantResponse.MoneyResponse(
                    variant.getPrice().amount(),
                    variant.getPrice().currency()
            );
        }

        ProductVariantResponse.MoneyResponse promotionalPriceResponse = null;
        if (variant.getPromotionalPrice() != null) {
            promotionalPriceResponse = new ProductVariantResponse.MoneyResponse(
                    variant.getPromotionalPrice().amount(),
                    variant.getPromotionalPrice().currency()
            );
        }

        ProductVariantResponse.DimensionsResponse dimensionsResponse = null;
        if (variant.getDimensions() != null) {
            dimensionsResponse = new ProductVariantResponse.DimensionsResponse(
                    variant.getDimensions().weight(),
                    variant.getDimensions().height(),
                    variant.getDimensions().width(),
                    variant.getDimensions().depth()
            );
        }

        return new ProductVariantResponse(
                variant.getId(),
                variant.getProduct() != null ? variant.getProduct().getId() : null,
                variant.getSkuCode(),
                variant.getBarcode(),
                priceResponse,
                promotionalPriceResponse,
                dimensionsResponse
        );
    }

    /**
     * Comando para criar variante de produto.
     *
     * Record imutável com os dados necessários para criar uma variante.
     */
    public record CreateProductVariantCommand(
        UUID productId,
        String skuCode,
        String barcode,
        java.math.BigDecimal price,
        String priceCurrency,
        java.math.BigDecimal promotionalPrice,
        String promotionalPriceCurrency,
        Double weight,
        Double height,
        Double width,
        Double depth
    ) {}
}

