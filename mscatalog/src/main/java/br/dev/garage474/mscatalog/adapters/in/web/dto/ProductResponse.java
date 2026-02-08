package br.dev.garage474.mscatalog.adapters.in.web.dto;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * DTO para resposta de listagem de produtos.
 * Utilizado como saída nos endpoints REST.
 */
public record ProductResponse(
    UUID id,
    String name,
    String description,
    String slug,
    BrandResponse brand,
    CategoryResponse category,
    List<String> tags
) implements Serializable {

    public record BrandResponse(
        UUID id,
        String name
    ) {}

    public record CategoryResponse(
        UUID id,
        String name
    ) {}
}

