package br.dev.garage474.mscatalog.adapters.in.dto;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO para resposta de marca.
 * Utilizado como saída nos endpoints REST.
 */
public record BrandResponse(
    UUID id,
    String name
) implements Serializable {
}

