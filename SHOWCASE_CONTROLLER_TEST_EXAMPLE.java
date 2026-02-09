package br.dev.garage474.mscatalog.adapters.in.web.controller;

import br.dev.garage474.mscatalog.adapters.in.web.dto.ShowcasePageResponse;
import br.dev.garage474.mscatalog.adapters.in.web.dto.ShowcaseProductResponse;
import br.dev.garage474.mscatalog.applications.usecase.GetShowcaseProductDetailsUseCase;
import br.dev.garage474.mscatalog.applications.usecase.ListShowcaseProductsUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes para o ShowcaseController.
 *
 * Nota: Este arquivo é uma referência para testes futuros.
 * Copie para src/test/java e adapte conforme necessário.
 */
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("ShowcaseController Tests")
public class ShowcaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ListShowcaseProductsUseCase listShowcaseProductsUseCase;

    @MockBean
    private GetShowcaseProductDetailsUseCase getShowcaseProductDetailsUseCase;

    private UUID tenantId;
    private UUID productId;
    private UUID brandId;
    private UUID categoryId;

    @BeforeEach
    void setUp() {
        tenantId = UUID.randomUUID();
        productId = UUID.randomUUID();
        brandId = UUID.randomUUID();
        categoryId = UUID.randomUUID();
    }

    @Test
    @DisplayName("Deve listar produtos com paginação")
    void testListProducts() throws Exception {
        // Arrange
        var brandResponse = new ShowcaseProductResponse.BrandResponse(brandId, "Dell");
        var categoryResponse = new ShowcaseProductResponse.CategoryResponse(categoryId, "Eletrônicos");

        var priceResponse = new ShowcaseProductResponse.ProductPriceResponse(
            null,
            new ShowcaseProductResponse.ProductPriceResponse.MoneyResponse(BigDecimal.valueOf(5999.99), "BRL"),
            new ShowcaseProductResponse.ProductPriceResponse.MoneyResponse(BigDecimal.valueOf(4999.99), "BRL")
        );

        var productResponse = new ShowcaseProductResponse(
            productId,
            "Notebook Dell XPS 13",
            "Ultra-portátil com tela 4K",
            "notebook-dell-xps-13",
            brandResponse,
            categoryResponse,
            Arrays.asList("bestseller", "ultrabook"),
            priceResponse,
            Arrays.asList()
        );

        var pageInfo = new ShowcasePageResponse.PageInfoResponse(0, 20, 1, 1);
        var pageResponse = new ShowcasePageResponse(Arrays.asList(productResponse), pageInfo);

        when(listShowcaseProductsUseCase.execute(any()))
            .thenReturn(pageResponse);

        // Act & Assert
        mockMvc.perform(get("/api/v1/showcase/products")
                .header("X-Tenant-ID", tenantId.toString())
                .param("page", "0")
                .param("size", "20")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content").isArray())
            .andExpect(jsonPath("$.content[0].name").value("Notebook Dell XPS 13"))
            .andExpect(jsonPath("$.pageInfo.page").value(0))
            .andExpect(jsonPath("$.pageInfo.size").value(20));
    }

    @Test
    @DisplayName("Deve buscar produtos por termo")
    void testSearchProducts() throws Exception {
        // Arrange
        var brandResponse = new ShowcaseProductResponse.BrandResponse(brandId, "Dell");
        var categoryResponse = new ShowcaseProductResponse.CategoryResponse(categoryId, "Eletrônicos");

        var priceResponse = new ShowcaseProductResponse.ProductPriceResponse(
            null,
            new ShowcaseProductResponse.ProductPriceResponse.MoneyResponse(BigDecimal.valueOf(5999.99), "BRL"),
            null
        );

        var productResponse = new ShowcaseProductResponse(
            productId,
            "Notebook Dell XPS 13",
            "Ultra-portátil com tela 4K",
            "notebook-dell-xps-13",
            brandResponse,
            categoryResponse,
            Arrays.asList("bestseller"),
            priceResponse,
            Arrays.asList()
        );

        var pageInfo = new ShowcasePageResponse.PageInfoResponse(0, 10, 1, 1);
        var pageResponse = new ShowcasePageResponse(Arrays.asList(productResponse), pageInfo);

        when(listShowcaseProductsUseCase.execute(any()))
            .thenReturn(pageResponse);

        // Act & Assert
        mockMvc.perform(get("/api/v1/showcase/products")
                .header("X-Tenant-ID", tenantId.toString())
                .param("search", "notebook")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].name").value("Notebook Dell XPS 13"));
    }

    @Test
    @DisplayName("Deve retornar 400 se X-Tenant-ID inválido")
    void testMissingTenantId() throws Exception {
        mockMvc.perform(get("/api/v1/showcase/products")
                .param("page", "0")
                .param("size", "20")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar 400 se paginação inválida")
    void testInvalidPagination() throws Exception {
        mockMvc.perform(get("/api/v1/showcase/products")
                .header("X-Tenant-ID", tenantId.toString())
                .param("page", "-1")
                .param("size", "20")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar 400 se size > 100")
    void testMaxPageSize() throws Exception {
        mockMvc.perform(get("/api/v1/showcase/products")
                .header("X-Tenant-ID", tenantId.toString())
                .param("page", "0")
                .param("size", "150")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve obter detalhes do produto")
    void testGetProductDetails() throws Exception {
        // Arrange
        var brandResponse = new ShowcaseProductResponse.BrandResponse(brandId, "Dell");
        var categoryResponse = new ShowcaseProductResponse.CategoryResponse(categoryId, "Eletrônicos");

        var priceResponse = new ShowcaseProductResponse.ProductPriceResponse(
            null,
            new ShowcaseProductResponse.ProductPriceResponse.MoneyResponse(BigDecimal.valueOf(5999.99), "BRL"),
            new ShowcaseProductResponse.ProductPriceResponse.MoneyResponse(BigDecimal.valueOf(4999.99), "BRL")
        );

        var variantMoneyResponse = new ShowcaseProductResponse.ProductVariantResponse.MoneyResponse(
            BigDecimal.valueOf(5999.99), "BRL"
        );

        var dimensionsResponse = new ShowcaseProductResponse.ProductVariantResponse.DimensionsResponse(
            1.2, 0.7, 30.0, 20.0
        );

        var variantResponse = new ShowcaseProductResponse.ProductVariantResponse(
            UUID.randomUUID(),
            productId,
            "DELL-XPS-13-512GB",
            "5901234123457",
            variantMoneyResponse,
            new ShowcaseProductResponse.ProductVariantResponse.MoneyResponse(BigDecimal.valueOf(4999.99), "BRL"),
            dimensionsResponse
        );

        var productResponse = new ShowcaseProductResponse(
            productId,
            "Notebook Dell XPS 13",
            "Ultra-portátil com tela 4K",
            "notebook-dell-xps-13",
            brandResponse,
            categoryResponse,
            Arrays.asList("bestseller", "ultrabook"),
            priceResponse,
            Arrays.asList(variantResponse)
        );

        when(getShowcaseProductDetailsUseCase.execute(any()))
            .thenReturn(productResponse);

        // Act & Assert
        mockMvc.perform(get("/api/v1/showcase/products/{id}", productId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Notebook Dell XPS 13"))
            .andExpect(jsonPath("$.variants").isArray())
            .andExpect(jsonPath("$.variants[0].skuCode").value("DELL-XPS-13-512GB"));
    }

    @Test
    @DisplayName("Deve retornar 404 se produto não encontrado")
    void testProductNotFound() throws Exception {
        when(getShowcaseProductDetailsUseCase.execute(any()))
            .thenThrow(new IllegalArgumentException("Produto não encontrado"));

        mockMvc.perform(get("/api/v1/showcase/products/{id}", productId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve filtrar produtos por brand")
    void testFilterByBrand() throws Exception {
        // Arrange
        var pageInfo = new ShowcasePageResponse.PageInfoResponse(0, 20, 1, 5);
        var pageResponse = new ShowcasePageResponse(Arrays.asList(), pageInfo);

        when(listShowcaseProductsUseCase.execute(any()))
            .thenReturn(pageResponse);

        // Act & Assert
        mockMvc.perform(get("/api/v1/showcase/products")
                .header("X-Tenant-ID", tenantId.toString())
                .param("brandId", brandId.toString())
                .param("page", "0")
                .param("size", "20")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.pageInfo.totalElements").value(5));
    }

    @Test
    @DisplayName("Deve filtrar produtos por categoria")
    void testFilterByCategory() throws Exception {
        // Arrange
        var pageInfo = new ShowcasePageResponse.PageInfoResponse(0, 20, 1, 10);
        var pageResponse = new ShowcasePageResponse(Arrays.asList(), pageInfo);

        when(listShowcaseProductsUseCase.execute(any()))
            .thenReturn(pageResponse);

        // Act & Assert
        mockMvc.perform(get("/api/v1/showcase/products")
                .header("X-Tenant-ID", tenantId.toString())
                .param("categoryId", categoryId.toString())
                .param("page", "0")
                .param("size", "20")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.pageInfo.totalElements").value(10));
    }

    @Test
    @DisplayName("Deve suportar múltiplos filtros")
    void testMultipleFilters() throws Exception {
        // Arrange
        var pageInfo = new ShowcasePageResponse.PageInfoResponse(0, 15, 1, 3);
        var pageResponse = new ShowcasePageResponse(Arrays.asList(), pageInfo);

        when(listShowcaseProductsUseCase.execute(any()))
            .thenReturn(pageResponse);

        // Act & Assert
        mockMvc.perform(get("/api/v1/showcase/products")
                .header("X-Tenant-ID", tenantId.toString())
                .param("search", "samsung")
                .param("brandId", brandId.toString())
                .param("categoryId", categoryId.toString())
                .param("page", "0")
                .param("size", "15")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.pageInfo.totalElements").value(3));
    }
}

