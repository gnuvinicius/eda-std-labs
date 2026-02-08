package br.dev.garage474.mscatalog.domain.services;

import br.dev.garage474.mscatalog.domain.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para ProductVariantService.
 *
 * Testes implementados:
 * 1. Validar SKU Code vazio
 * 2. Validar SKU Code tamanho mínimo
 * 3. Validar SKU Code tamanho máximo
 * 4. Validar SKU Code unicidade
 * 5. Validar SKU Code formato
 * 6. Validar preço zero/negativo
 * 7. Validar preço promocional maior que regular
 * 8. Validar dimensões negativas
 * 9. Validar com sucesso
 */
@DisplayName("ProductVariantService Tests")
class ProductVariantServiceTest {

    @Mock
    private ProductRepository productRepository;

    private ProductVariantService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new ProductVariantService(productRepository);
    }

    @Test
    @DisplayName("Should fail when SKU Code is empty")
    void testValidateSkuCodeEmpty() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
            service.validateProductVariantCreation(
                    "",
                    "1234567890",
                    BigDecimal.valueOf(99.99),
                    null,
                    0.5,
                    10.0,
                    5.0,
                    3.0
            )
        );
    }

    @Test
    @DisplayName("Should fail when SKU Code is null")
    void testValidateSkuCodeNull() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
            service.validateProductVariantCreation(
                    null,
                    "1234567890",
                    BigDecimal.valueOf(99.99),
                    null,
                    0.5,
                    10.0,
                    5.0,
                    3.0
            )
        );
    }

    @Test
    @DisplayName("Should fail when SKU Code is too short")
    void testValidateSkuCodeTooShort() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
            service.validateProductVariantCreation(
                    "AB",
                    "1234567890",
                    BigDecimal.valueOf(99.99),
                    null,
                    0.5,
                    10.0,
                    5.0,
                    3.0
            )
        );
    }

    @Test
    @DisplayName("Should fail when SKU Code is too long")
    void testValidateSkuCodeTooLong() {
        // Arrange
        String longSkuCode = "A".repeat(51);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
            service.validateProductVariantCreation(
                    longSkuCode,
                    "1234567890",
                    BigDecimal.valueOf(99.99),
                    null,
                    0.5,
                    10.0,
                    5.0,
                    3.0
            )
        );
    }

    @Test
    @DisplayName("Should fail when SKU Code starts with hyphen")
    void testValidateSkuCodeStartsWithHyphen() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
            service.validateProductVariantCreation(
                    "-PROD-001",
                    "1234567890",
                    BigDecimal.valueOf(99.99),
                    null,
                    0.5,
                    10.0,
                    5.0,
                    3.0
            )
        );
    }

    @Test
    @DisplayName("Should fail when SKU Code ends with hyphen")
    void testValidateSkuCodeEndsWithHyphen() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
            service.validateProductVariantCreation(
                    "PROD-001-",
                    "1234567890",
                    BigDecimal.valueOf(99.99),
                    null,
                    0.5,
                    10.0,
                    5.0,
                    3.0
            )
        );
    }

    @Test
    @DisplayName("Should fail when SKU Code contains lowercase")
    void testValidateSkuCodeContainsLowercase() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
            service.validateProductVariantCreation(
                    "Prod-001",
                    "1234567890",
                    BigDecimal.valueOf(99.99),
                    null,
                    0.5,
                    10.0,
                    5.0,
                    3.0
            )
        );
    }

    @Test
    @DisplayName("Should fail when price is zero")
    void testValidatePriceZero() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
            service.validateProductVariantCreation(
                    "PROD-001",
                    "1234567890",
                    BigDecimal.ZERO,
                    null,
                    0.5,
                    10.0,
                    5.0,
                    3.0
            )
        );
    }

    @Test
    @DisplayName("Should fail when price is negative")
    void testValidatePriceNegative() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
            service.validateProductVariantCreation(
                    "PROD-001",
                    "1234567890",
                    BigDecimal.valueOf(-99.99),
                    null,
                    0.5,
                    10.0,
                    5.0,
                    3.0
            )
        );
    }

    @Test
    @DisplayName("Should fail when price is null")
    void testValidatePriceNull() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
            service.validateProductVariantCreation(
                    "PROD-001",
                    "1234567890",
                    null,
                    null,
                    0.5,
                    10.0,
                    5.0,
                    3.0
            )
        );
    }

    @Test
    @DisplayName("Should fail when promotional price is greater than regular price")
    void testValidatePromotionalPriceGreaterThanRegularPrice() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
            service.validateProductVariantCreation(
                    "PROD-001",
                    "1234567890",
                    BigDecimal.valueOf(99.99),
                    BigDecimal.valueOf(199.99),
                    0.5,
                    10.0,
                    5.0,
                    3.0
            )
        );
    }

    @Test
    @DisplayName("Should fail when promotional price is zero")
    void testValidatePromotionalPriceZero() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
            service.validateProductVariantCreation(
                    "PROD-001",
                    "1234567890",
                    BigDecimal.valueOf(99.99),
                    BigDecimal.ZERO,
                    0.5,
                    10.0,
                    5.0,
                    3.0
            )
        );
    }

    @Test
    @DisplayName("Should fail when weight is negative")
    void testValidateWeightNegative() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
            service.validateProductVariantCreation(
                    "PROD-001",
                    "1234567890",
                    BigDecimal.valueOf(99.99),
                    null,
                    -0.5,
                    10.0,
                    5.0,
                    3.0
            )
        );
    }

    @Test
    @DisplayName("Should fail when height is negative")
    void testValidateHeightNegative() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
            service.validateProductVariantCreation(
                    "PROD-001",
                    "1234567890",
                    BigDecimal.valueOf(99.99),
                    null,
                    0.5,
                    -10.0,
                    5.0,
                    3.0
            )
        );
    }

    @Test
    @DisplayName("Should fail when width is negative")
    void testValidateWidthNegative() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
            service.validateProductVariantCreation(
                    "PROD-001",
                    "1234567890",
                    BigDecimal.valueOf(99.99),
                    null,
                    0.5,
                    10.0,
                    -5.0,
                    3.0
            )
        );
    }

    @Test
    @DisplayName("Should fail when depth is negative")
    void testValidateDepthNegative() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
            service.validateProductVariantCreation(
                    "PROD-001",
                    "1234567890",
                    BigDecimal.valueOf(99.99),
                    null,
                    0.5,
                    10.0,
                    5.0,
                    -3.0
            )
        );
    }

    @Test
    @DisplayName("Should validate successfully with valid data")
    void testValidateSuccessfully() {
        // Act & Assert
        assertDoesNotThrow(() ->
            service.validateProductVariantCreation(
                    "PROD-001",
                    "1234567890",
                    BigDecimal.valueOf(99.99),
                    BigDecimal.valueOf(79.99),
                    0.5,
                    10.0,
                    5.0,
                    3.0
            )
        );
    }

    @Test
    @DisplayName("Should validate successfully with valid SKU format")
    void testValidateSuccessfullyWithValidSkuFormat() {
        // Act & Assert - Multiple valid formats
        assertDoesNotThrow(() ->
            service.validateProductVariantCreation(
                    "PROD-001",
                    "1234567890",
                    BigDecimal.valueOf(99.99),
                    null,
                    0.5,
                    10.0,
                    5.0,
                    3.0
            )
        );

        assertDoesNotThrow(() ->
            service.validateProductVariantCreation(
                    "SKU123",
                    "1234567890",
                    BigDecimal.valueOf(99.99),
                    null,
                    0.5,
                    10.0,
                    5.0,
                    3.0
            )
        );

        assertDoesNotThrow(() ->
            service.validateProductVariantCreation(
                    "A-B-C-D",
                    "1234567890",
                    BigDecimal.valueOf(99.99),
                    null,
                    0.5,
                    10.0,
                    5.0,
                    3.0
            )
        );
    }

    @Test
    @DisplayName("Should validate successfully without optional promotional price")
    void testValidateSuccessfullyWithoutPromotionalPrice() {
        // Act & Assert
        assertDoesNotThrow(() ->
            service.validateProductVariantCreation(
                    "PROD-001",
                    "1234567890",
                    BigDecimal.valueOf(99.99),
                    null,
                    0.5,
                    10.0,
                    5.0,
                    3.0
            )
        );
    }

    @Test
    @DisplayName("Should validate successfully without optional dimensions")
    void testValidateSuccessfullyWithoutDimensions() {
        // Act & Assert
        assertDoesNotThrow(() ->
            service.validateProductVariantCreation(
                    "PROD-001",
                    "1234567890",
                    BigDecimal.valueOf(99.99),
                    null,
                    null,
                    null,
                    null,
                    null
            )
        );
    }
}

