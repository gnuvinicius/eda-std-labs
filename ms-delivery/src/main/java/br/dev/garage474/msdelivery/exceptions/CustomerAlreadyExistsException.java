package br.dev.garage474.msdelivery.exceptions;

/**
 * Thrown when a customer registration is attempted with an email or document
 * that already belongs to an existing customer.
 */
public class CustomerAlreadyExistsException extends RuntimeException {

    public CustomerAlreadyExistsException(String field, String value) {
        super("customer already exists with " + field + ": " + value);
    }
}

