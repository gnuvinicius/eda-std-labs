package br.dev.garage474.msdelivery.resources.dtos;

import java.util.UUID;

public record CheckoutCartResponse(
        UUID cartId,
        String status,
        String message
) {
}

