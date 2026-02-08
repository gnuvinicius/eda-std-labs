package br.dev.garage474.mscatalog.applications.usecase;

import br.dev.garage474.mscatalog.domain.repositories.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Use Case para deletar uma variante de produto.
 *
 * Responsabilidades:
 * - Receber comando de deleção
 * - Verificar se a variante existe
 * - Deletar a variante
 *
 * Arquitetura: Clean Architecture - Application Layer
 */
@Service
public class DeleteProductVariantUseCase {

    private final ProductRepository productRepository;

    public DeleteProductVariantUseCase(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Executa o caso de uso de deleção de variante de produto.
     *
     * @param command Comando com dados da variante a deletar
     */
    @Transactional
    public void execute(DeleteProductVariantCommand command) {
        // 1. Validar que a variante existe
        if (!productRepository.existsProductVariant(command.variantId())) {
            throw new IllegalArgumentException("Variante não encontrada: " + command.variantId());
        }

        // 2. Deletar variante
        productRepository.deleteProductVariant(command.variantId());
    }

    /**
     * Comando para deletar variante de produto.
     *
     * Record imutável com os dados necessários para deletar uma variante.
     */
    public record DeleteProductVariantCommand(
        UUID variantId
    ) {}
}

