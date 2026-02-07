package br.dev.garage474.mscatalog.domain.service;

import br.dev.garage474.mscatalog.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;

/**
 * Domain Service - Regras de Negócio para Produtos.
 *
 * Responsabilidades:
 * - Encapsular lógica de negócio que envolve múltiplas entidades ou agregados
 * - Validar regras de negócio complexas
 * - Operações que não pertencem a uma única entidade
 *
 * Arquitetura: DDD - Domain Layer
 *
 * ⚠️ Importante:
 * - NÃO é um CRUD simples (isso fica no Repository)
 * - É para lógica complexa de negócio
 * - Exemplos: validações, cálculos, verificações cross-aggregate
 */
@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Valida as regras de negócio para criação de um novo produto.
     *
     * Regras de Negócio:
     * 1. Nome não pode ser vazio ou nulo
     * 2. Slug não pode ser vazio ou nulo
     * 3. Slug deve ser único no catálogo
     * 4. Slug deve estar em formato válido (lowercase, sem espaços, etc)
     *
     * @param name Nome do produto
     * @param slug Slug do produto (identificador único)
     * @throws IllegalArgumentException Se alguma regra for violada
     */
    public void validateProductCreation(String name, String slug) {
        // Regra 1: Validar nome
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Nome do produto não pode ser vazio");
        }

        if (name.length() < 3) {
            throw new IllegalArgumentException("Nome do produto deve ter pelo menos 3 caracteres");
        }

        if (name.length() > 255) {
            throw new IllegalArgumentException("Nome do produto não pode ter mais de 255 caracteres");
        }

        // Regra 2: Validar slug
        if (slug == null || slug.isBlank()) {
            throw new IllegalArgumentException("Slug do produto não pode ser vazio");
        }

        // Regra 3: Verificar unicidade do slug
        if (productRepository.findProductBySlug(slug).isPresent()) {
            throw new IllegalArgumentException("Slug já existe no catálogo: " + slug);
        }

        // Regra 4: Validar formato do slug
        if (!isValidSlug(slug)) {
            throw new IllegalArgumentException("Slug inválido. Use apenas letras, números e hífen");
        }
    }

    /**
     * Valida o formato do slug.
     *
     * Regras:
     * - Deve conter apenas letras minúsculas, números e hífen
     * - Não pode começar ou terminar com hífen
     * - Tamanho entre 3 e 100 caracteres
     *
     * @param slug Slug a validar
     * @return true se válido, false caso contrário
     */
    private boolean isValidSlug(String slug) {
        if (slug.length() < 3 || slug.length() > 100) {
            return false;
        }

        if (slug.startsWith("-") || slug.endsWith("-")) {
            return false;
        }

        return slug.matches("^[a-z0-9]+(?:-[a-z0-9]+)*$");
    }
}

