package br.dev.garage474.msdelivery.dtos;

/**
 * Request DTO for registering a new customer.
 */
public record CreateCustomerRequest(
                String name,
                String email,
                String phone,
                String document) {
}
