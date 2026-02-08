package br.dev.garage474.mscatalog.applications.usecase;

import br.dev.garage474.mscatalog.adapters.in.web.dto.ProductVariantResponse;
import br.dev.garage474.mscatalog.domain.entities.ProductVariant;
import br.dev.garage474.mscatalog.domain.repositories.ProductRepository;
import br.dev.garage474.mscatalog.domain.services.ProductVariantService;
import br.dev.garage474.mscatalog.domain.vo.Dimensions;
import br.dev.garage474.mscatalog.domain.vo.Money;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Use Case para atualizar uma variante de produto.
 *
 * Responsabilidades:
 * - Receber comando de atualização
 * - Validar regras de negócio (via ProductVariantService)
 * - Atualizar a variante
 * - Retornar resposta
 *
 * Arquitetura: Clean Architecture - Application Layer
 */
@Service
public class UpdateProductVariantUseCase {

    private final ProductVariantService productVariantService;
    private final ProductRepository productRepository;

    public UpdateProductVariantUseCase(
            ProductVariantService productVariantService,
            ProductRepository productRepository) {
        this.productVariantService = productVariantService;
        this.productRepository = productRepository;
    }

    /**
     * Executa o caso de uso de atualização de variante de produto.
     *
     * @param command Comando com dados da variante a atualizar
     * @return Resposta com dados da variante atualizada
     */
    @Transactional
    public ProductVariantResponse execute(UpdateProductVariantCommand command) {
        // 1. Buscar variante existente
        ProductVariant variant = productRepository.findProductVariantById(command.variantId())
                .orElseThrow(() -> new IllegalArgumentException("Variante não encontrada: " + command.variantId()));

        // 2. Validar que o produto existe
        if (!productRepository.existsProduct(command.productId())) {
            throw new IllegalArgumentException("Produto não encontrado: " + command.productId());
        }

        // 3. Validar regras de negócio através do ProductVariantService
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

        // 4. Atualizar campos da variante
        variant.setSkuCode(command.skuCode());
        variant.setBarcode(command.barcode());

        // 5. Atualizar Value Object Money para preço
        if (command.price() != null) {
            variant.setPrice(new Money(command.price(), command.priceCurrency()));
        }

        // 6. Atualizar Value Object Money para preço promocional (se fornecido)
        if (command.promotionalPrice() != null) {
            variant.setPromotionalPrice(new Money(command.promotionalPrice(), command.promotionalPriceCurrency()));
        }

        // 7. Atualizar Value Object Dimensions (se fornecido)
        if (command.weight() != null || command.height() != null || command.width() != null || command.depth() != null) {
            variant.setDimensions(new Dimensions(
                    command.weight() != null ? command.weight() : 0.0,
                    command.height() != null ? command.height() : 0.0,
                    command.width() != null ? command.width() : 0.0,
                    command.depth() != null ? command.depth() : 0.0
            ));
        }

        // 8. Persistir
        ProductVariant updatedVariant = productRepository.saveProductVariant(variant);

        // 9. Converter e retornar como resposta
        return convertToResponse(updatedVariant);
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
     * Comando para atualizar variante de produto.
     *
     * Record imutável com os dados necessários para atualizar uma variante.
     */
    public record UpdateProductVariantCommand(
        UUID variantId,
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

