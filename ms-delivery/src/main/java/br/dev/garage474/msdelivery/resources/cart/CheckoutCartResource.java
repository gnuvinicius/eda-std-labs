package br.dev.garage474.msdelivery.resources.cart;

import br.dev.garage474.msdelivery.resources.dtos.CartCheckoutEvent;
import br.dev.garage474.msdelivery.resources.dtos.CheckoutCartResponse;
import br.dev.garage474.msdelivery.exceptions.CartAlreadyCheckedOutException;
import br.dev.garage474.msdelivery.exceptions.CartNotFoundException;
import br.dev.garage474.msdelivery.exceptions.EmptyCartCheckoutException;
import br.dev.garage474.msdelivery.models.Cart;
import br.dev.garage474.msdelivery.models.CartStatus;
import br.dev.garage474.msdelivery.repositories.CartRepository;
import br.dev.garage474.msdelivery.broker.RabbitCartCheckoutPublisher;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Tag(name = "Cart", description = "Endpoints para gerenciamento do carrinho de compras")
@RestController
@RequestMapping(path = "/api/v1/carts", produces = MediaType.APPLICATION_JSON_VALUE)
public class CheckoutCartResource {

    private static final Logger log = LoggerFactory.getLogger(CheckoutCartResource.class);

    private final CartRepository cartRepository;
    private final RabbitCartCheckoutPublisher cartCheckoutPublisher;

    public CheckoutCartResource(CartRepository cartRepository,
                                RabbitCartCheckoutPublisher  cartCheckoutPublisher) {
        this.cartRepository = cartRepository;
        this.cartCheckoutPublisher = cartCheckoutPublisher;
    }

    /**
     * Finalizes (checks out) a shopping cart and publishes it to the order processing queue.
     *
     * <p>After a successful checkout the cart status is changed to {@code CHECKED_OUT}
     * and an event is published to the RabbitMQ new-orders queue for downstream processing.</p>
     *
     * @param cartId the UUID of the cart to be checked out
     * @return a {@link CheckoutCartResponse} containing the cart ID, final status and a confirmation message
     */
    @Operation(
            summary = "Finalizar carrinho (checkout)",
            description = "Finaliza o carrinho de compras, alterando seu status para CHECKED_OUT e "
                    + "publicando o evento de novo pedido na fila do RabbitMQ para processamento assíncrono."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Checkout realizado com sucesso",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CheckoutCartResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "Carrinho não encontrado", content = @Content),
            @ApiResponse(responseCode = "409", description = "Carrinho já foi finalizado anteriormente", content = @Content)
    })
    @PostMapping(path = "/{cartId}/checkout")
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    public CheckoutCartResponse checkout(
            @Parameter(description = "UUID do carrinho a ser finalizado", required = true,
                    example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
            @PathVariable UUID cartId
    ) {
        try {
            Cart cart = getOpenCart(cartId);
            if (cart.getItems().isEmpty()) {
                throw new EmptyCartCheckoutException(cartId);
            }

            cart.setStatus(CartStatus.CHECKED_OUT);
            cart.setCheckedOutAt(LocalDateTime.now());
            Cart savedCart = cartRepository.save(cart);

            CartCheckoutEvent event = CartCheckoutEvent.toCheckoutEvent(savedCart);
            cartCheckoutPublisher.publish(event);

            log.info("cart checked out with id={} and event sent to processing", cartId);
            return new CheckoutCartResponse(cartId, CartStatus.CHECKED_OUT.name(), "cart sent to processing queue");
        } catch (Exception e) {
            log.error("error checking out cartId={}: {}", cartId, e.getMessage(), e);
            throw e;
        }
    }

    private Cart getOpenCart(UUID cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException(cartId));

        if (cart.isCheckedOut()) {
            throw new CartAlreadyCheckedOutException(cartId);
        }
        return cart;
    }

}
