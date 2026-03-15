package br.dev.garage474.msdelivery.dtos;

import java.math.BigDecimal;
import java.util.UUID;

public record CartItemResponse(
        Long id,
        UUID productId,
        String productName,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal subtotal
) {
}

