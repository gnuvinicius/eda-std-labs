package br.dev.garage474.msdelivery.dtos;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateCartRequest(@NotNull UUID customerId) {
}

