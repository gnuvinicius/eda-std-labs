package br.dev.garage474.msdelivery.dtos;

import java.util.UUID;

public record CheckoutCartResponse(
        UUID cartId,
        String status,
        String message
) {
}

