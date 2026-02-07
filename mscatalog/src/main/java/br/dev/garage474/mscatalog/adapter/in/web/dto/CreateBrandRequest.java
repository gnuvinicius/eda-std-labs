package br.dev.garage474.mscatalog.adapter.in.web.dto;

import java.io.Serializable;

/**
 * DTO para requisição de criação de marca.
 * Utilizado como entrada nos endpoints REST.
 */
public record CreateBrandRequest(
    String name
) implements Serializable {
}

