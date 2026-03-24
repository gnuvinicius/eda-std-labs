package br.dev.garage474.msdelivery.resources.dtos;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateCartRequest(@NotNull UUID customerId) {
}

