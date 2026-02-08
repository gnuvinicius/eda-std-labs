package br.dev.garage474.mscatalog.adapters.in.web.controller;

import br.dev.garage474.mscatalog.adapters.in.web.dto.CreateProductVariantRequest;
import br.dev.garage474.mscatalog.adapters.in.web.dto.ProductVariantResponse;
import br.dev.garage474.mscatalog.applications.usecase.CreateProductVariantUseCase;
import br.dev.garage474.mscatalog.applications.usecase.ListProductVariantsByProductUseCase;
import br.dev.garage474.mscatalog.applications.usecase.UpdateProductVariantUseCase;
import br.dev.garage474.mscatalog.applications.usecase.DeleteProductVariantUseCase;
import br.dev.garage474.mscatalog.applications.usecase.CreateProductUseCase;
import br.dev.garage474.mscatalog.applications.usecase.ListProductsByTenantUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para ProductController (ProductVariant endpoints).
 *
 * Testes implementados:
 * 1. Criar variante com sucesso
 * 2. Listar variantes
 * 3. Atualizar variante
 * 4. Deletar variante
 */
@DisplayName("ProductController Tests - ProductVariant Endpoints")
class ProductControllerVariantTest {

    @Mock
    private CreateProductUseCase createProductUseCase;

    @Mock
    private ListProductsByTenantUseCase listProductsByTenantUseCase;

    @Mock
    private CreateProductVariantUseCase createProductVariantUseCase;

    @Mock
    private ListProductVariantsByProductUseCase listProductVariantsByProductUseCase;

    @Mock
    private UpdateProductVariantUseCase updateProductVariantUseCase;

    @Mock
    private DeleteProductVariantUseCase deleteProductVariantUseCase;

    private ProductController controller;
    private ObjectMapper objectMapper;

    private UUID productId;
    private UUID variantId;
    private CreateProductVariantRequest createRequest;
    private ProductVariantResponse mockResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new ProductController(
                createProductUseCase,
                listProductsByTenantUseCase,
                createProductVariantUseCase,
                listProductVariantsByProductUseCase,
                updateProductVariantUseCase,
                deleteProductVariantUseCase
        );
        objectMapper = new ObjectMapper();

        productId = UUID.randomUUID();
        variantId = UUID.randomUUID();

        createRequest = new CreateProductVariantRequest(
                "PROD-001",
                "1234567890",
                BigDecimal.valueOf(99.99),
                "BRL",
                BigDecimal.valueOf(79.99),
                "BRL",
                0.5,
                10.0,
                5.0,
                3.0
        );

        mockResponse = new ProductVariantResponse(
                variantId,
                productId,
                "PROD-001",
                "1234567890",
                new ProductVariantResponse.MoneyResponse(BigDecimal.valueOf(99.99), "BRL"),
                new ProductVariantResponse.MoneyResponse(BigDecimal.valueOf(79.99), "BRL"),
                new ProductVariantResponse.DimensionsResponse(0.5, 10.0, 5.0, 3.0)
        );
    }

    @Test
    @DisplayName("Should create product variant successfully")
    void testCreateProductVariant() {
        // Arrange
        var command = new CreateProductVariantUseCase.CreateProductVariantCommand(
                productId,
                "PROD-001",
                "1234567890",
                BigDecimal.valueOf(99.99),
                "BRL",
                BigDecimal.valueOf(79.99),
                "BRL",
                0.5,
                10.0,
                5.0,
                3.0
        );

        // Mock retorna resposta
        org.mockito.Mockito.when(createProductVariantUseCase.execute(org.mockito.ArgumentMatchers.any()))
                .thenReturn(mockResponse);

        // Act
        var response = controller.createProductVariant(productId, createRequest);

        // Assert
        assertNotNull(response);
        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(variantId, response.getBody().id());
        assertEquals("PROD-001", response.getBody().skuCode());
    }

    @Test
    @DisplayName("Should list product variants successfully")
    void testListProductVariants() {
        // Arrange
        ProductVariantResponse variant1 = new ProductVariantResponse(
                UUID.randomUUID(),
                productId,
                "PROD-001",
                "1234567890",
                new ProductVariantResponse.MoneyResponse(BigDecimal.valueOf(99.99), "BRL"),
                null,
                null
        );

        ProductVariantResponse variant2 = new ProductVariantResponse(
                UUID.randomUUID(),
                productId,
                "PROD-002",
                "1234567891",
                new ProductVariantResponse.MoneyResponse(BigDecimal.valueOf(149.99), "BRL"),
                null,
                null
        );

        List<ProductVariantResponse> variants = Arrays.asList(variant1, variant2);

        org.mockito.Mockito.when(listProductVariantsByProductUseCase.execute(org.mockito.ArgumentMatchers.any()))
                .thenReturn(variants);

        // Act
        var response = controller.listProductVariants(productId);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("PROD-001", response.getBody().get(0).skuCode());
        assertEquals("PROD-002", response.getBody().get(1).skuCode());
    }

    @Test
    @DisplayName("Should update product variant successfully")
    void testUpdateProductVariant() {
        // Arrange
        ProductVariantResponse updatedResponse = new ProductVariantResponse(
                variantId,
                productId,
                "PROD-001-UPDATED",
                "1234567890",
                new ProductVariantResponse.MoneyResponse(BigDecimal.valueOf(89.99), "BRL"),
                null,
                null
        );

        org.mockito.Mockito.when(updateProductVariantUseCase.execute(org.mockito.ArgumentMatchers.any()))
                .thenReturn(updatedResponse);

        // Act
        var response = controller.updateProductVariant(productId, variantId, createRequest);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("PROD-001-UPDATED", response.getBody().skuCode());
    }

    @Test
    @DisplayName("Should delete product variant successfully")
    void testDeleteProductVariant() {
        // Act
        var response = controller.deleteProductVariant(productId, variantId);

        // Assert
        assertNotNull(response);
        assertEquals(204, response.getStatusCodeValue());
    }
}






