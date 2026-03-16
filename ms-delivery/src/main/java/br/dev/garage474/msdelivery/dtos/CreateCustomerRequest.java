package br.dev.garage474.msdelivery.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for registering a new customer.
 */
public record CreateCustomerRequest(
        String name,
        String email,
        String phone,
        String document
) {
}

