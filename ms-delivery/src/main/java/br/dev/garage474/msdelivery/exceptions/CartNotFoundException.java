package br.dev.garage474.msdelivery.exceptions;

import java.util.UUID;

public class CartNotFoundException extends RuntimeException {

    public CartNotFoundException(UUID cartId) {
        super("cart not found for id: " + cartId);
    }
}

