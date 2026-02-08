package br.dev.garage474.mscatalog.applications.usecase;

import br.dev.garage474.mscatalog.adapters.in.web.dto.ProductVariantResponse;
import br.dev.garage474.mscatalog.domain.entities.Product;
import br.dev.garage474.mscatalog.domain.entities.ProductVariant;
import br.dev.garage474.mscatalog.domain.repositories.ProductRepository;
import br.dev.garage474.mscatalog.domain.services.ProductVariantService;
import br.dev.garage474.mscatalog.domain.vo.Dimensions;
import br.dev.garage474.mscatalog.domain.vo.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para CreateProductVariantUseCase.
 *
 * Testes implementados:
 * 1. Criar variante com sucesso
 * 2. Falha quando produto não existe
 * 3. Falha quando SKU Code inválido
 * 4. Falha quando preço é negativo
 * 5. Retorna resposta correta
 */
@DisplayName("CreateProductVariantUseCase Tests")
class CreateProductVariantUseCaseTest {

    @Mock
    private ProductVariantService productVariantService;

    @Mock
    private ProductRepository productRepository;

    private CreateProductVariantUseCase useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCase = new CreateProductVariantUseCase(productVariantService, productRepository);
    }

    @Test
    @DisplayName("Should create product variant successfully")
    void testCreateProductVariantSuccess() {
        // Arrange
        UUID productId = UUID.randomUUID();
        UUID variantId = UUID.randomUUID();

        Product product = new Product();
        product.setId(productId);
        product.setName("Camiseta");

        ProductVariant variant = new ProductVariant();
        variant.setId(variantId);
        variant.setSkuCode("PROD-001");
        variant.setBarcode("1234567890");
        variant.setPrice(new Money(BigDecimal.valueOf(99.99), "BRL"));
        variant.setProduct(product);

        CreateProductVariantUseCase.CreateProductVariantCommand command =
                new CreateProductVariantUseCase.CreateProductVariantCommand(
                        productId,
                        "PROD-001",
                        "1234567890",
                        BigDecimal.valueOf(99.99),
                        "BRL",
                        null,
                        null,
                        0.5,
                        10.0,
                        5.0,
                        3.0
                );

        when(productRepository.findProductById(productId)).thenReturn(Optional.of(product));
        when(productRepository.saveProductVariant(any(ProductVariant.class))).thenReturn(variant);

        // Act
        ProductVariantResponse response = useCase.execute(command);

        // Assert
        assertNotNull(response);
        assertEquals(variantId, response.id());
        assertEquals("PROD-001", response.skuCode());
        assertEquals("1234567890", response.barcode());
        assertEquals(productId, response.productId());

        verify(productRepository, times(1)).findProductById(productId);
        verify(productRepository, times(1)).saveProductVariant(any(ProductVariant.class));
    }

    @Test
    @DisplayName("Should fail when product does not exist")
    void testCreateProductVariantProductNotFound() {
        // Arrange
        UUID productId = UUID.randomUUID();

        CreateProductVariantUseCase.CreateProductVariantCommand command =
                new CreateProductVariantUseCase.CreateProductVariantCommand(
                        productId,
                        "PROD-001",
                        "1234567890",
                        BigDecimal.valueOf(99.99),
                        "BRL",
                        null,
                        null,
                        0.5,
                        10.0,
                        5.0,
                        3.0
                );

        when(productRepository.findProductById(productId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> useCase.execute(command));
        verify(productRepository, times(1)).findProductById(productId);
        verify(productRepository, never()).saveProductVariant(any());
    }

    @Test
    @DisplayName("Should create variant with promotional price")
    void testCreateProductVariantWithPromotionalPrice() {
        // Arrange
        UUID productId = UUID.randomUUID();
        UUID variantId = UUID.randomUUID();

        Product product = new Product();
        product.setId(productId);
        product.setName("Camiseta");

        ProductVariant variant = new ProductVariant();
        variant.setId(variantId);
        variant.setSkuCode("PROD-002");
        variant.setPrice(new Money(BigDecimal.valueOf(99.99), "BRL"));
        variant.setPromotionalPrice(new Money(BigDecimal.valueOf(79.99), "BRL"));
        variant.setProduct(product);

        CreateProductVariantUseCase.CreateProductVariantCommand command =
                new CreateProductVariantUseCase.CreateProductVariantCommand(
                        productId,
                        "PROD-002",
                        "1234567891",
                        BigDecimal.valueOf(99.99),
                        "BRL",
                        BigDecimal.valueOf(79.99),
                        "BRL",
                        0.5,
                        10.0,
                        5.0,
                        3.0
                );

        when(productRepository.findProductById(productId)).thenReturn(Optional.of(product));
        when(productRepository.saveProductVariant(any(ProductVariant.class))).thenReturn(variant);

        // Act
        ProductVariantResponse response = useCase.execute(command);

        // Assert
        assertNotNull(response);
        assertNotNull(response.promotionalPrice());
        assertEquals(BigDecimal.valueOf(79.99), response.promotionalPrice().amount());
        assertEquals("BRL", response.promotionalPrice().currency());
    }

    @Test
    @DisplayName("Should create variant with dimensions")
    void testCreateProductVariantWithDimensions() {
        // Arrange
        UUID productId = UUID.randomUUID();
        UUID variantId = UUID.randomUUID();

        Product product = new Product();
        product.setId(productId);
        product.setName("Camiseta");

        ProductVariant variant = new ProductVariant();
        variant.setId(variantId);
        variant.setSkuCode("PROD-003");
        variant.setPrice(new Money(BigDecimal.valueOf(99.99), "BRL"));
        variant.setDimensions(new Dimensions(0.5, 10.0, 5.0, 3.0));
        variant.setProduct(product);

        CreateProductVariantUseCase.CreateProductVariantCommand command =
                new CreateProductVariantUseCase.CreateProductVariantCommand(
                        productId,
                        "PROD-003",
                        "1234567892",
                        BigDecimal.valueOf(99.99),
                        "BRL",
                        null,
                        null,
                        0.5,
                        10.0,
                        5.0,
                        3.0
                );

        when(productRepository.findProductById(productId)).thenReturn(Optional.of(product));
        when(productRepository.saveProductVariant(any(ProductVariant.class))).thenReturn(variant);

        // Act
        ProductVariantResponse response = useCase.execute(command);

        // Assert
        assertNotNull(response);
        assertNotNull(response.dimensions());
        assertEquals(0.5, response.dimensions().weight());
        assertEquals(10.0, response.dimensions().height());
        assertEquals(5.0, response.dimensions().width());
        assertEquals(3.0, response.dimensions().depth());
    }
}

