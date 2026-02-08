package br.dev.garage474.mscatalog.adapters.in.web.dto;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * DTO para resposta de categoria.
 * Utilizado como saída nos endpoints REST.
 */
public record CategoryResponse(
    UUID id,
    String name,
    CategoryResponse parentCategory,
    List<CategoryResponse> subCategories
) implements Serializable {
}

