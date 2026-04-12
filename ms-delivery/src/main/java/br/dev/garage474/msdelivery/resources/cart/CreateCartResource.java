package br.dev.garage474.msdelivery.resources.cart;

import br.dev.garage474.msdelivery.models.Cart;
import br.dev.garage474.msdelivery.models.CartStatus;
import br.dev.garage474.msdelivery.repositories.CartRepository;
import br.dev.garage474.msdelivery.resources.dtos.CartResponse;
import br.dev.garage474.msdelivery.resources.dtos.CreateCartRequest;
import io.swagger.v3.oas.annotations.Operation;
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

import java.util.Optional;
import java.util.UUID;

@Tag(name = "Cart", description = "Endpoints para gerenciamento do carrinho de compras")
@RestController
@RequestMapping(path = "/api/v1/carts", produces = MediaType.APPLICATION_JSON_VALUE)
public class CreateCartResource {

    private final static Logger log = LoggerFactory.getLogger(CreateCartResource.class);

    private final CartRepository cartRepository;

    public CreateCartResource(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    /**
     * Creates a new shopping cart associated with a customer.
     *
     * @param request a {@link CreateCartRequest} containing the customer ID
     * @return a {@link CartResponse} representing the newly created cart
     */
    @Operation(
            summary = "Criar novo carrinho",
            description = "Cria um novo carrinho de compras vinculado ao cliente informado no corpo da requisição."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Carrinho criado com sucesso",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CartResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "Dados inválidos na requisição", content = @Content),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado", content = @Content)
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public ResponseEntity<CartResponse> createCart(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados necessários para criação do carrinho",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CreateCartRequest.class))
            )
            @RequestBody CreateCartRequest request
    ) {
        try {
            Optional<Cart> byCustomerId = cartRepository.findByCustomerId(request.customerId(), CartStatus.OPEN);

            if (byCustomerId.isPresent()) {
                return ResponseEntity.ok(CartResponse.toResponse(byCustomerId.get()));
            } else {
                Cart cart = new Cart();
                cart.setId(UUID.randomUUID());
                cart.setCustomerId(request.customerId());
                cart.setStatus(CartStatus.OPEN);
                Cart savedCart = cartRepository.save(cart);
                log.info("cart created with id={} customerId={}", savedCart.getId(), savedCart.getCustomerId());
                return ResponseEntity.ok(CartResponse.toResponse(savedCart));
            }
        } catch (Exception e) {
            log.error("error creating cart for customerId={}: {}", request.customerId(), e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }
}
