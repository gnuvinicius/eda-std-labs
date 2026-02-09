package br.dev.garage474.mscatalog.applications.usecase;

import br.dev.garage474.mscatalog.adapters.in.web.dto.ShowcasePageResponse;
import br.dev.garage474.mscatalog.adapters.in.web.dto.ShowcaseProductResponse;
import br.dev.garage474.mscatalog.domain.entities.Product;
import br.dev.garage474.mscatalog.domain.repositories.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Use Case para listar produtos no Showcase com paginação e filtros.
 *
 * Responsabilidades:
 * - Receber query de listagem com filtros e paginação
 * - Delegar busca ao repositório
 * - Converter e retornar respostas para o frontend
 *
 * Arquitetura: Clean Architecture - Application Layer
 */
@Service
@Slf4j
public class ListShowcaseProductsUseCase {

    private final ProductRepository productRepository;

    public ListShowcaseProductsUseCase(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Executa o caso de uso de listagem de produtos com filtros e paginação.
     *
     * @param query Query com dados de filtro e paginação
     * @return Página de produtos para exibir no Showcase
     */
    @Transactional(readOnly = true)
    public ShowcasePageResponse execute(ListShowcaseProductsQuery query) {
        try {
            // 1. Buscar produtos com filtros e paginação
            var pageResult = productRepository.findProductsByTenantWithFilters(
                query.tenantId(),
                query.searchTerm(),
                query.brandId(),
                query.categoryId(),
                query.page(),
                query.size()
            );

            // 2. Converter para responses
            var content = pageResult.content().stream()
                .map(this::convertToShowcaseResponse)
                .collect(Collectors.toList());

            // 3. Retornar page response
            var pageInfo = new ShowcasePageResponse.PageInfoResponse(
                pageResult.page(),
                pageResult.size(),
                pageResult.totalPages(),
                pageResult.totalElements()
            );

            return new ShowcasePageResponse(content, pageInfo);
        } catch (Exception e) {
            log.error("erro ao listar produtos do showcase com filtros: tenantId={}, searchTerm={}, brandId={}, categoryId={}",
                query.tenantId(), query.searchTerm(), query.brandId(), query.categoryId(), e);
            throw e;
        }
    }

    /**
     * Converte Product (Domain Entity) para ShowcaseProductResponse (DTO).
     */
    private ShowcaseProductResponse convertToShowcaseResponse(Product product) {
        // Calcular preços das variantes
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
     * Query para o caso de uso de listagem com filtros.
     */
    public record ListShowcaseProductsQuery(
        UUID tenantId,
        String searchTerm,
        UUID brandId,
        UUID categoryId,
        int page,
        int size
    ) {}
}


