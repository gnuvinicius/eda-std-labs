package br.dev.garage474.mscatalog.applications.usecase;

import br.dev.garage474.mscatalog.adapters.in.dto.BrandResponse;
import br.dev.garage474.mscatalog.domain.entities.Brand;
import br.dev.garage474.mscatalog.domain.repositories.BrandRepository;
import br.dev.garage474.mscatalog.domain.services.BrandService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Use Case para criar uma nova marca.
 *
 * Responsabilidades:
 * - Receber comando de criação
 * - Validar regras de negócio (via BrandService)
 * - Persistir a marca
 * - Retornar resposta
 *
 * Arquitetura: Clean Architecture - Application Layer
 */
@Service
public class CreateBrandUseCase {

    private final BrandService brandService;
    private final BrandRepository brandRepository;

    public CreateBrandUseCase(
            BrandService brandService,
            BrandRepository brandRepository) {
        this.brandService = brandService;
        this.brandRepository = brandRepository;
    }

    /**
     * Executa o caso de uso de criação de marca.
     *
     * @param command Comando com dados da marca
     * @return Resposta com dados da marca criada
     */
    @Transactional
    public BrandResponse execute(CreateBrandCommand command) {
        // 1. Validar regras de negócio através do BrandService
        brandService.validateBrandCreation(command.name());

        // 2. Criar entidade Brand
        Brand brand = new Brand();
        brand.setName(command.name());
        brand.setTenantId(command.tenantId());

        // 3. Persistir
        Brand savedBrand = brandRepository.saveBrand(brand);

        // 4. Converter e retornar como resposta
        return convertToResponse(savedBrand);
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
     * Comando para criar marca.
     *
     * Record imutável com os dados necessários para criar uma marca.
     */
    public record CreateBrandCommand(
        UUID tenantId,
        String name
    ) {}
}

