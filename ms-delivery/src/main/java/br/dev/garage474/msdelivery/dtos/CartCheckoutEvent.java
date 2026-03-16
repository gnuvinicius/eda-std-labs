package br.dev.garage474.msdelivery.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record CartCheckoutEvent(
        UUID cartId,
        UUID customerId,
        BigDecimal totalAmount,
        LocalDateTime checkedOutAt,
        List<CartItemResponse> items
) {
}
