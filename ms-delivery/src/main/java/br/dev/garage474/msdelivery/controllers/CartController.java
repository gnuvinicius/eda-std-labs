package br.dev.garage474.msdelivery.controllers;

import br.dev.garage474.msdelivery.dtos.AddCartItemRequest;
import br.dev.garage474.msdelivery.dtos.CartResponse;
import br.dev.garage474.msdelivery.dtos.CheckoutCartResponse;
import br.dev.garage474.msdelivery.dtos.CreateCartRequest;
import br.dev.garage474.msdelivery.services.CartService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/carts", produces = MediaType.APPLICATION_JSON_VALUE)
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public CartResponse createCart(@Valid @RequestBody CreateCartRequest request) {
        return cartService.createCart(request);
    }

    @PostMapping(path = "/{cartId}/items", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public CartResponse addItem(
            @PathVariable UUID cartId,
            @Valid @RequestBody AddCartItemRequest request
    ) {
        return cartService.addItem(cartId, request);
    }

    @PostMapping(path = "/{cartId}/checkout")
    @ResponseStatus(HttpStatus.OK)
    public CheckoutCartResponse checkout(@PathVariable UUID cartId) {
        return cartService.checkout(cartId);
    }
}

