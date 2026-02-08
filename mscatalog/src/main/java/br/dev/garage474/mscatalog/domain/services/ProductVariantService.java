package br.dev.garage474.mscatalog.domain.services;

import br.dev.garage474.mscatalog.domain.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Domain Service - Regras de Negócio para ProductVariant.
 *
 * Responsabilidades:
 * - Encapsular lógica de negócio que envolve múltiplas entidades ou agregados
 * - Validar regras de negócio complexas
 * - Operações que não pertencem a uma única entidade
 *
 * Arquitetura: DDD - Domain Layer
 */
@Service
public class ProductVariantService {

    private final ProductRepository productRepository;

    public ProductVariantService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Valida as regras de negócio para criação de uma nova variante de produto.
     *
     * Regras de Negócio:
     * 1. SKU Code não pode ser vazio ou nulo
     * 2. SKU Code deve ser único no catálogo
     * 3. SKU Code deve estar em formato válido (sem espaços especiais)
     * 4. Preço não pode ser negativo
     * 5. Preço promocional não pode ser maior que preço regular
     * 6. Dimensões (se fornecidas) não podem ser negativas
     *
     * @param skuCode Código SKU
     * @param barcode Código de barras (opcional)
     * @param priceAmount Valor do preço
     * @param promotionalPriceAmount Valor do preço promocional (opcional)
     * @throws IllegalArgumentException Se alguma regra for violada
     */
    public void validateProductVariantCreation(
            String skuCode,
            String barcode,
            BigDecimal priceAmount,
            BigDecimal promotionalPriceAmount,
            Double weight,
            Double height,
            Double width,
            Double depth) {

        // Regra 1: Validar SKU Code
        if (skuCode == null || skuCode.isBlank()) {
            throw new IllegalArgumentException("SKU Code não pode ser vazio");
        }

        if (skuCode.length() < 3 || skuCode.length() > 50) {
            throw new IllegalArgumentException("SKU Code deve ter entre 3 e 50 caracteres");
        }

        // Regra 2: Verificar unicidade do SKU Code
        if (productRepository.findProductVariantBySkuCode(skuCode).isPresent()) {
            throw new IllegalArgumentException("SKU Code já existe no catálogo: " + skuCode);
        }

        // Regra 3: Validar formato do SKU Code
        if (!isValidSkuCode(skuCode)) {
            throw new IllegalArgumentException("SKU Code inválido. Use apenas letras, números e hífen");
        }

        // Regra 4: Validar preço
        if (priceAmount == null || priceAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Preço deve ser maior que zero");
        }

        // Regra 5: Validar preço promocional
        if (promotionalPriceAmount != null) {
            if (promotionalPriceAmount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Preço promocional deve ser maior que zero");
            }
            if (promotionalPriceAmount.compareTo(priceAmount) > 0) {
                throw new IllegalArgumentException("Preço promocional não pode ser maior que o preço regular");
            }
        }

        // Regra 6: Validar dimensões
        if (weight != null && weight < 0) {
            throw new IllegalArgumentException("Peso não pode ser negativo");
        }
        if (height != null && height < 0) {
            throw new IllegalArgumentException("Altura não pode ser negativa");
        }
        if (width != null && width < 0) {
            throw new IllegalArgumentException("Largura não pode ser negativa");
        }
        if (depth != null && depth < 0) {
            throw new IllegalArgumentException("Profundidade não pode ser negativa");
        }
    }

    /**
     * Valida o formato do SKU Code.
     *
     * Regras:
     * - Deve conter apenas letras maiúsculas, números e hífen
     * - Não pode começar ou terminar com hífen
     *
     * @param skuCode SKU Code a validar
     * @return true se válido, false caso contrário
     */
    private boolean isValidSkuCode(String skuCode) {
        if (skuCode.startsWith("-") || skuCode.endsWith("-")) {
            return false;
        }

        return skuCode.matches("^[A-Z0-9]+(?:-[A-Z0-9]+)*$");
    }
}

