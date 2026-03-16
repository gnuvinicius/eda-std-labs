package br.dev.garage474.msdelivery.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO representing a registered customer.
 */
public record CustomerResponse(
        UUID id,
        String name,
        String email,
        String phone,
        String document,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}

