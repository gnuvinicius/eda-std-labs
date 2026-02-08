package br.dev.garage474.mscatalog.applications.usecase;

import br.dev.garage474.mscatalog.adapters.in.web.dto.BrandResponse;
import br.dev.garage474.mscatalog.domain.entities.Brand;
import br.dev.garage474.mscatalog.domain.repositories.BrandRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Use Case para listar marcas por tenant.
 *
 * Responsabilidades:
 * - Receber query de listagem
 * - Buscar marcas do tenant
 * - Converter e retornar respostas
 *
 * Arquitetura: Clean Architecture - Application Layer
 */
@Service
public class ListBrandsByTenantUseCase {

    private final BrandRepository brandRepository;

    public ListBrandsByTenantUseCase(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    /**
     * Executa o caso de uso de listagem de marcas.
     *
     * @param query Query com dados de filtro
     * @return Lista de marcas do tenant
     */
    @Transactional(readOnly = true)
    public List<BrandResponse> execute(ListBrandsQuery query) {
        // 1. Buscar marcas do tenant (por enquanto todos)
        List<Brand> brands = brandRepository.findAllBrands(query.tenantId);

        // 2. Converter e retornar como resposta
        return brands.stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
    }

    /**
     * Converte Brand (Domain Entity) para BrandResponse (DTO).
     */
    private BrandResponse convertToResponse(Brand brand) {
        return new BrandResponse(
            brand.getId(),
            brand.getName()
        );
    }

    /**
     * Query para listar marcas.
     *
     * Record imutável com os dados necessários para listar marcas.
     */
    public record ListBrandsQuery(
        UUID tenantId
    ) {}
}

