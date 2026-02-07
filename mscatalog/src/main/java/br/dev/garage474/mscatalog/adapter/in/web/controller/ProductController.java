package br.dev.garage474.mscatalog.adapter.in.web.controller;

import br.dev.garage474.mscatalog.adapter.in.web.dto.CreateProductRequest;
import br.dev.garage474.mscatalog.adapter.in.web.dto.ProductResponse;
import br.dev.garage474.mscatalog.application.usecase.CreateProductUseCase;
import br.dev.garage474.mscatalog.application.usecase.ListProductsByTenantUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Controller REST para operações com Produtos.
 *
 * Endpoints:
 * - POST /api/v1/products - Criar novo produto
 * - GET /api/v1/products - Listar produtos por tenant
 *
 * Implementação de Clean Architecture:
 * - Recebe requisições HTTP
 * - Delegação para Use Cases
 * - Conversão DTO ↔ Domain
 */
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final CreateProductUseCase createProductUseCase;
    private final ListProductsByTenantUseCase listProductsByTenantUseCase;

    public ProductController(
            CreateProductUseCase createProductUseCase,
            ListProductsByTenantUseCase listProductsByTenantUseCase) {
        this.createProductUseCase = createProductUseCase;
        this.listProductsByTenantUseCase = listProductsByTenantUseCase;
    }

    /**
     * POST /api/v1/products
     *
     * Cria um novo produto para um tenant específico.
     *
     * @param tenantId ID do tenant (obtido do header X-Tenant-ID)
     * @param request Dados do produto a ser criado
     * @return Resposta com dados do produto criado
     */
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @RequestBody CreateProductRequest request) {

        var command = new CreateProductUseCase.CreateProductCommand(
            tenantId,
            request.name(),
            request.description(),
            request.slug(),
            request.brandId() != null ? UUID.fromString(request.brandId()) : null,
            request.categoryId() != null ? UUID.fromString(request.categoryId()) : null,
            request.tags()
        );

        var response = createProductUseCase.execute(command);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * GET /api/v1/products
     *
     * Lista todos os produtos de um tenant específico.
     *
     * @param tenantId ID do tenant (obtido do header X-Tenant-ID)
     * @return Lista de produtos do tenant
     */
    @GetMapping
    public ResponseEntity<List<ProductResponse>> listProductsByTenant(
            @RequestHeader("X-Tenant-ID") UUID tenantId) {

        var query = new ListProductsByTenantUseCase.ListProductsQuery(tenantId);
        var products = listProductsByTenantUseCase.execute(query);

        return ResponseEntity.ok(products);
    }
}

