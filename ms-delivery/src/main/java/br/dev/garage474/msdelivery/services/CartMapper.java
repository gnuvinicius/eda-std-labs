package br.dev.garage474.msdelivery.services;

import br.dev.garage474.msdelivery.dtos.CartCheckoutEvent;
import br.dev.garage474.msdelivery.dtos.CartItemResponse;
import br.dev.garage474.msdelivery.dtos.CartResponse;
import br.dev.garage474.msdelivery.models.Cart;
import br.dev.garage474.msdelivery.models.CartItem;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class CartMapper {

    public CartResponse toResponse(Cart cart) {
        List<CartItemResponse> items = cart.getItems().stream()
                .map(this::toItemResponse)
                .toList();

        BigDecimal total = items.stream()
                .map(CartItemResponse::subtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CartResponse(
                cart.getId(),
                cart.getCustomerId(),
                cart.getStatus(),
                items,
                total,
                cart.getCreatedAt(),
                cart.getUpdatedAt(),
                cart.getCheckedOutAt()
        );
    }

    public CartCheckoutEvent toCheckoutEvent(Cart cart) {
        CartResponse cartResponse = toResponse(cart);
        return new CartCheckoutEvent(
                cartResponse.id(),
                cartResponse.customerId(),
                cartResponse.totalAmount(),
                cartResponse.checkedOutAt(),
                cartResponse.items()
        );
    }

    private CartItemResponse toItemResponse(CartItem item) {
        return new CartItemResponse(
                item.getId(),
                item.getProductId(),
                item.getProductName(),
                item.getQuantity(),
                item.getUnitPrice(),
                item.subtotal()
        );
    }
}

