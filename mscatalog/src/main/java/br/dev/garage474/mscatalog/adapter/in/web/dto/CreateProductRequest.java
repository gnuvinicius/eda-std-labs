package br.dev.garage474.mscatalog.adapter.in.web.dto;

import java.io.Serializable;
import java.util.List;

/**
 * DTO para requisição de criação de produto.
 * Utilizado como entrada nos endpoints REST.
 */
public record CreateProductRequest(
    String name,
    String description,
    String slug,
    String brandId,
    String categoryId,
    List<String> tags
) implements Serializable {
}

