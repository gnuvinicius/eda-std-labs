package br.dev.garage474.mscatalog.adapters.in.web.controller;

import br.dev.garage474.mscatalog.adapters.in.dto.ShowcasePageResponse;
import br.dev.garage474.mscatalog.adapters.in.dto.ShowcaseProductResponse;
import br.dev.garage474.mscatalog.applications.usecase.GetShowcaseProductDetailsUseCase;
import br.dev.garage474.mscatalog.applications.usecase.ListShowcaseProductsUseCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Controller REST para operações de Showcase de Produtos.
 * <p>
 * Endpoints disponíveis (apenas GETs para frontend/mobile):
 * - GET /api/v1/showcase/products - Listar produtos paginados com filtros opcionais
 * - GET /api/v1/showcase/products/{id} - Obter detalhes de um produto específico
 * <p>
 * Parâmetros de Query:
 * - page: número da página (default: 0)
 * - size: tamanho da página (default: 20)
 * - search: termo de busca em nome e descrição (optional)
 * - brandId: filtro por brand (optional, UUID)
 * - categoryId: filtro por categoria (optional, UUID)
 * <p>
 * Implementação de Clean Architecture:
 * - Controller recebe requisições HTTP
 * - Delegação para Use Cases
 * - Conversão DTO ← Domain
 * <p>
 * Sem autenticação/autorização: dados públicos para qualquer cliente frontend/mobile
 */
@RestController
@RequestMapping("/api/v1/showcase/products")
@Slf4j
public class ShowcaseController {

    private final ListShowcaseProductsUseCase listShowcaseProductsUseCase;
    private final GetShowcaseProductDetailsUseCase getShowcaseProductDetailsUseCase;

    public ShowcaseController(
            ListShowcaseProductsUseCase listShowcaseProductsUseCase,
            GetShowcaseProductDetailsUseCase getShowcaseProductDetailsUseCase) {
        this.listShowcaseProductsUseCase = listShowcaseProductsUseCase;
        this.getShowcaseProductDetailsUseCase = getShowcaseProductDetailsUseCase;
    }

    /**
     * GET /api/v1/showcase/products
     * <p>
     * Lista produtos com paginação e filtros opcionais.
     * Sempre requer tenantId do header para isolar dados por cliente.
     *
     * @param tenantId   ID do tenant (obtido do header X-Tenant-ID)
     * @param page       Número da página (0-based, default: 0)
     * @param size       Tamanho da página (default: 20)
     * @param search     Termo de busca em nome e descrição (optional)
     * @param brandId    ID da brand para filtrar (optional)
     * @param categoryId ID da categoria para filtrar (optional)
     * @return Página com produtos que atendem aos critérios
     */
    @GetMapping
    public ResponseEntity<ShowcasePageResponse> listProducts(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String brandId,
            @RequestParam(required = false) String categoryId) {

        try {
            // Validar paginação
            if (page < 0 || size <= 0 || size > 100) {
                log.warn("parâmetros de paginação inválidos: page={}, size={}", page, size);
                return ResponseEntity.badRequest().build();
            }

            UUID brand = brandId != null ? UUID.fromString(brandId) : null;
            UUID category = categoryId != null ? UUID.fromString(categoryId) : null;

            var query = new ListShowcaseProductsUseCase.ListShowcaseProductsQuery(
                    tenantId,
                    search,
                    brand,
                    category,
                    page,
                    size
            );

            var response = listShowcaseProductsUseCase.execute(query);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("parâmetro inválido na requisição do showcase: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("erro ao listar produtos do showcase", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * GET /api/v1/showcase/products/{id}
     * <p>
     * Obtém detalhes completos de um produto específico.
     * Inclui todas as variantes e preços.
     *
     * @param id ID do produto a detalhar
     * @return Detalhes completos do produto
     */
    @GetMapping("/{id}")
    public ResponseEntity<ShowcaseProductResponse> getProductDetails(
            @PathVariable UUID id) {

        try {
            var query = new GetShowcaseProductDetailsUseCase.GetShowcaseProductDetailsQuery(id);
            var response = getShowcaseProductDetailsUseCase.execute(query);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.warn("produto não encontrado: id={}", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("erro ao buscar detalhes do produto: id={}", id, e);
            return ResponseEntity.internalServerError().build();
        }
    }
}

