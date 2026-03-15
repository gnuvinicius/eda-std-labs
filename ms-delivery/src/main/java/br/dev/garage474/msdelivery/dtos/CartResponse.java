package br.dev.garage474.msdelivery.dtos;

import br.dev.garage474.msdelivery.models.CartStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record CartResponse(
        UUID id,
        UUID customerId,
        CartStatus status,
        List<CartItemResponse> items,
        BigDecimal totalAmount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime checkedOutAt
) {
}

