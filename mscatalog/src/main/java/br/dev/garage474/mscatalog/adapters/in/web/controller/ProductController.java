package br.dev.garage474.mscatalog.adapters.in.web.controller;

import br.dev.garage474.mscatalog.adapters.in.dto.CreateProductRequest;
import br.dev.garage474.mscatalog.adapters.in.dto.CreateProductVariantRequest;
import br.dev.garage474.mscatalog.adapters.in.dto.ProductResponse;
import br.dev.garage474.mscatalog.adapters.in.dto.ProductVariantResponse;
import br.dev.garage474.mscatalog.applications.usecase.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Controller REST para operações com Produtos e Variantes.
 *
 * Endpoints de Produtos:
 * - POST /api/v1/products - Criar novo produto
 * - GET /api/v1/products - Listar produtos por tenant
 *
 * Endpoints de Variantes:
 * - POST /api/v1/products/{productId}/variants - Criar nova variante
 * - GET /api/v1/products/{productId}/variants - Listar variantes de um produto
 * - PUT /api/v1/products/{productId}/variants/{variantId} - Atualizar variante
 * - DELETE /api/v1/products/{productId}/variants/{variantId} - Deletar variante
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
    private final CreateProductVariantUseCase createProductVariantUseCase;
    private final ListProductVariantsByProductUseCase listProductVariantsByProductUseCase;
    private final UpdateProductVariantUseCase updateProductVariantUseCase;
    private final DeleteProductVariantUseCase deleteProductVariantUseCase;

    public ProductController(
            CreateProductUseCase createProductUseCase,
            ListProductsByTenantUseCase listProductsByTenantUseCase,
            CreateProductVariantUseCase createProductVariantUseCase,
            ListProductVariantsByProductUseCase listProductVariantsByProductUseCase,
            UpdateProductVariantUseCase updateProductVariantUseCase,
            DeleteProductVariantUseCase deleteProductVariantUseCase) {
        this.createProductUseCase = createProductUseCase;
        this.listProductsByTenantUseCase = listProductsByTenantUseCase;
        this.createProductVariantUseCase = createProductVariantUseCase;
        this.listProductVariantsByProductUseCase = listProductVariantsByProductUseCase;
        this.updateProductVariantUseCase = updateProductVariantUseCase;
        this.deleteProductVariantUseCase = deleteProductVariantUseCase;
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

    // ==================== PRODUCT VARIANT ENDPOINTS ====================

    /**
     * POST /api/v1/products/{productId}/variants
     *
     * Cria uma nova variante para um produto específico.
     *
     * @param productId ID do produto
     * @param request Dados da variante a ser criada
     * @return Resposta com dados da variante criada
     */
    @PostMapping("/{productId}/variants")
    public ResponseEntity<ProductVariantResponse> createProductVariant(
            @PathVariable UUID productId,
            @RequestBody CreateProductVariantRequest request) {

        var command = new CreateProductVariantUseCase.CreateProductVariantCommand(
            productId,
            request.skuCode(),
            request.barcode(),
            request.price(),
            request.priceCurrency(),
            request.promotionalPrice(),
            request.promotionalPriceCurrency(),
            request.weight(),
            request.height(),
            request.width(),
            request.depth()
        );

        var response = createProductVariantUseCase.execute(command);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * GET /api/v1/products/{productId}/variants
     *
     * Lista todas as variantes de um produto específico.
     *
     * @param productId ID do produto
     * @return Lista de variantes do produto
     */
    @GetMapping("/{productId}/variants")
    public ResponseEntity<List<ProductVariantResponse>> listProductVariants(
            @PathVariable UUID productId) {

        var query = new ListProductVariantsByProductUseCase.ListProductVariantsQuery(productId);
        var variants = listProductVariantsByProductUseCase.execute(query);

        return ResponseEntity.ok(variants);
    }

    /**
     * PUT /api/v1/products/{productId}/variants/{variantId}
     *
     * Atualiza uma variante de um produto específico.
     *
     * @param productId ID do produto
     * @param variantId ID da variante
     * @param request Dados atualizados da variante
     * @return Resposta com dados da variante atualizada
     */
    @PutMapping("/{productId}/variants/{variantId}")
    public ResponseEntity<ProductVariantResponse> updateProductVariant(
            @PathVariable UUID productId,
            @PathVariable UUID variantId,
            @RequestBody CreateProductVariantRequest request) {

        var command = new UpdateProductVariantUseCase.UpdateProductVariantCommand(
            variantId,
            productId,
            request.skuCode(),
            request.barcode(),
            request.price(),
            request.priceCurrency(),
            request.promotionalPrice(),
            request.promotionalPriceCurrency(),
            request.weight(),
            request.height(),
            request.width(),
            request.depth()
        );

        var response = updateProductVariantUseCase.execute(command);

        return ResponseEntity.ok(response);
    }

    /**
     * DELETE /api/v1/products/{productId}/variants/{variantId}
     *
     * Deleta uma variante de um produto específico.
     *
     * @param productId ID do produto
     * @param variantId ID da variante
     * @return Status 204 (No Content)
     */
    @DeleteMapping("/{productId}/variants/{variantId}")
    public ResponseEntity<Void> deleteProductVariant(
            @PathVariable UUID productId,
            @PathVariable UUID variantId) {

        var command = new DeleteProductVariantUseCase.DeleteProductVariantCommand(variantId);
        deleteProductVariantUseCase.execute(command);

        return ResponseEntity.noContent().build();
    }
}
