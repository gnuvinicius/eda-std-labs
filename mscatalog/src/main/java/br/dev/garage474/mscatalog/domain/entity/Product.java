package br.dev.garage474.mscatalog.domain.entity;

import br.dev.garage474.mscatalog.domain.vo.Tags;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Product é o Aggregate Root do bounded context mscatalog.
 * <p>
 * Esta classe representa a raiz do agregado e é responsável por manter a consistência
 * de todas as entidades e value objects que fazem parte do agregado de Produto.
 * <p>
 * Entidades filhas (Child Entities):
 * - {@link ProductVariant}: Representa as diferentes variações/SKUs do produto (tamanho, cor, etc).
 *   Cada variante possui seu próprio preço, código SKU, código de barras e dimensões.
 * - {@link Brand}: Referência à marca do produto. Embora seja uma entidade separada,
 *   é referenciada pelo Product como parte do seu contexto.
 * - {@link Category}: Referência à categoria do produto. Também é uma entidade separada,
 *   referenciada pelo Product e pode conter subcategorias.
 * <p>
 * Value Objects (Objetos de Valor):
 * - {@link Tags}: Representa um conjunto de tags/rótulos associados ao produto.
 *   É um value object imutável que encapsula uma lista de strings.
 * <p>
 * Value Objects nas entidades filhas:
 * - {@link br.dev.garage474.mscatalog.domain.vo.Money}: Encapsula o valor monetário (amount e currency).
 *   Utilizado em ProductVariant para representar preço e preço promocional.
 * - {@link br.dev.garage474.mscatalog.domain.vo.Dimensions}: Encapsula as dimensões físicas (weight, height, width, depth).
 *   Utilizado em ProductVariant para representar as dimensões da variante.
 * <p>
 * Responsabilidades do Aggregate:
 * - Manter a integridade das variações de produto
 * - Gerenciar o ciclo de vida do produto e suas variantes
 * - Garantir que todas as mudanças sejam coerentes e consistentes
 */
@Getter
@Setter
public class Product {

    private UUID id;
    private String name;
    private String description;
    private Brand brand;
    private Category category;
    private String slug;
    private Tags tags;
    private List<ProductVariant> variants = new ArrayList<>();
}
