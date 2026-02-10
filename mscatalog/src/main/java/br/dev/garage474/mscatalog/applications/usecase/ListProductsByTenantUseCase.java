package br.dev.garage474.mscatalog.applications.usecase;

import br.dev.garage474.mscatalog.adapters.in.dto.ProductResponse;
import br.dev.garage474.mscatalog.domain.entities.Product;
import br.dev.garage474.mscatalog.domain.repositories.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Use Case para listar produtos por tenant.
 *
 * Responsabilidades:
 * - Receber query de listagem
 * - Buscar produtos do tenant
 * - Converter e retornar respostas
 *
 * Arquitetura: Clean Architecture - Application Layer
 */
@Service
public class ListProductsByTenantUseCase {

    private final ProductRepository productRepository;

    public ListProductsByTenantUseCase(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Executa o caso de uso de listagem de produtos.
     *
     * @param query Query com dados de filtro
     * @return Lista de produtos do tenant
     */
    @Transactional(readOnly = true)
    public List<ProductResponse> execute(ListProductsQuery query) {
        // 1. Buscar produtos do tenant (por enquanto todos, pois Product ainda não tem tenantId)
        List<Product> products = productRepository.findAllProducts();

        // 2. Converter e retornar como resposta
        return products.stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
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
     * Query para listar produtos.
     *
     * Record imutável com os dados necessários para listar produtos.
     */
    public record ListProductsQuery(
        UUID tenantId
    ) {}
}

