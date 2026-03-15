package br.dev.garage474.msdelivery.exceptions;

import java.util.UUID;

public class EmptyCartCheckoutException extends RuntimeException {

    public EmptyCartCheckoutException(UUID cartId) {
        super("cannot checkout an empty cart: " + cartId);
    }
}

