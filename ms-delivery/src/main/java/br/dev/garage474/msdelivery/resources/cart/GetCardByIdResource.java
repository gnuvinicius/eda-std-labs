package br.dev.garage474.msdelivery.resources.cart;

import br.dev.garage474.msdelivery.exceptions.CartNotFoundException;
import br.dev.garage474.msdelivery.repositories.CartRepository;
import br.dev.garage474.msdelivery.resources.dtos.CartResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Cart", description = "Endpoints para gerenciamento do carrinho de compras")
@RestController
@RequestMapping(path = "/api/v1/carts", produces = MediaType.APPLICATION_JSON_VALUE)
public class GetCardByIdResource {

    private final CartRepository cartRepository;

    public GetCardByIdResource(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    /**
     * Retrieves a cart by its unique identifier.
     *
     * @param cartId the UUID of the cart to be retrieved
     * @return a {@link CartResponse} containing the cart details
     */
    @Operation(
            summary = "Buscar carrinho por ID",
            description = "Retorna os detalhes de um carrinho de compras a partir do seu identificador único (UUID)."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Carrinho encontrado com sucesso",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CartResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "Carrinho não encontrado", content = @Content),
            @ApiResponse(responseCode = "400", description = "UUID inválido fornecido", content = @Content)
    })
    @GetMapping(path = "/{cartId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<CartResponse> getCartById(
            @Parameter(description = "UUID do carrinho", required = true, example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
            @PathVariable UUID cartId
    ) {
        CartResponse cartResponse = cartRepository.findById(cartId)
                .map(CartResponse::toResponse)
                .orElseThrow(() -> new CartNotFoundException(cartId));

        return ResponseEntity.ok(cartResponse);
    }
}
