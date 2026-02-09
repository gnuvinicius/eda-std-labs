package br.dev.garage474.mscatalog.adapters.in.web.dto;

import java.io.Serializable;
import java.util.List;

/**
 * DTO para resposta paginada do Showcase.
 * Utilizado para retornar produtos com informações de paginação ao frontend/mobile.
 */
public record ShowcasePageResponse(
    List<ShowcaseProductResponse> content,
    PageInfoResponse pageInfo
) implements Serializable {

    public record PageInfoResponse(
        int page,
        int size,
        int totalPages,
        long totalElements
    ) implements Serializable {}
}

