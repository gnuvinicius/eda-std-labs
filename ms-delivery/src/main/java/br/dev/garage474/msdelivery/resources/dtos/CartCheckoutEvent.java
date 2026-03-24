package br.dev.garage474.msdelivery.resources.dtos;

import br.dev.garage474.msdelivery.models.Cart;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class CartCheckoutEvent {

    private UUID orderId;
    private UUID customerId;
    private BigDecimal totalAmount;
    private LocalDateTime checkedOutAt;
    private List<CartItemResponse> items;

    public static CartCheckoutEvent toCheckoutEvent(Cart cart) {
        CartResponse cartResponse = CartResponse.toResponse(cart);
        return new CartCheckoutEvent(
                cartResponse.getId(),
                cartResponse.getCustomerId(),
                cartResponse.getTotalAmount(),
                cartResponse.getCheckedOutAt(),
                cartResponse.getItems()
        );
    }
}