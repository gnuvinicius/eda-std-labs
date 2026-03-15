package br.dev.garage474.msdelivery.exceptions;

import java.util.UUID;

public class CartAlreadyCheckedOutException extends RuntimeException {

    public CartAlreadyCheckedOutException(UUID cartId) {
        super("cart is already checked out for id: " + cartId);
    }
}

