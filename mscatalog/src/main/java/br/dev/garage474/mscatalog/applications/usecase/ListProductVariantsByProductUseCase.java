package br.dev.garage474.mscatalog.applications.usecase;

import br.dev.garage474.mscatalog.adapters.in.dto.ProductVariantResponse;
import br.dev.garage474.mscatalog.domain.entities.ProductVariant;
import br.dev.garage474.mscatalog.domain.repositories.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Use Case para listar variantes de um produto.
 *
 * Responsabilidades:
 * - Receber query de listagem
 * - Buscar variantes no repositório
 * - Converter e retornar resposta
 *
 * Arquitetura: Clean Architecture - Application Layer
 */
@Service
public class ListProductVariantsByProductUseCase {

    private final ProductRepository productRepository;

    public ListProductVariantsByProductUseCase(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Executa o caso de uso de listagem de variantes por produto.
     *
     * @param query Query com dados da busca
     * @return Lista de variantes do produto
     */
    @Transactional(readOnly = true)
    public List<ProductVariantResponse> execute(ListProductVariantsQuery query) {
        // 1. Validar que o produto existe
        if (!productRepository.existsProduct(query.productId())) {
            throw new IllegalArgumentException("Produto não encontrado: " + query.productId());
        }

        // 2. Buscar variantes
        List<ProductVariant> variants = productRepository.findProductVariantsByProductId(query.productId());

        // 3. Converter e retornar
        return variants.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
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
     * Query para listar variantes de um produto.
     *
     * Record imutável com os parâmetros da busca.
     */
    public record ListProductVariantsQuery(
        UUID productId
    ) {}
}

