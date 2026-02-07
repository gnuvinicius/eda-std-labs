package br.dev.garage474.mscatalog.adapter.in.web.controller;

import br.dev.garage474.mscatalog.adapter.in.web.dto.BrandResponse;
import br.dev.garage474.mscatalog.adapter.in.web.dto.CreateBrandRequest;
import br.dev.garage474.mscatalog.application.usecase.CreateBrandUseCase;
import br.dev.garage474.mscatalog.application.usecase.ListBrandsByTenantUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Controller REST para operações com Marcas.
 *
 * Endpoints:
 * - POST /api/v1/brands - Criar nova marca
 * - GET /api/v1/brands - Listar marcas por tenant
 *
 * Implementação de Clean Architecture:
 * - Recebe requisições HTTP
 * - Delegação para Use Cases
 * - Conversão DTO ↔ Domain
 */
@RestController
@RequestMapping("/api/v1/brands")
public class BrandController {

    private final CreateBrandUseCase createBrandUseCase;
    private final ListBrandsByTenantUseCase listBrandsByTenantUseCase;

    public BrandController(
            CreateBrandUseCase createBrandUseCase,
            ListBrandsByTenantUseCase listBrandsByTenantUseCase) {
        this.createBrandUseCase = createBrandUseCase;
        this.listBrandsByTenantUseCase = listBrandsByTenantUseCase;
    }

    /**
     * POST /api/v1/brands
     *
     * Cria uma nova marca para um tenant específico.
     *
     * @param tenantId ID do tenant (obtido do header X-Tenant-ID)
     * @param request Dados da marca a ser criada
     * @return Resposta com dados da marca criada
     */
    @PostMapping
    public ResponseEntity<BrandResponse> createBrand(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @RequestBody CreateBrandRequest request) {

        var command = new CreateBrandUseCase.CreateBrandCommand(
            tenantId,
            request.name()
        );

        var response = createBrandUseCase.execute(command);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * GET /api/v1/brands
     *
     * Lista todas as marcas de um tenant específico.
     *
     * @param tenantId ID do tenant (obtido do header X-Tenant-ID)
     * @return Lista de marcas do tenant
     */
    @GetMapping
    public ResponseEntity<List<BrandResponse>> listBrandsByTenant(
            @RequestHeader("X-Tenant-ID") UUID tenantId) {

        var query = new ListBrandsByTenantUseCase.ListBrandsQuery(tenantId);
        var brands = listBrandsByTenantUseCase.execute(query);

        return ResponseEntity.ok(brands);
    }
}

