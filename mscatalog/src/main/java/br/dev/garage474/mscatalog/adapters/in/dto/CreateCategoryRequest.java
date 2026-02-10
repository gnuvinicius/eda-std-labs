package br.dev.garage474.mscatalog.adapters.in.dto;

import java.io.Serializable;

/**
 * DTO para requisição de criação de categoria.
 * Utilizado como entrada nos endpoints REST.
 */
public record CreateCategoryRequest(
    String name,
    String parentCategoryId
) implements Serializable {
}

