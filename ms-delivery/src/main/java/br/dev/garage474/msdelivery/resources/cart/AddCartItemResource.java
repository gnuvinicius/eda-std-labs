package br.dev.garage474.msdelivery.resources.cart;

import br.dev.garage474.msdelivery.exceptions.CartAlreadyCheckedOutException;
import br.dev.garage474.msdelivery.exceptions.CartNotFoundException;
import br.dev.garage474.msdelivery.models.Cart;
import br.dev.garage474.msdelivery.models.CartItem;
import br.dev.garage474.msdelivery.repositories.CartRepository;
import br.dev.garage474.msdelivery.resources.dtos.AddCartItemRequest;
import br.dev.garage474.msdelivery.resources.dtos.CartResponse;
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
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Cart", description = "Endpoints para gerenciamento do carrinho de compras")
@RestController
@RequestMapping(path = "/api/v1/carts", produces = MediaType.APPLICATION_JSON_VALUE)
public class AddCartItemResource {

    private final static Logger log = LoggerFactory.getLogger(AddCartItemResource.class);

    private final CartRepository cartRepository;

    public AddCartItemResource(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    /**
     * Adds an item to an existing shopping cart.
     *
     * @param cartId  the UUID of the target cart
     * @param request an {@link AddCartItemRequest} containing product details and quantity
     * @return a {@link CartResponse} with the updated cart contents
     */
    @Operation(
            summary = "Adicionar item ao carrinho",
            description = "Adiciona um produto ao carrinho de compras informado. "
                    + "Caso o produto já exista no carrinho, a quantidade é incrementada."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Item adicionado com sucesso",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CartResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "Dados inválidos na requisição", content = @Content),
            @ApiResponse(responseCode = "404", description = "Carrinho não encontrado", content = @Content)
    })
    @PostMapping(path = "/{cartId}/items", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    public ResponseEntity<CartResponse> addItem(
            @Parameter(description = "UUID do carrinho", required = true, example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
            @PathVariable UUID cartId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados do produto a ser adicionado ao carrinho",
                    required = true,
                    content = @Content(schema = @Schema(implementation = AddCartItemRequest.class))
            )
            @RequestBody AddCartItemRequest request
    ) {
        try {
            Cart cart = getOpenCart(cartId);

            CartItem cartItem = new CartItem();
            cartItem.setProductId(request.productId());
            cartItem.setProductName(request.productName());
            cartItem.setQuantity(request.quantity());
            cartItem.setUnitPrice(request.unitPrice());

            cart.addItem(cartItem);
            Cart savedCart = cartRepository.save(cart);

            log.info("item added to cartId={} productId={} quantity={}", cartId, request.productId(), request.quantity());
            return ResponseEntity.ok(CartResponse.toResponse(savedCart));
        } catch (Exception e) {
            log.error("error adding item to cartId={}: {}", cartId, e.getMessage(), e);
            return ResponseEntity.badRequest().build();
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
