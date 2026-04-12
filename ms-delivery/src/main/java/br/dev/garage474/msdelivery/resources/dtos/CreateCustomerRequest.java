package br.dev.garage474.msdelivery.resources.dtos;

import br.dev.garage474.msdelivery.models.Customer;
import lombok.Getter;

import java.util.UUID;

/**
 * Request DTO for registering a new customer.
 */
@Getter
public class CreateCustomerRequest {

    private String name;
    private String email;
    private String phone;
    private String document;

    public Customer toEntity() {
        Customer customer = new Customer();
        customer.setId(UUID.randomUUID());
        customer.setName(this.name);
        customer.setEmail(this.email.toLowerCase().trim());
        customer.setPhone(this.phone);
        customer.setDocument(this.document.replaceAll("[^0-9]", ""));
        return customer;
    }
}
