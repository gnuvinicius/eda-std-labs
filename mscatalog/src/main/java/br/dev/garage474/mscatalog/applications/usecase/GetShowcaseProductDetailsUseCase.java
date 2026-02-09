package br.dev.garage474.mscatalog.applications.usecase;

import br.dev.garage474.mscatalog.adapters.in.web.dto.ShowcaseProductResponse;
import br.dev.garage474.mscatalog.domain.entities.Product;
import br.dev.garage474.mscatalog.domain.repositories.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Use Case para obter detalhes de um produto no Showcase.
 *
 * Responsabilidades:
 * - Receber ID do produto
 * - Buscar produto do repositório
 * - Converter e retornar resposta formatada para o frontend
 *
 * Arquitetura: Clean Architecture - Application Layer
 */
@Service
@Slf4j
public class GetShowcaseProductDetailsUseCase {

    private final ProductRepository productRepository;

    public GetShowcaseProductDetailsUseCase(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Executa o caso de uso de obtenção de detalhes do produto.
     *
     * @param query Query com ID do produto
     * @return Detalhes completos do produto para exibir no Showcase
     */
    @Transactional(readOnly = true)
    public ShowcaseProductResponse execute(GetShowcaseProductDetailsQuery query) {
        try {
            // 1. Buscar produto por ID
            var product = productRepository.findProductById(query.productId())
                .orElseThrow(() -> {
                    log.warn("produto não encontrado: id={}", query.productId());
                    return new IllegalArgumentException("Produto não encontrado");
                });

            // 2. Converter e retornar como resposta
            return convertToShowcaseResponse(product);
        } catch (Exception e) {
            log.error("erro ao buscar detalhes do produto: id={}", query.productId(), e);
            throw e;
        }
    }

    /**
     * Converte Product (Domain Entity) para ShowcaseProductResponse (DTO).
     */
    private ShowcaseProductResponse convertToShowcaseResponse(Product product) {
        // Converter variantes
        var variantsResponses = product.getVariants().stream()
            .map(this::convertVariantToShowcase)
            .collect(Collectors.toList());

        // Encontrar menor preço
        var lowestVariantPrice = product.getVariants().stream()
            .filter(v -> v.getPrice() != null)
            .min((v1, v2) -> v1.getPrice().amount().compareTo(v2.getPrice().amount()))
            .map(v -> new ShowcaseProductResponse.ProductPriceResponse.MoneyResponse(
                v.getPrice().amount(),
                v.getPrice().currency()
            ))
            .orElse(null);

        // Encontrar menor preço promocional
        var lowestPromotionalPrice = product.getVariants().stream()
            .filter(v -> v.getPromotionalPrice() != null)
            .min((v1, v2) -> v1.getPromotionalPrice().amount().compareTo(v2.getPromotionalPrice().amount()))
            .map(v -> new ShowcaseProductResponse.ProductPriceResponse.MoneyResponse(
                v.getPromotionalPrice().amount(),
                v.getPromotionalPrice().currency()
            ))
            .orElse(null);

        var priceInfo = new ShowcaseProductResponse.ProductPriceResponse(
            null,
            lowestVariantPrice,
            lowestPromotionalPrice
        );

        return new ShowcaseProductResponse(
            product.getId(),
            product.getName(),
            product.getDescription(),
            product.getSlug(),
            product.getBrand() != null ?
                new ShowcaseProductResponse.BrandResponse(product.getBrand().getId(), product.getBrand().getName()) : null,
            product.getCategory() != null ?
                new ShowcaseProductResponse.CategoryResponse(product.getCategory().getId(), product.getCategory().getName()) : null,
            product.getTags() != null ? product.getTags().values() : null,
            priceInfo,
            variantsResponses
        );
    }

    /**
     * Converte ProductVariant para ShowcaseProductVariantResponse.
     */
    private ShowcaseProductResponse.ProductVariantResponse convertVariantToShowcase(
            br.dev.garage474.mscatalog.domain.entities.ProductVariant variant) {
        return new ShowcaseProductResponse.ProductVariantResponse(
            variant.getId(),
            variant.getProduct() != null ? variant.getProduct().getId() : null,
            variant.getSkuCode(),
            variant.getBarcode(),
            variant.getPrice() != null ?
                new ShowcaseProductResponse.ProductVariantResponse.MoneyResponse(
                    variant.getPrice().amount(),
                    variant.getPrice().currency()
                ) : null,
            variant.getPromotionalPrice() != null ?
                new ShowcaseProductResponse.ProductVariantResponse.MoneyResponse(
                    variant.getPromotionalPrice().amount(),
                    variant.getPromotionalPrice().currency()
                ) : null,
            variant.getDimensions() != null ?
                new ShowcaseProductResponse.ProductVariantResponse.DimensionsResponse(
                    variant.getDimensions().weight(),
                    variant.getDimensions().height(),
                    variant.getDimensions().width(),
                    variant.getDimensions().depth()
                ) : null
        );
    }

    /**
     * Query para o caso de uso de detalhes do produto.
     */
    public record GetShowcaseProductDetailsQuery(UUID productId) {}
}


