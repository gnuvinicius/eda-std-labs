package br.dev.garage474.mscatalog.adapter.in.web.controller;

import br.dev.garage474.mscatalog.adapter.in.web.dto.CategoryResponse;
import br.dev.garage474.mscatalog.adapter.in.web.dto.CreateCategoryRequest;
import br.dev.garage474.mscatalog.application.usecase.CreateCategoryUseCase;
import br.dev.garage474.mscatalog.application.usecase.ListCategoriesByTenantUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Controller REST para operações com Categorias.
 *
 * Endpoints:
 * - POST /api/v1/categories - Criar nova categoria
 * - GET /api/v1/categories - Listar categorias por tenant
 *
 * Implementação de Clean Architecture:
 * - Recebe requisições HTTP
 * - Delegação para Use Cases
 * - Conversão DTO ↔ Domain
 */
@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CreateCategoryUseCase createCategoryUseCase;
    private final ListCategoriesByTenantUseCase listCategoriesByTenantUseCase;

    public CategoryController(
            CreateCategoryUseCase createCategoryUseCase,
            ListCategoriesByTenantUseCase listCategoriesByTenantUseCase) {
        this.createCategoryUseCase = createCategoryUseCase;
        this.listCategoriesByTenantUseCase = listCategoriesByTenantUseCase;
    }

    /**
     * POST /api/v1/categories
     *
     * Cria uma nova categoria para um tenant específico.
     *
     * @param tenantId ID do tenant (obtido do header X-Tenant-ID)
     * @param request Dados da categoria a ser criada
     * @return Resposta com dados da categoria criada
     */
    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @RequestBody CreateCategoryRequest request) {

        var command = new CreateCategoryUseCase.CreateCategoryCommand(
            tenantId,
            request.name(),
            request.parentCategoryId() != null ? UUID.fromString(request.parentCategoryId()) : null
        );

        var response = createCategoryUseCase.execute(command);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * GET /api/v1/categories
     *
     * Lista todas as categorias de um tenant específico.
     *
     * @param tenantId ID do tenant (obtido do header X-Tenant-ID)
     * @return Lista de categorias do tenant
     */
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> listCategoriesByTenant(
            @RequestHeader("X-Tenant-ID") UUID tenantId) {

        var query = new ListCategoriesByTenantUseCase.ListCategoriesQuery(tenantId);
        var categories = listCategoriesByTenantUseCase.execute(query);

        return ResponseEntity.ok(categories);
    }
}

